package web.controlevacinacao.repository.queries.pessoa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.controlevacinacao.filter.PessoaFilter;
import web.controlevacinacao.model.Pessoa;

public interface PessoaQueries {

	Page<Pessoa> pesquisar(PessoaFilter filtro, Pageable pageable);
	
}
