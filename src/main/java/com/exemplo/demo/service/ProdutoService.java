package com.exemplo.demo.service;

import com.exemplo.demo.domain.Produto;
import com.exemplo.demo.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    @Transactional(readOnly = true)
    public List<Produto> listar() {
        return produtoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Produto> listarComEstoqueAbaixoDe(Integer limite) {
        return produtoRepository.findByEstoqueLessThan(limite);
    }

    @Transactional
    public Produto criar(Produto produto) {
        return produtoRepository.save(produto);
    }

    @Transactional
    public Optional<Produto> atualizar(Long id, Produto dados) {
        return produtoRepository.findById(id)
                .map(existente -> {
                    existente.setNome(dados.getNome());
                    existente.setDescricao(dados.getDescricao());
                    existente.setPreco(dados.getPreco());
                    if (dados.getEstoque() != null) {
                        existente.setEstoque(dados.getEstoque());
                    }
                    return produtoRepository.save(existente);
                });
    }

    @Transactional
    public boolean excluir(Long id) {
        return produtoRepository.findById(id)
                .map(p -> {
                    produtoRepository.delete(p);
                    return true;
                })
                .orElse(false);
    }
}
