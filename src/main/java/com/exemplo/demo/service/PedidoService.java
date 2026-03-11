package com.exemplo.demo.service;

import com.exemplo.demo.domain.*;
import com.exemplo.demo.integration.PedidoCriadoEvent;
import com.exemplo.demo.repository.ClienteRepository;
import com.exemplo.demo.repository.PedidoRepository;
import com.exemplo.demo.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Pedido> buscarPorIdComItens(Long id) {
        return pedidoRepository.findByIdWithItensAndProdutos(id);
    }

    @Transactional
    public Pedido criar(Long clienteId, List<ItemPedidoCriar> itens) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + clienteId));
        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .status(StatusPedido.ABERTO)
                .build();
        BigDecimal total = BigDecimal.ZERO;
        for (ItemPedidoCriar dto : itens) {
            Produto produto = produtoRepository.findById(dto.getProdutoId())
                    .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + dto.getProdutoId()));
            if (produto.getEstoque() < dto.getQuantidade()) {
                throw new IllegalArgumentException(
                        "Estoque insuficiente para produto " + produto.getNome() + ": disponível " + produto.getEstoque());
            }
            ItemPedido item = ItemPedido.builder()
                    .produto(produto)
                    .quantidade(dto.getQuantidade())
                    .precoUnitario(produto.getPreco())
                    .build();
            pedido.adicionarItem(item);
            total = total.add(item.getSubtotal());
        }
        pedido.setTotal(total);
        pedido = pedidoRepository.save(pedido);
        eventPublisher.publishEvent(new PedidoCriadoEvent(this, pedido.getId()));
        return pedido;
    }

    @Transactional
    public Optional<Pedido> atualizarStatus(Long id, StatusPedido novoStatus) {
        return pedidoRepository.findById(id)
                .map(pedido -> {
                    pedido.setStatus(novoStatus);
                    return pedidoRepository.save(pedido);
                });
    }

    @Transactional
    public boolean excluir(Long id) {
        return pedidoRepository.findById(id)
                .map(p -> {
                    pedidoRepository.delete(p);
                    return true;
                })
                .orElse(false);
    }

    /** DTO interno para criação de itens do pedido */
    @lombok.Data
    public static class ItemPedidoCriar {
        private Long produtoId;
        private Integer quantidade;
    }
}
