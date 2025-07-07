package web.controlevacinacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.controlevacinacao.model.ItemVenda;
import web.controlevacinacao.model.Produto;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.Venda;
import web.controlevacinacao.repository.ProdutoRepository;
import web.controlevacinacao.repository.VendaRepository;

@Service
@Transactional
public class VendaService {

    private final VendaRepository vendaRepository;
    private ProdutoRepository produtoRepository;

    @Autowired
    public VendaService(VendaRepository vendaRepository, ProdutoRepository produtoRepository) {
        this.vendaRepository = vendaRepository;
        this.produtoRepository = produtoRepository;
    }

    public void salvar(Venda venda) {
        calcularValorTotal(venda);
        for (ItemVenda item : venda.getItensVendidos()) {
            Produto produto = produtoRepository.findById(item.getProduto().getId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            if (item.getQuantidade() > produto.getEstoque()) {
                throw new RuntimeException("Quantidade de " + produto.getNome() + " excede o estoque disponível.");
            }

            // Reduzir o estoque do produto
            produto.setEstoque(produto.getEstoque() - item.getQuantidade());
            produtoRepository.save(produto);
        }

        vendaRepository.save(venda);
    }

    public void alterar(Venda venda) {
        calcularValorTotal(venda);
        vendaRepository.save(venda);
    }

    private void calcularValorTotal(Venda venda) {
        double total = venda.getItensVendidos().stream()
                .mapToDouble(ItemVenda::getSubTotal)
                .sum();
        venda.setValorTotal(total);
    }

    public void remover(Long id) {
        Venda venda = vendaRepository.findByIdAndStatus(id, Status.ATIVO);
        if (venda == null) {
            throw new RuntimeException("Remoção de Venda com ID inválido.");
        } else {
            venda.setStatus(Status.INATIVO);
            vendaRepository.save(venda);
        }
    }
}
