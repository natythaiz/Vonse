package web.controlevacinacao.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.controlevacinacao.model.Lote;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.repository.LoteRepository;

@Service
@Transactional
public class LoteService {

    private LoteRepository loteRepository;

    public LoteService(LoteRepository loteRepository) {
        this.loteRepository = loteRepository;
    }

    public void salvar(Lote lote) {
        loteRepository.save(lote);
    }

    public void alterar(Lote lote) {
        loteRepository.save(lote);
    }

    public void remover(Long codigo) {
        Lote lote = loteRepository.findByCodigoAndStatus(codigo, Status.ATIVO);
        if (lote == null) {
            throw new RuntimeException("Remoção de Lote com codigo inválido");
        } else {
            lote.setStatus(Status.INATIVO);
            loteRepository.save(lote);
        }
    }

}
