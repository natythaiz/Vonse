package web.controlevacinacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.Vacina;
import web.controlevacinacao.repository.queries.vacina.VacinaQueries;

public interface VacinaRepository extends JpaRepository<Vacina, Long>, VacinaQueries {

    Vacina findByCodigoAndStatus (Long codigo, Status status);

}
