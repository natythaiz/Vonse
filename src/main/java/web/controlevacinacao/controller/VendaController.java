package web.controlevacinacao.controller;

import java.time.LocalDate;
import java.util.ArrayList;

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
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import web.controlevacinacao.filter.ClienteFilter;
import web.controlevacinacao.filter.ProdutoFilter;
import web.controlevacinacao.filter.VendaFilter;
import web.controlevacinacao.model.Cliente;
import web.controlevacinacao.model.ItemVenda;
import web.controlevacinacao.model.Produto;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.Venda;
import web.controlevacinacao.notificacao.NotificacaoSweetAlert2;
import web.controlevacinacao.notificacao.TipoNotificaoSweetAlert2;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.ClienteRepository;
import web.controlevacinacao.repository.ProdutoRepository;
import web.controlevacinacao.repository.VendaRepository;
import web.controlevacinacao.service.ProdutoService;
import web.controlevacinacao.service.VendaService;

@Controller
public class VendaController {

    private static final Logger logger = LoggerFactory.getLogger(VendaController.class);

    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final VendaService vendaService;
    private final ProdutoService produtoService;
    private final VendaRepository vendaRepository;

    public VendaController(ClienteRepository clienteRepository, ProdutoRepository produtoRepository,
            VendaService vendaService, ProdutoService produtoService, VendaRepository vendaRepository) {
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
        this.vendaService = vendaService;
        this.produtoService = produtoService;
        this.vendaRepository = vendaRepository;
    }

    @HxRequest
    @GetMapping("/vendas/cadastrar")
    public String abrirPesquisaCliente() {
        return "vendas/pesquisacliente :: formulario";
    }

    @HxRequest
    @GetMapping("/vendas/pesquisarcliente")
    public String mostrarClientesPesquisa(ClienteFilter filtro, Model model,
            @PageableDefault(size = 8) @SortDefault(sort = "nome") Pageable pageable,
            HttpServletRequest request) {
        Page<Cliente> pagina = clienteRepository.pesquisar(filtro, pageable);
        logger.info("Clientes pesquisados: {}", pagina);
        PageWrapper<Cliente> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "vendas/escolhercliente :: tabela";
    }

    @HxRequest
    @GetMapping("/vendas/cliente/{id}")
    public String abrirPesquisaProduto(@PathVariable("id") Long id, HttpSession sessao, Model model) {
        Cliente cliente = clienteRepository.findByIdAndStatus(id, Status.ATIVO);
        if (cliente != null) {
            Venda venda = new Venda();
            venda.setCliente(cliente);
            venda.setData(LocalDate.now());
            venda.setItensVendidos(new ArrayList<>());
            sessao.setAttribute("venda", venda);
            return "vendas/pesquisarproduto :: formulario";
        } else {
            model.addAttribute("mensagem", "Cliente não encontrado.");
            return "mensagem :: texto";
        }
    }

    @HxRequest
    @GetMapping("/vendas/pesquisarproduto")
    public String mostrarProdutosPesquisa(ProdutoFilter filtro, Model model,
            @PageableDefault(size = 8) @SortDefault(sort = "nome") Pageable pageable,
            HttpServletRequest request) {
        Page<Produto> pagina = produtoRepository.pesquisar(filtro, pageable);
        logger.info("Produtos pesquisados: {}", pagina);
        PageWrapper<Produto> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "vendas/escolherproduto :: tabela";
    }

