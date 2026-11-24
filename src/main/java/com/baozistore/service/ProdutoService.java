package com.baozistore.service;

import com.baozistore.domain.Produto;
import com.baozistore.exception.RecursoNaoEncontradoException;
import com.baozistore.exception.RegraNegocioException;
import org.springframework.stereotype.Service;
import com.baozistore.repository.PedidoRepository;
import com.baozistore.repository.ProdutoRepository;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final PedidoRepository pedidoRepository;

    public ProdutoService(ProdutoRepository produtoRepository, PedidoRepository pedidoRepository) {
        this.produtoRepository = produtoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public Produto criar(Produto produto) {
        produto.setId(null);
        return produtoRepository.save(produto);
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id).orElseThrow(() ->
                new RecursoNaoEncontradoException("Produto não encontrado."));
    }

    public List<Produto> listar() {
        return produtoRepository.findAll();
    }

    public void apagar(Long id){
        buscarPorId(id);
        if (pedidoRepository.existsById(id)){
            throw new RegraNegocioException("Não é possível apagar produtos que possuem pedidos.");
        }
        produtoRepository.deleteById(id);
    }
}
