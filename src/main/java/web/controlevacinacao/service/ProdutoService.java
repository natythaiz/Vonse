package web.controlevacinacao.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.controlevacinacao.model.Produto;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.repository.ProdutoRepository;

@Service
@Transactional
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private static final double MARGEM_PADRAO = 1.7;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public void salvar(Produto produto) {
        produto.setPrecoVenda(produto.getPrecoCusto() * MARGEM_PADRAO);
        produtoRepository.save(produto);
    }

    public void alterar(Produto produto) {
        produto.setPrecoVenda(produto.getPrecoCusto() * MARGEM_PADRAO);
        produtoRepository.save(produto);
    }

    public void remover(Long id) {
        Produto produto = produtoRepository.findByIdAndStatus(id, Status.ATIVO);
        if (produto == null) {
            throw new RuntimeException("Remoção de Produto com ID inválido.");
        } else {
            produto.setStatus(Status.INATIVO);
            produtoRepository.save(produto);
        }
    }
}
