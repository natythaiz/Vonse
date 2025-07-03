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
import web.controlevacinacao.filter.ClienteFilter;
import web.controlevacinacao.model.Cliente;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.notificacao.NotificacaoSweetAlert2;
import web.controlevacinacao.notificacao.TipoNotificaoSweetAlert2;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.ClienteRepository;
import web.controlevacinacao.service.ClienteService;

@Controller
public class ClienteController {
    //TA AQUI
    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    private final ClienteRepository clienteRepository;
    private final ClienteService clienteService;

    public ClienteController(ClienteRepository clienteRepository, ClienteService clienteService) {
        this.clienteRepository = clienteRepository;
        this.clienteService = clienteService;
    }

    @HxRequest
    @GetMapping("/clientes/abrirpesquisa")
    public String abrirPesquisaHTMX() {
        return "clientes/pesquisar :: formulario";
    }

    @HxRequest
    @GetMapping("/clientes/pesquisar")
    public String mostrarClientesPesquisaHTMX(ClienteFilter filtro, Model model,
            @PageableDefault(size = 8) @SortDefault(sort = "nome", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Cliente> pagina = clienteRepository.pesquisar(filtro, pageable);
        logger.info("Clientes pesquisados: {}", pagina);
        PageWrapper<Cliente> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "clientes/listar :: tabela";
    }

    @HxRequest
    @GetMapping("/clientes/cadastrar")
    public String abrirCadastroHTMX(Cliente cliente) {
        return "clientes/cadastrar :: formulario";
    }

    @HxRequest
    @PostMapping("/clientes/cadastrar")
    public String cadastrarHTMX(@Valid Cliente cliente,
                                 BindingResult resultado,
                                 RedirectAttributes attributes) {
        if (resultado.hasErrors()) {
            logger.info("O cliente recebido para cadastrar não é válido.");
            for (FieldError erro : resultado.getFieldErrors()) {
                logger.info("{}", erro);
            }
            for (ObjectError erro : resultado.getGlobalErrors()) {
                logger.info("{}", erro);
            }
            return "clientes/cadastrar :: formulario";
        } else {
            clienteService.salvar(cliente);
            attributes.addFlashAttribute("notificacao",
                    new NotificacaoSweetAlert2("Cliente cadastrado com sucesso!",
                            TipoNotificaoSweetAlert2.SUCCESS, 4000));
            return "redirect:/clientes/cadastrar";
        }
    }

    @HxRequest
    @GetMapping("/clientes/alterar/{id}")
    public String abrirAlterarHTMX(@PathVariable("id") Long id, Model model) {
        Cliente cliente = clienteRepository.findByIdAndStatus(id, Status.ATIVO);
        if (cliente != null) {
            model.addAttribute("cliente", cliente);
            return "clientes/alterar :: formulario";
        } else {
            model.addAttribute("mensagem", "Não existe um cliente com este ID.");
            return "mensagem :: texto";
        }
    }

    @HxRequest
    @PostMapping("/clientes/alterar")
    public String alterarHTMX(@Valid Cliente cliente, BindingResult resultado,
                               RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            logger.info("O cliente recebido para alterar não é válido.");
            for (FieldError erro : resultado.getFieldErrors()) {
                logger.info("{}", erro);
            }
            for (ObjectError erro : resultado.getGlobalErrors()) {
                logger.info("{}", erro);
            }
            return "clientes/alterar :: formulario";
        } else {
            clienteService.alterar(cliente);
            redirectAttributes.addFlashAttribute("notificacao",
                    new NotificacaoSweetAlert2("Cliente alterado com sucesso!",
                            TipoNotificaoSweetAlert2.SUCCESS, 4000));
            return "redirect:/clientes/abrirpesquisa";
        }
    }

    @HxRequest
    @HxLocation(path = "/mensagem", target = "#main", swap = "outerHTML")
    @GetMapping("/clientes/remover/{id}")
    public String removerHTMX(@PathVariable("id") Long id, RedirectAttributes attributes) {
        clienteService.remover(id);
        attributes.addFlashAttribute("notificacao",
                new NotificacaoSweetAlert2("Cliente removido com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
        return "redirect:/clientes/abrirpesquisa";
    }
}
