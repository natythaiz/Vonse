package web.controlevacinacao.controller;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import web.controlevacinacao.filter.LoteFilter;
import web.controlevacinacao.filter.PessoaFilter;
import web.controlevacinacao.model.Aplicacao;
import web.controlevacinacao.model.Lote;
import web.controlevacinacao.model.Pessoa;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.LoteRepository;
import web.controlevacinacao.repository.PessoaRepository;

@Controller
public class AplicacaoController {

    private static final Logger logger = LoggerFactory.getLogger(AplicacaoController.class);
    private PessoaRepository pessoaRepository;
    private LoteRepository loteRepository;

    public AplicacaoController(PessoaRepository pessoaRepository,
            LoteRepository loteRepository) {
        this.pessoaRepository = pessoaRepository;
        this.loteRepository = loteRepository;
    }

    @HxRequest
    @GetMapping("/aplicacoes/cadastrar")
    public String abrirEscolhaPessoa() {
        return "aplicacoes/pesquisapessoa :: formulario";
    }

    @HxRequest
    @GetMapping("/aplicacoes/pesquisarpessoa")
    public String mostrarPessoasPesquisa(PessoaFilter filtro, Model model,
            @PageableDefault(size = 8) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Pessoa> pagina = pessoaRepository.pesquisar(filtro, pageable);
        logger.info("Pessoas pesquisadas: {}", pagina);
        PageWrapper<Pessoa> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "aplicacoes/escolherpessoa :: tabela";
    }

    @HxRequest
    @GetMapping("/aplicacoes/pessoa/{codigo}")
    public String abrirEscolhaLote(@PathVariable("codigo") Long codigo,
            Model model, HttpSession sessao) {
        Pessoa pessoa = pessoaRepository.findByCodigoAndStatus(codigo, Status.ATIVO);
        if (pessoa != null) {
            Aplicacao aplicacao = new Aplicacao();
            aplicacao.setPessoa(pessoa);
            sessao.setAttribute("aplicacao", aplicacao);
            return "aplicacoes/pesquisalote :: formulario";
        } else {
            model.addAttribute("mensagem", "Não existe uma vacina com esse código");
            return "mensagem :: texto";
        }
    }

    @HxRequest
    @GetMapping("/aplicacoes/pesquisarlote")
    public String mostrarLotesPesquisa(LoteFilter filtro, Model model,
            @PageableDefault(size = 8) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Lote> pagina = loteRepository.pesquisar(filtro, pageable);
        logger.info("Lotes pesquisados: {}", pagina);
        PageWrapper<Lote> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "aplicacoes/escolherlote :: tabela";
    }

    @HxRequest
    @GetMapping("/aplicacoes/lote/{codigo}")
    public String abrirCadastro(@PathVariable("codigo") Long codigo,
            Model model, HttpSession sessao) {
        Lote lote = loteRepository.findByCodigoAndStatus(codigo, Status.ATIVO);
        if (lote != null) {
            Aplicacao aplicacao = (Aplicacao) sessao.getAttribute("aplicacao");
            aplicacao.setLote(lote);
            aplicacao.setData(LocalDate.now());
            sessao.setAttribute("aplicacao", aplicacao);
            model.addAttribute("aplicacao", aplicacao);
            return "aplicacoes/cadastrar :: formulario";
        } else {
            model.addAttribute("mensagem", "Não existe um lote com esse código");
            return "mensagem :: texto";
        }
    }

    // /aplicacoes/cadastrar
}
