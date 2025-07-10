package web.controlevacinacao.repository.queries.venda;

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
import web.controlevacinacao.filter.VendaFilter;
import web.controlevacinacao.model.Venda;
import web.controlevacinacao.pagination.PaginacaoUtil;

public class VendaQueriesImpl implements VendaQueries {

    @PersistenceContext
    private EntityManager em;

    @Override
	public Venda buscarCompletoCodigo(Long id) {
        String query = "select distinct v from Venda v join fetch v.cliente join fetch v.itensVendidos i join fetch i.produto where v.status = 'ATIVO' and v.id = :id";
		TypedQuery<Venda> typedQuery = em.createQuery(query, Venda.class);
		typedQuery.setParameter("id", id);
		return typedQuery.getSingleResult();
	}

    @Override
    public Page<Venda> pesquisar(VendaFilter filtro, Pageable pageable) {

        StringBuilder queryVendas = new StringBuilder("select distinct v from Venda v join fetch v.cliente c join fetch v.itensVendidos i join fetch i.produto p");
        StringBuilder condicoes = new StringBuilder();
        Map<String, Object> parametros = new HashMap<>();

        preencherCondicoesEParametros(filtro, condicoes, parametros);

        if (condicoes.isEmpty()) {
            condicoes.append(" where v.status = 'ATIVO'");
        } else {
            condicoes.append(" and v.status = 'ATIVO'");
        }

        queryVendas.append(condicoes);
        PaginacaoUtil.prepararOrdemJPQL(queryVendas, "v", pageable);

        TypedQuery<Venda> typedQuery = em.createQuery(queryVendas.toString(), Venda.class);
        PaginacaoUtil.prepararIntervalo(typedQuery, pageable);
        PaginacaoUtil.preencherParametros(parametros, typedQuery);

        List<Venda> vendas = typedQuery.getResultList();
        long totalVendas = PaginacaoUtil.getTotalRegistros("Venda", "v", condicoes, parametros, em);

        return new PageImpl<>(vendas, pageable, totalVendas);
    }

    private void preencherCondicoesEParametros(VendaFilter filtro, StringBuilder condicoes, Map<String, Object> parametros) {
        boolean condicao = false;

        if (filtro.getId() != null) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("v.id = :id");
            parametros.put("id", filtro.getId());
            condicao = true;
        }

        if (filtro.getMinData() != null) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("v.data >= :minData");
            parametros.put("minData", filtro.getMinData());
            condicao = true;
        }

        if (filtro.getMaxData() != null) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("v.data <= :maxData");
            parametros.put("maxData", filtro.getMaxData());
            condicao = true;
        }

        if (StringUtils.hasText(filtro.getNomeCliente())) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("lower(v.cliente.nome) like :nomeCliente");
            parametros.put("nomeCliente", "%" + filtro.getNomeCliente().toLowerCase() + "%");
            condicao = true;
        }

        if (filtro.getMinValorTotal() != null) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("v.valorTotal >= :minValorTotal");
            parametros.put("minValorTotal", filtro.getMinValorTotal());
            condicao = true;
        }

        if (filtro.getMaxValorTotal() != null) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("v.valorTotal <= :maxValorTotal");
            parametros.put("maxValorTotal", filtro.getMaxValorTotal());
            condicao = true;
        }
    }
}
