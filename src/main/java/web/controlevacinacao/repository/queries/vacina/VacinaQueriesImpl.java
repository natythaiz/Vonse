package web.controlevacinacao.repository.queries.vacina;

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
import web.controlevacinacao.filter.VacinaFilter;
import web.controlevacinacao.model.Vacina;
import web.controlevacinacao.pagination.PaginacaoUtil;

public class VacinaQueriesImpl implements VacinaQueries {

	@PersistenceContext
	private EntityManager em;

	public Page<Vacina> pesquisar(VacinaFilter filtro, Pageable pageable) {

		StringBuilder queryVacinas = new StringBuilder("select distinct v from Vacina v");
		StringBuilder condicoes = new StringBuilder();
		Map<String, Object> parametros = new HashMap<>();

		preencherCondicoesEParametros(filtro, condicoes, parametros);

		if (condicoes.isEmpty()) {
			condicoes.append(" where v.status = 'ATIVO'");
		} else {
			condicoes.append(" and v.status = 'ATIVO'");
		}

		queryVacinas.append(condicoes);
		PaginacaoUtil.prepararOrdemJPQL(queryVacinas, "v", pageable);
		TypedQuery<Vacina> typedQuery = em.createQuery(queryVacinas.toString(), Vacina.class);
		PaginacaoUtil.prepararIntervalo(typedQuery, pageable);
		PaginacaoUtil.preencherParametros(parametros, typedQuery);
		List<Vacina> vacinas = typedQuery.getResultList();

		long totalVacinas = PaginacaoUtil.getTotalRegistros("Vacina", "v", condicoes, parametros, em);

		return new PageImpl<>(vacinas, pageable, totalVacinas);
	}

	private void preencherCondicoesEParametros(VacinaFilter filtro, StringBuilder condicoes, Map<String, Object> parametros) {
		boolean condicao = false;

		if (filtro.getCodigo() != null) {
			PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
			condicoes.append("v.codigo = :codigo");
			parametros.put("codigo", filtro.getCodigo());
			condicao = true;
		}
		if (StringUtils.hasText(filtro.getNome())) {
			PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);		
			condicoes.append("lower(v.nome) like :nome");
			parametros.put("nome", "%" + filtro.getNome().toLowerCase() + "%");
			condicao = true;
		}
		if (StringUtils.hasText(filtro.getDescricao())) {
			PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
			condicoes.append("lower(v.descricao) like :descricao");
			parametros.put("descricao", "%" + filtro.getDescricao().toLowerCase() + "%");
		}
	}
	
}
