package web.controlevacinacao.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.Venda;
import web.controlevacinacao.repository.VendaRepository;

@Service
@Transactional
public class VendaService {

    private final VendaRepository vendaRepository;

    public VendaService(VendaRepository vendaRepository) {
        this.vendaRepository = vendaRepository;
    }

    public void salvar(Venda venda) {
        vendaRepository.save(venda);
    }

    public void alterar(Venda venda) {
        vendaRepository.save(venda);
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
