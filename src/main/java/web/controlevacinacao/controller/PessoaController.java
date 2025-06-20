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
import web.controlevacinacao.filter.PessoaFilter;
import web.controlevacinacao.model.Pessoa;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.notificacao.NotificacaoSweetAlert2;
import web.controlevacinacao.notificacao.TipoNotificaoSweetAlert2;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.PessoaRepository;
import web.controlevacinacao.service.PessoaService;

@Controller
public class PessoaController {

    private static final Logger logger = LoggerFactory.getLogger(PessoaController.class);
    private final PessoaRepository pessoaRepository;
    private final PessoaService pessoaService;

    public PessoaController(PessoaRepository pessoaRepository, PessoaService pessoaService) {
        this.pessoaRepository = pessoaRepository;
        this.pessoaService = pessoaService;
    }

    @HxRequest
    @GetMapping("/pessoas/abrirpesquisa")
    public String abrirPesquisaHTMX() {
        return "pessoas/pesquisar :: formulario";
    }

    @HxRequest
    @GetMapping("/pessoas/pesquisar")
    public String mostrarPessoasPesquisaHTMX(PessoaFilter filtro, Model model,
            @PageableDefault(size = 8) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Pessoa> pagina = pessoaRepository.pesquisar(filtro, pageable);
        logger.info("Pessoas pesquisadas: {}", pagina);
        PageWrapper<Pessoa> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "pessoas/listar :: tabela";
    }

    @HxRequest
    @GetMapping("/pessoas/cadastrar")
    public String abrirCadastroHTMX(Pessoa pessoa) {
        return "pessoas/cadastrar :: formulario";
    }

    @HxRequest
    @PostMapping("/pessoas/cadastrar")
    public String cadastrarHTMX(@Valid Pessoa pessoa,
            BindingResult resultado,
            RedirectAttributes attributes) {
        if (resultado.hasErrors()) {
            logger.info("A pessoa recebida para cadastrar não é válida.");
            logger.info("Erros encontrados:");
            for (FieldError erro : resultado.getFieldErrors()) {
                logger.info("{}", erro);
            }
            for (ObjectError erro : resultado.getGlobalErrors()) {
                logger.info("{}", erro);
            }
            return "pessoas/cadastrar :: formulario";
        } else {
            pessoaService.salvar(pessoa);

            attributes.addFlashAttribute("notificacao",
                    new NotificacaoSweetAlert2("Pessoa cadastrada com sucesso!",
                            TipoNotificaoSweetAlert2.SUCCESS, 4000));

            return "redirect:/pessoas/cadastrar";
        }
    }

    // @HxRequest
    // @GetMapping("/mensagem")
    // public String mostrarMensagemHTMX(String mensagem, Model model) {
    //     if (mensagem != null && !mensagem.isEmpty()) {
    //         model.addAttribute("mensagem", mensagem);
    //     }
    //     return "mensagem :: texto";
    // }

    @HxRequest
    @GetMapping("/pessoas/alterar/{codigo}")
    public String abrirAlterarHTMX(@PathVariable("codigo") Long codigo, Model model) {
        Pessoa pessoa = pessoaRepository.findByCodigoAndStatus(codigo, Status.ATIVO);
        if (pessoa != null) {
            model.addAttribute("pessoa", pessoa);
            return "pessoas/alterar :: formulario";
        } else {
            model.addAttribute("mensagem", "Não existe uma pessoa com esse código");
            return "mensagem :: texto";
        }
    }

    @HxRequest
    @PostMapping("/pessoas/alterar")
    public String alterarHTMX(@Valid Pessoa pessoa, BindingResult resultado,
            RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            logger.info("A pessoa recebida para alterar não é válida.");
            logger.info("Erros encontrados:");
            for (FieldError erro : resultado.getFieldErrors()) {
                logger.info("{}", erro);
            }
            for (ObjectError erro : resultado.getGlobalErrors()) {
                logger.info("{}", erro);
            }
            return "pessoas/alterar :: formulario";
        } else {
            pessoaService.alterar(pessoa);
            redirectAttributes.addFlashAttribute("notificacao",
                    new NotificacaoSweetAlert2("Pessoa alterada com sucesso!",
                            TipoNotificaoSweetAlert2.SUCCESS, 4000));
            return "redirect:/pessoas/abrirpesquisa";
        }
    }

    @HxRequest
    @HxLocation(path = "/mensagem", target = "#main", swap = "outerHTML")
    @GetMapping("/pessoas/remover/{codigo}")
    public String removerHTMX(@PathVariable("codigo") Long codigo, RedirectAttributes attributes) {
        pessoaService.remover(codigo);
        attributes.addFlashAttribute("notificacao",
                new NotificacaoSweetAlert2("Pessoa removida com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
        return "redirect:/pessoas/abrirpesquisa";
    }
}
