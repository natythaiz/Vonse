package web.controlevacinacao.repository.queries.produto;

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
import web.controlevacinacao.filter.ProdutoFilter;
import web.controlevacinacao.model.Produto;
import web.controlevacinacao.pagination.PaginacaoUtil;

public class ProdutoQueriesImpl implements ProdutoQueries {

	@PersistenceContext
	private EntityManager em;

	public Page<Produto> pesquisar(ProdutoFilter filtro, Pageable pageable) {

		StringBuilder queryProdutos = new StringBuilder("select distinct p from Produto p");
		StringBuilder condicoes = new StringBuilder();
		Map<String, Object> parametros = new HashMap<>();

		preencherCondicoesEParametros(filtro, condicoes, parametros);

		if (condicoes.isEmpty()) {
			condicoes.append(" where p.status = 'ATIVO'");
		} else {
			condicoes.append(" and p.status = 'ATIVO'");
		}

		queryProdutos.append(condicoes);
		PaginacaoUtil.prepararOrdemJPQL(queryProdutos, "p", pageable);
		TypedQuery<Produto> typedQuery = em.createQuery(queryProdutos.toString(), Produto.class);
		PaginacaoUtil.prepararIntervalo(typedQuery, pageable);
		PaginacaoUtil.preencherParametros(parametros, typedQuery);
		List<Produto> produtos = typedQuery.getResultList();

		long totalProdutos = PaginacaoUtil.getTotalRegistros("Produto", "p", condicoes, parametros, em);

		return new PageImpl<>(produtos, pageable, totalProdutos);
	}

	private void preencherCondicoesEParametros(ProdutoFilter filtro, StringBuilder condicoes, Map<String, Object> parametros) {
		boolean condicao = false;

		if (filtro.getId() != null) {
			PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
			condicoes.append("p.id = :id");
			parametros.put("id", filtro.getId());
			condicao = true;
		}
		// private String nome;
		if (StringUtils.hasText(filtro.getNome())) {
			PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);		
			condicoes.append("lower(p.nome) like :nome");
			parametros.put("nome", "%" + filtro.getNome().toLowerCase() + "%");
			condicao = true;
		}

		//private Categoria categoria;
		if (filtro.getCategoria() != null) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("p.categoria = :categoria");
            parametros.put("categoria", filtro.getCategoria());
            condicao = true;
        }

		// private Tipo tipo;
		if (filtro.getTipo() != null) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("p.tipo = :tipo");
            parametros.put("tipo", filtro.getTipo());
            condicao = true;
        }

		// private Integer minimoCusto;
		if (filtro.getMinimoCusto() != null) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("p.precoCusto >= :minimoCusto");
            parametros.put("minimoCusto", filtro.getMinimoCusto());
            condicao = true;
        }

		// private Integer maximoCusto;
		if (filtro.getMaximoCusto() != null) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("p.precoCusto <= :maximoCusto");
            parametros.put("maximoCusto", filtro.getMaximoCusto());
            condicao = true;
        }

		// private Integer minimoPrecoVenda;
		if (filtro.getMinimoPrecoVenda() != null) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("p.precoVenda >= :minimoPrecoVenda");
            parametros.put("minimoPrecoVenda", filtro.getMinimoPrecoVenda());
            condicao = true;
        }

		// private Integer maximoPrecoVenda;
		if (filtro.getMaximoPrecoVenda() != null) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("p.precoVenda <= :maximoPrecoVenda");
            parametros.put("maximoPrecoVenda", filtro.getMaximoPrecoVenda());
            condicao = true;
        }

		// private Long estoque;
		if (filtro.getEstoque() != null) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("p.estoque = :estoque");
            parametros.put("estoque", filtro.getEstoque());
            condicao = true;
        }

		// private String fornecedor;
		if (StringUtils.hasText(filtro.getFornecedor())) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("lower(p.fornecedor) like :fornecedor");
            parametros.put("fornecedor", "%" + filtro.getFornecedor().toLowerCase() + "%");
            condicao = true;
        }
	}
}
