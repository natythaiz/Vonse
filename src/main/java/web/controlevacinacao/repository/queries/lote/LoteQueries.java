package web.controlevacinacao.repository.queries.lote;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.controlevacinacao.filter.LoteFilter;
import web.controlevacinacao.model.Lote;

public interface LoteQueries {

	Page<Lote> pesquisar(LoteFilter filtro, Pageable pageable);
	
}
