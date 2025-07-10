package web.controlevacinacao.controller;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Controller
public class IndexController {
    @PersistenceContext
    private EntityManager em;

    @GetMapping(value = { "/", "/index.html" })
    public String index(Model model) {
        String mesAtual = LocalDate.now()
                .getMonth()
                .getDisplayName(TextStyle.FULL, new Locale("pt", "BR"))
                .toUpperCase();

        // Consulta do valor total vendido no mês atual
        String jpql = "SELECT COALESCE(SUM(v.valorTotal), 0) FROM Venda v WHERE v.status = 'ATIVO' AND MONTH(v.data) = :mes AND YEAR(v.data) = :ano";
        TypedQuery<Double> query = em.createQuery(jpql, Double.class);
        query.setParameter("mes", LocalDate.now().getMonthValue());
        query.setParameter("ano", LocalDate.now().getYear());
        Double valorTotalMesAtual = query.getSingleResult();

        // Quantidade de clientes cadastrados no mês atual
        String jpqlClientes = "SELECT COUNT(c) FROM Cliente c WHERE c.status = 'ATIVO' AND MONTH(c.dataCadastro) = :mes AND YEAR(c.dataCadastro) = :ano";
        TypedQuery<Long> queryClientes = em.createQuery(jpqlClientes, Long.class);
        queryClientes.setParameter("mes", LocalDate.now().getMonthValue());
        queryClientes.setParameter("ano", LocalDate.now().getYear());
        Long quantidadeClientesMesAtual = queryClientes.getSingleResult();

        // Quantidade de produtos com estoque baixo (< 2)
        String jpqlProdutosBaixoEstoque = "SELECT COUNT(p) FROM Produto p WHERE p.status = 'ATIVO' AND p.estoque < 2";
        TypedQuery<Long> queryProdutosBaixoEstoque = em.createQuery(jpqlProdutosBaixoEstoque, Long.class);
        Long quantidadeProdutosBaixoEstoque = queryProdutosBaixoEstoque.getSingleResult();

        model.addAttribute("mesAtual", mesAtual);
        model.addAttribute("valorTotalMesAtual", valorTotalMesAtual);
        model.addAttribute("quantidadeClientesMesAtual", quantidadeClientesMesAtual);
        model.addAttribute("quantidadeProdutosBaixoEstoque", quantidadeProdutosBaixoEstoque);

        return "index";
    }

}
