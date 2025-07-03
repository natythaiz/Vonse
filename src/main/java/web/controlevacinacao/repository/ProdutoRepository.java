package web.controlevacinacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web.controlevacinacao.model.Produto;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.repository.queries.produto.ProdutoQueries;

public interface ProdutoRepository extends JpaRepository<Produto, Long>, ProdutoQueries {

    Produto findByIdAndStatus(Long id, Status status);

}