    @HxRequest
    @GetMapping("/vendas/produto/{id}")
    public String abrirAdicionarItem(@PathVariable("id") Long id, Model model, HttpSession sessao) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        Venda venda = (Venda) sessao.getAttribute("venda");
        if (venda == null) {
            venda = new Venda();
            sessao.setAttribute("venda", venda);
        }
        ItemVenda itemVenda = new ItemVenda();
        itemVenda.setProduto(produto);
        itemVenda.setPrecoUnitario(produto.getPrecoVenda());
        model.addAttribute("itemVenda", itemVenda);
        return "vendas/adicionaritem :: formulario";
    }

    @HxRequest
    @PostMapping("/vendas/adicionaritem")
    public String adicionarItemVenda(@Valid ItemVenda itemVenda, BindingResult resultado,
            HttpSession sessao, RedirectAttributes attributes, Model model) {
        // Buscar o produto atualizado no banco para checar o estoque real
        Produto produto = produtoRepository.findById(itemVenda.getProduto().getId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        // Validar se a quantidade não excede o estoque
        if (itemVenda.getQuantidade() > produto.getEstoque()) {
            resultado.rejectValue("quantidade", null,
                    "Quantidade solicitada (" + itemVenda.getQuantidade() + ") excede o estoque disponível ("
                            + produto.getEstoque() + ").");

            model.addAttribute("mensagem", "Não é possível adicionar mais itens do que a quantidade de estoque disponível. Por favor refaça a venda e se atente aos valores informados.");
            return "mensagem :: texto";
        }

        if (resultado.hasErrors()) {
            for (FieldError erro : resultado.getFieldErrors()) {
                logger.info("{}", erro);
            }
            for (ObjectError erro : resultado.getGlobalErrors()) {
                logger.info("{}", erro);
            }
            model.addAttribute("itemVenda", itemVenda);
            return "vendas/adicionaritem :: formulario";
        }

        // Calcular subtotal e adicionar à venda na sessão
        Venda venda = (Venda) sessao.getAttribute("venda");
        itemVenda.setPrecoUnitario(produto.getPrecoVenda());
        itemVenda.setSubTotal(itemVenda.getPrecoUnitario() * itemVenda.getQuantidade());
        itemVenda.setVenda(venda);

        venda.getItensVendidos().add(itemVenda);

        double valorTotal = venda.getItensVendidos().stream()
                .mapToDouble(ItemVenda::getSubTotal)
                .sum();
        venda.setValorTotal(valorTotal);

        sessao.setAttribute("venda", venda);

        attributes.addFlashAttribute("notificacao",
                new NotificacaoSweetAlert2("Item '" + itemVenda.getProduto().getNome() + "' adicionado à venda.",
                        TipoNotificaoSweetAlert2.SUCCESS, 3000));

        return "redirect:/vendas/pesquisarproduto";
    }

    @HxRequest
    @GetMapping("/vendas/cadastrarFinal")
    public String mostrarTelaCadastro(HttpSession sessao, Model model) {
        Venda venda = (Venda) sessao.getAttribute("venda");

        if (venda == null || venda.getItensVendidos().isEmpty()) {
            model.addAttribute("mensagem", "Não é possível finalizar uma venda sem itens.");
            return "mensagem :: texto";
        }

        // Garante que cada ItemVenda tenha o Produto completo
        for (ItemVenda item : venda.getItensVendidos()) {
            if (item.getProduto() != null && (item.getProduto().getNome() == null || item.getProduto().getNome().isEmpty())) {
                Produto produtoCompleto = produtoRepository.findById(item.getProduto().getId()).orElse(null);
                item.setProduto(produtoCompleto);
            }
        }

        sessao.setAttribute("venda", venda); // Atualiza na sessão também

        model.addAttribute("venda", venda);
        return "vendas/cadastrarFinal :: formulario";
    }

    @HxRequest
    @PostMapping("/vendas/finalizar")
    public String finalizarCadastro(HttpSession sessao, RedirectAttributes attributes) {
        Venda venda = (Venda) sessao.getAttribute("venda");
        if (venda != null && !venda.getItensVendidos().isEmpty()) {
            vendaService.salvar(venda);
            sessao.removeAttribute("venda");
            attributes.addFlashAttribute("notificacao",
                    new NotificacaoSweetAlert2("Venda cadastrada com sucesso!", TipoNotificaoSweetAlert2.SUCCESS,
                            4000));
            return "redirect:/vendas/cadastrar";
        } else {
            attributes.addFlashAttribute("notificacao",
                    new NotificacaoSweetAlert2("A venda precisa ter pelo menos um item!",
                            TipoNotificaoSweetAlert2.WARNING, 4000));
            return "redirect:/vendas/pesquisarproduto";
        }
    }

    @HxRequest
    @GetMapping("/vendas/abrirpesquisa")
    public String abrirPesquisa() {
        return "vendas/pesquisar :: formulario";
    }

    @HxRequest
    @GetMapping("/vendas/pesquisar")
    public String pesquisar(VendaFilter filtro, Model model,
            @PageableDefault(size = 8) @SortDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Venda> pagina = vendaRepository.pesquisar(filtro, pageable);
        logger.info("Vendas pesquisadas: {}", pagina);
        PageWrapper<Venda> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "vendas/listar :: tabela";
    }

    @HxRequest
    @HxLocation(path = "/mensagem", target = "#main", swap = "outerHTML")
    @GetMapping("/vendas/remover/{codigo}")
    public String remover(@PathVariable("codigo") Long codigo, RedirectAttributes attributes) {
        vendaService.remover(codigo);
        attributes.addFlashAttribute("notificacao",
                new NotificacaoSweetAlert2("Venda removida com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
        return "redirect:/vendas/abrirpesquisa";
    }

    @HxRequest
    @GetMapping("/vendas/alterar/{codigo}")
    public String abrirAlterar(@PathVariable("codigo") Long codigo, Model model, HttpSession sessao) {
        Venda venda = vendaRepository.buscarCompletoCodigo(codigo);
        if (venda != null) {
            sessao.setAttribute("venda", venda);
            model.addAttribute("venda", venda);
            return "vendas/alterar :: formulario";
        } else {
            model.addAttribute("mensagem", "Não existe uma venda com esse código");
            return "mensagem :: texto";
        }
    }

    @HxRequest
    @PostMapping("vendas/alterar")
    public String alterar(@Valid Venda venda, BindingResult resultado,
            RedirectAttributes redirectAttributes, HttpSession sessao) {
        if (resultado.hasErrors()) {
            logger.info("A venda recebida para alterar não é válida.");
            logger.info("Erros encontrados:");
            for (FieldError erro : resultado.getFieldErrors()) {
                logger.info("{}", erro);
            }
            for (ObjectError erro : resultado.getGlobalErrors()) {
                logger.info("{}", erro);
            }
            return "vendas/alterar :: formulario";
        } else {
            Venda salva = (Venda) sessao.getAttribute("venda");
            salva.setData(venda.getData());
            vendaService.alterar(salva);
            sessao.removeAttribute("venda");

            redirectAttributes.addFlashAttribute("notificacao",
                    new NotificacaoSweetAlert2("Venda alterada com sucesso!",
                            TipoNotificaoSweetAlert2.SUCCESS, 4000));

            return "redirect:/vendas/abrirpesquisa";
        }
    }

}
