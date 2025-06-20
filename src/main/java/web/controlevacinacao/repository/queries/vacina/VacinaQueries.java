package web.controlevacinacao.repository.queries.vacina;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.controlevacinacao.filter.VacinaFilter;
import web.controlevacinacao.model.Vacina;

public interface VacinaQueries {

	Page<Vacina> pesquisar(VacinaFilter filtro, Pageable pageable);
	
}
