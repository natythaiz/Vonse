package web.controlevacinacao.repository.queries.lote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import web.controlevacinacao.filter.LoteFilter;
import web.controlevacinacao.model.Lote;
import web.controlevacinacao.pagination.PaginacaoUtil;

public class LoteQueriesImpl implements LoteQueries {

	@PersistenceContext
	private EntityManager em;

	@Override
	public Page<Lote> pesquisar(LoteFilter filtro, Pageable pageable) {

		StringBuilder queryLotes = new StringBuilder("select distinct l from Lote l inner join fetch l.vacina");
		StringBuilder condicoes = new StringBuilder();
		Map<String, Object> parametros = new HashMap<>();

		preencherCondicoesEParametros(filtro, condicoes, parametros);

		if (condicoes.isEmpty()) {
			condicoes.append(" where l.status = 'ATIVO'");
		} else {
			condicoes.append(" and l.status = 'ATIVO'");
		}

		queryLotes.append(condicoes);
		PaginacaoUtil.prepararOrdemJPQL(queryLotes, "l", pageable);
		TypedQuery<Lote> typedQuery = em.createQuery(queryLotes.toString(), Lote.class);
		PaginacaoUtil.prepararIntervalo(typedQuery, pageable);
		PaginacaoUtil.preencherParametros(parametros, typedQuery);
		List<Lote> lotes = typedQuery.getResultList();

		long totalLotes = PaginacaoUtil.getTotalRegistros("Lote", "l", condicoes, parametros, em);

		return new PageImpl<>(lotes, pageable, totalLotes);
	}

	private void preencherCondicoesEParametros(LoteFilter filtro, StringBuilder condicoes, Map<String, Object> parametros) {
		boolean condicao = false;

		if (filtro.getCodigo() != null) {
			PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
			condicoes.append("l.codigo = :codigo");
			parametros.put("codigo", filtro.getCodigo());
			condicao = true;
		}
		if (filtro.getInicioValidade() != null) {
			PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
			condicoes.append("l.validade >= :inicioValidade");
			parametros.put("inicioValidade", filtro.getInicioValidade());
			condicao = true;
		}
		if (filtro.getFimValidade() != null) {
			PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
			condicoes.append("l.validade <= :fimValidade");
			parametros.put("fimValidade", filtro.getFimValidade());
			condicao = true;
		}
		if (filtro.getMinimoDosesLote() != null) {
			PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
			condicoes.append("l.nroDosesDoLote >= :minimoDosesLote");
			parametros.put("minimoDosesLote", filtro.getMinimoDosesLote());
			condicao = true;
		}
		if (filtro.getMaximoDosesLote() != null) {
			PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
			condicoes.append("l.nroDosesDoLote <= :maximoDosesLote");
			parametros.put("maximoDosesLote", filtro.getMaximoDosesLote());
			condicao = true;
		}
		if (filtro.getMinimoDosesAtual() != null) {
			PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
			condicoes.append("l.nroDosesAtual >= :minimoDosesAtual");
			parametros.put("minimoDosesAtual", filtro.getMinimoDosesAtual());
			condicao = true;
		}
		if (filtro.getMaximoDosesAtual() != null) {
			PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
			condicoes.append("l.nroDosesAtual <= :maximoDosesAtual");
			parametros.put("maximoDosesAtual", filtro.getMaximoDosesAtual());
			condicao = true;
		}
		if (StringUtils.hasText(filtro.getNomeVacina())) {
			PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
			condicoes.append("lower(l.vacina.nome) like :nomeVacina");
			parametros.put("nomeVacina", "%" + filtro.getNomeVacina().toLowerCase() + "%");
		}
	}

}
