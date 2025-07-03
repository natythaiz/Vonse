package web.controlevacinacao.repository.queries.cliente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.controlevacinacao.filter.ClienteFilter;
import web.controlevacinacao.model.Cliente;

public interface ClienteQueries {

	Page<Cliente> pesquisar(ClienteFilter filtro, Pageable pageable);
	
}
