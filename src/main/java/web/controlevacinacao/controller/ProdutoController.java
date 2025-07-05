package web.controlevacinacao.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxLocation;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import web.controlevacinacao.filter.ProdutoFilter;
import web.controlevacinacao.model.Produto;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.notificacao.NotificacaoSweetAlert2;
import web.controlevacinacao.notificacao.TipoNotificaoSweetAlert2;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.ProdutoRepository;
import web.controlevacinacao.service.ProdutoService;

@Controller
public class ProdutoController {

    private static final Logger logger = LoggerFactory.getLogger(ProdutoController.class);

    private final ProdutoRepository produtoRepository;
    private final ProdutoService produtoService;

    public ProdutoController(ProdutoRepository produtoRepository, ProdutoService produtoService) {
        this.produtoRepository = produtoRepository;
        this.produtoService = produtoService;
    }

    @HxRequest
    @GetMapping("/produtos/abrirpesquisa")
    public String abrirPesquisaHTMX() {
        return "produtos/pesquisar :: formulario";
    }

    @HxRequest
    @GetMapping("/produtos/pesquisar")
    public String mostrarProdutosPesquisaHTMX(ProdutoFilter filtro, Model model,
            @PageableDefault(size = 8) @SortDefault(sort = "nome", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Produto> pagina = produtoRepository.pesquisar(filtro, pageable);
        logger.info("Produtos pesquisados: {}", pagina);
        PageWrapper<Produto> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "produtos/listar :: tabela";
    }

    @HxRequest
    @GetMapping("/produtos/cadastrar")
    public String abrirCadastroHTMX(Produto produto) {
        return "produtos/cadastrar :: formulario";
    }

    @HxRequest
    @PostMapping("/produtos/cadastrar")
    public String cadastrarHTMX(@Valid Produto produto,
            BindingResult resultado,
            RedirectAttributes attributes) {
        if (resultado.hasErrors()) {
            logger.info("O produto recebido para cadastrar não é válido.");
            for (FieldError erro : resultado.getFieldErrors()) {
                logger.info("{}", erro);
            }
            for (ObjectError erro : resultado.getGlobalErrors()) {
                logger.info("{}", erro);
            }
            return "produtos/cadastrar :: formulario";
        } else {
            produtoService.salvar(produto);
            attributes.addFlashAttribute("notificacao",
                    new NotificacaoSweetAlert2("Produto cadastrado com sucesso!",
                            TipoNotificaoSweetAlert2.SUCCESS, 4000));
            return "redirect:/produtos/cadastrar";
        }
    }

    @HxRequest
    @GetMapping("/produtos/alterar/{id}")
    public String abrirAlterarHTMX(@PathVariable("id") Long id, Model model) {
        Produto produto = produtoRepository.findByIdAndStatus(id, Status.ATIVO);
        if (produto != null) {
            model.addAttribute("produto", produto);
            return "produtos/alterar :: formulario";
        } else {
            model.addAttribute("mensagem", "Não existe um produto com este ID.");
            return "mensagem :: texto";
        }
    }

    @HxRequest
    @PostMapping("/produtos/alterar")
    public String alterarHTMX(@Valid Produto produto, BindingResult resultado,
            RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            logger.info("O produto recebido para alterar não é válido.");
            for (FieldError erro : resultado.getFieldErrors()) {
                logger.info("{}", erro);
            }
            for (ObjectError erro : resultado.getGlobalErrors()) {
                logger.info("{}", erro);
            }
            return "produtos/alterar :: formulario";
        } else {
            produtoService.alterar(produto);
            redirectAttributes.addFlashAttribute("notificacao",
                    new NotificacaoSweetAlert2("Produto alterado com sucesso!",
                            TipoNotificaoSweetAlert2.SUCCESS, 4000));
            return "redirect:/produtos/abrirpesquisa";
        }
    }

    @HxRequest
    @HxLocation(path = "/mensagem", target = "#main", swap = "outerHTML")
    @GetMapping("/produtos/remover/{id}")
    public String removerHTMX(@PathVariable("id") Long id, RedirectAttributes attributes) {
        produtoService.remover(id);
        attributes.addFlashAttribute("notificacao",
                new NotificacaoSweetAlert2("Produto removido com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
        return "redirect:/produtos/abrirpesquisa";
    }

    @GetMapping("/produtos/listar")
    public String listarProdutos(Model model, Pageable pageable, HttpServletRequest request) {
        Page<Produto> pagina = produtoRepository.findAll(pageable);
        PageWrapper<Produto> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "produtos/listar";
    }

}
