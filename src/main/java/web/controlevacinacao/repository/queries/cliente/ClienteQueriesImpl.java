package web.controlevacinacao.repository.queries.cliente;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import web.controlevacinacao.filter.ClienteFilter;
import web.controlevacinacao.model.Cliente;
import web.controlevacinacao.pagination.PaginacaoUtil;

public class ClienteQueriesImpl implements ClienteQueries {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Cliente> pesquisar(ClienteFilter filtro, Pageable pageable) {

        StringBuilder queryClientes = new StringBuilder("select distinct c from Cliente c");
        StringBuilder condicoes = new StringBuilder();
        Map<String, Object> parametros = new HashMap<>();

        preencherCondicoesEParametros(filtro, condicoes, parametros);

        if (condicoes.isEmpty()) {
            condicoes.append(" where c.status = 'ATIVO'");
        } else {
            condicoes.append(" and c.status = 'ATIVO'");
        }

        queryClientes.append(condicoes);
        PaginacaoUtil.prepararOrdemJPQL(queryClientes, "c", pageable);

        TypedQuery<Cliente> typedQuery = em.createQuery(queryClientes.toString(), Cliente.class);
        PaginacaoUtil.prepararIntervalo(typedQuery, pageable);
        PaginacaoUtil.preencherParametros(parametros, typedQuery);

        List<Cliente> clientes = typedQuery.getResultList();
        long totalClientes = PaginacaoUtil.getTotalRegistros("Cliente", "c", condicoes, parametros, em);

        return new PageImpl<>(clientes, pageable, totalClientes);
    }

    private void preencherCondicoesEParametros(ClienteFilter filtro, StringBuilder condicoes, Map<String, Object> parametros) {
        boolean condicao = false;

        if (filtro.getId() != null) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("c.id = :id");
            parametros.put("id", filtro.getId());
            condicao = true;
        }

        if (StringUtils.hasText(filtro.getNome())) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("lower(c.nome) like :nome");
            parametros.put("nome", "%" + filtro.getNome().toLowerCase() + "%");
            condicao = true;
        }

        if (StringUtils.hasText(filtro.getContato())) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("lower(c.contato) like :contato");
            parametros.put("contato", "%" + filtro.getContato().toLowerCase() + "%");
            condicao = true;
        }

        if (filtro.getMinDataCadastro() != null) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("c.dataCadastro >= :minDataCadastro");
            parametros.put("minDataCadastro", filtro.getMinDataCadastro());
            condicao = true;
        }

        if (filtro.getMaxDataCadastro() != null) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("c.dataCadastro <= :maxDataCadastro");
            parametros.put("maxDataCadastro", filtro.getMaxDataCadastro());
            condicao = true;
        }
    }
}
