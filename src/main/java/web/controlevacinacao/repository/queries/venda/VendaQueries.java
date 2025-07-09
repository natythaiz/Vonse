package web.controlevacinacao.repository.queries.venda;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.controlevacinacao.filter.VendaFilter;
import web.controlevacinacao.model.Venda;

public interface VendaQueries {

	Page<Venda> pesquisar(VendaFilter filtro, Pageable pageable);
	
	Venda buscarCompletoCodigo(Long codigo);
}
