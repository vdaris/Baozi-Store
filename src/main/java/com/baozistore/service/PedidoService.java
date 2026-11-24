package com.baozistore.service;

import com.baozistore.domain.Pedido;
import com.baozistore.domain.Produto;
import com.baozistore.exception.RecursoNaoEncontradoException;
import com.baozistore.exception.RegraNegocioException;
import org.springframework.stereotype.Service;
import com.baozistore.repository.PedidoRepository;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteService clienteService;
    private final ProdutoService produtoService;

    public PedidoService(PedidoRepository pedidoRepository,
                         ClienteService clienteService,
                         ProdutoService produtoService) {
        this.pedidoRepository = pedidoRepository;
        this.clienteService = clienteService;
        this.produtoService = produtoService;
    }

    public Pedido criar (Pedido pedido) {
        clienteService.buscarPorId(pedido.getClienteId());
        Produto produto = produtoService.buscarPorId(pedido.getProdutoId());
        if (!Boolean.TRUE.equals(produto.getEstoque())){
            throw new RegraNegocioException("Produto sem estoque." + produto.getNome());
        }
        pedido.setId(null);
        return pedidoRepository.save(pedido);
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id).orElseThrow(() ->
                new RecursoNaoEncontradoException("Pedido não encontrado."));
    }

    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    public void deletar(Long id) {
        buscarPorId(id);
        pedidoRepository.deleteById(id);
    }
}
