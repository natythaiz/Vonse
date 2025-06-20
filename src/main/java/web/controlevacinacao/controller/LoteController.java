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
import web.controlevacinacao.filter.LoteFilter;
import web.controlevacinacao.filter.VacinaFilter;
import web.controlevacinacao.model.Lote;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.Vacina;
import web.controlevacinacao.notificacao.NotificacaoSweetAlert2;
import web.controlevacinacao.notificacao.TipoNotificaoSweetAlert2;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.LoteRepository;
import web.controlevacinacao.repository.VacinaRepository;
import web.controlevacinacao.service.LoteService;

@Controller
public class LoteController {

    private static final Logger logger = LoggerFactory.getLogger(LoteController.class);
    private VacinaRepository vacinaRepository;
    private LoteService loteService;
    private LoteRepository loteRepository;

    public LoteController(VacinaRepository vacinaRepository, 
                          LoteService loteService,
                          LoteRepository loteRepository) {
        this.vacinaRepository = vacinaRepository;
        this.loteService = loteService;
        this.loteRepository = loteRepository;
    }

    @HxRequest
    @GetMapping("/lotes/cadastrar")
    public String abrirEscolhaVacina() {
        return "lotes/pesquisavacina :: formulario";
    }

    @HxRequest
    @GetMapping("/lotes/pesquisarvacina")
    public String mostrarVacinasPesquisa(VacinaFilter filtro, Model model,
            @PageableDefault(size = 8) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Vacina> pagina = vacinaRepository.pesquisar(filtro, pageable);
        logger.info("Vacinas pesquisadas: {}", pagina);
        PageWrapper<Vacina> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "lotes/escolhervacina :: tabela";
    }

    @HxRequest
    @GetMapping("/lotes/vacina/{codigo}")
    public String abrirCadastro(@PathVariable("codigo") Long codigo, Model model) {
        Vacina vacina = vacinaRepository.findByCodigoAndStatus(codigo, Status.ATIVO);
        if (vacina != null) {
            Lote lote = new Lote();
            lote.setVacina(vacina);
            model.addAttribute("lote", lote);
            return "lotes/cadastrar :: formulario";
        } else {
            model.addAttribute("mensagem", "Não existe uma vacina com esse código");
            return "mensagem :: texto";
        }
    }

    @HxRequest
    @PostMapping("/lotes/cadastrar")
    public String cadastrar(@Valid Lote lote,
            BindingResult resultado,
            RedirectAttributes attributes) {
        if (resultado.hasErrors()) {
            logger.info("O lote recebido para cadastrar não é válido.");
            logger.info("Erros encontrados:");
            for (FieldError erro : resultado.getFieldErrors()) {
                logger.info("{}", erro);
            }
            for (ObjectError erro : resultado.getGlobalErrors()) {
                logger.info("{}", erro);
            }
            return "lotes/cadastrar :: formulario";
        } else {
            loteService.salvar(lote);

            attributes.addFlashAttribute("notificacao",
                    new NotificacaoSweetAlert2("Lote cadastrado com sucesso!",
                            TipoNotificaoSweetAlert2.SUCCESS, 4000));

            return "redirect:/lotes/cadastrar";
        }
    }

    @HxRequest
    @GetMapping("/lotes/abrirpesquisa")
    public String abrirPesquisa() {
        return "lotes/pesquisar :: formulario";
    }

    @HxRequest
    @GetMapping("/lotes/pesquisar")
    public String mostrarLotesPesquisa(LoteFilter filtro, Model model,
            @PageableDefault(size = 8) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Lote> pagina = loteRepository.pesquisar(filtro, pageable);
        logger.info("Lotes pesquisados: {}", pagina);
        PageWrapper<Lote> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "lotes/listar :: tabela";
    }

    @HxRequest
    @HxLocation(path = "/mensagem", target = "#main", swap = "outerHTML")
    @GetMapping("/lotes/remover/{codigo}")
    public String remover(@PathVariable("codigo") Long codigo, RedirectAttributes attributes) {
        loteService.remover(codigo);
        attributes.addFlashAttribute("notificacao", new NotificacaoSweetAlert2("Lote removido com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
        return "redirect:/lotes/abrirpesquisa";
    }

    @HxRequest
    @GetMapping("/lotes/alterar/{codigo}")
    public String abrirAlterar(@PathVariable("codigo") Long codigo, Model model) {
        Lote lote = loteRepository.findByCodigoAndStatus(codigo, Status.ATIVO);
        if (lote != null) {
            model.addAttribute("lote", lote);
            return "lotes/alterar :: formulario";
        } else {
            model.addAttribute("mensagem", "Não existe uma vacina com esse código");
            return "mensagem :: texto";
        }
    }

    @HxRequest
    @PostMapping("/lotes/alterar")
    public String alterar(@Valid Lote lote, BindingResult resultado,
            RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            logger.info("O lote recebido para alterar não é válido.");
            logger.info("Erros encontrados:");
            for (FieldError erro : resultado.getFieldErrors()) {
                logger.info("{}", erro);
            }
            for (ObjectError erro : resultado.getGlobalErrors()) {
                logger.info("{}", erro);
            }
            return "lotes/alterar :: formulario";
        } else {
            loteService.alterar(lote);

            redirectAttributes.addFlashAttribute("notificacao",
                    new NotificacaoSweetAlert2("Lote alterado com sucesso!",
                            TipoNotificaoSweetAlert2.SUCCESS, 4000));

            return "redirect:/lotes/abrirpesquisa";
        }
    }
}
