package web.controlevacinacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web.controlevacinacao.model.Venda;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.repository.queries.venda.VendaQueries;

public interface VendaRepository extends JpaRepository<Venda, Long>, VendaQueries {

    Venda findByIdAndStatus(Long id, Status status);

}
