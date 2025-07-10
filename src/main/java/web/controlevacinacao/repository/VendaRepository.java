package web.controlevacinacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import web.controlevacinacao.model.Venda;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.repository.queries.venda.VendaQueries;

public interface VendaRepository extends JpaRepository<Venda, Long>, VendaQueries {

    @Query("SELECT COALESCE(SUM(v.valorTotal), 0) FROM Venda v WHERE v.status = 'ATIVO' AND MONTH(v.data) = :mes AND YEAR(v.data) = :ano")
    Double buscarValorTotalVendidoMesAtual(@Param("mes") int mes, @Param("ano") int ano);

    Venda findByIdAndStatus(Long id, Status status);

}
