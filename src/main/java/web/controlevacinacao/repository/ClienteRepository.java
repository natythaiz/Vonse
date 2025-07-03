package web.controlevacinacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web.controlevacinacao.model.Cliente;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.repository.queries.cliente.ClienteQueries;

public interface ClienteRepository extends JpaRepository<Cliente, Long>, ClienteQueries {

    Cliente findByIdAndStatus(Long id, Status status);

}
