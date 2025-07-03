package web.controlevacinacao.repository.queries.produto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.controlevacinacao.filter.ProdutoFilter;
import web.controlevacinacao.model.Produto;

public interface ProdutoQueries {

	Page<Produto> pesquisar(ProdutoFilter filtro, Pageable pageable);
	
}
