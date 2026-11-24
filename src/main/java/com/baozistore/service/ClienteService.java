package com.baozistore.service;

import com.baozistore.domain.Cliente;
import com.baozistore.exception.RecursoNaoEncontradoException;
import com.baozistore.exception.RegraNegocioException;
import org.springframework.stereotype.Service;
import com.baozistore.repository.ClienteRepository;
import com.baozistore.repository.PedidoRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PedidoRepository pedidoRepository;

    public ClienteService(ClienteRepository clienteRepository, PedidoRepository pedidoRepository) {
        this.clienteRepository = clienteRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public Cliente criar(Cliente cliente) {
        cliente.setId(null);
        if (cliente.getClienteDesde() == null){
            cliente.setClienteDesde(LocalDate.now());
        }
        return  clienteRepository.save(cliente);
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id).orElseThrow(() ->
                new RecursoNaoEncontradoException("Cliente não encontrado"));
    }

    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    public void apagar(Long id){
        buscarPorId(id);
        if (pedidoRepository.existsById(id)){
            throw new RegraNegocioException("Não é possível apagar cliente com pedidos a serem entregues.");
        }
        clienteRepository.deleteById(id);
    }
}
