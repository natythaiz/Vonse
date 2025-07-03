package web.controlevacinacao.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.controlevacinacao.model.Cliente;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.repository.ClienteRepository;

@Service
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public void salvar(Cliente cliente) {
        clienteRepository.save(cliente);
    }

    public void alterar(Cliente cliente) {
        clienteRepository.save(cliente);
    }

    public void remover(Long id) {
        Cliente cliente = clienteRepository.findByIdAndStatus(id, Status.ATIVO);
        if (cliente == null) {
            throw new RuntimeException("Remoção de Cliente com ID inválido.");
        } else {
            cliente.setStatus(Status.INATIVO);
            clienteRepository.save(cliente);
        }
    }
}
