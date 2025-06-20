package web.controlevacinacao.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.controlevacinacao.model.Pessoa;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.repository.PessoaRepository;

@Service
@Transactional
public class PessoaService {

    private PessoaRepository pessoaRepository;

    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    public void salvar(Pessoa pessoa) {
        pessoaRepository.save(pessoa);
    }

    public void alterar(Pessoa pessoa) {
        pessoaRepository.save(pessoa);
    }

    public void remover(Long codigo) {
        Pessoa pessoa = pessoaRepository.findByCodigoAndStatus(codigo, Status.ATIVO);
        if (pessoa == null) {
            throw new RuntimeException("Remoção de Pessoa com codigo inválido");
        } else {
            pessoa.setStatus(Status.INATIVO);
            pessoaRepository.save(pessoa);
        }
    }

}
