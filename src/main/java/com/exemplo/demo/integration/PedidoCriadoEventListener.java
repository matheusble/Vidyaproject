package com.exemplo.demo.integration;

import com.exemplo.demo.domain.ItemPedido;
import com.exemplo.demo.domain.Pedido;
import com.exemplo.demo.repository.PedidoRepository;
import com.exemplo.demo.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PedidoCriadoEventListener {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onPedidoCriado(PedidoCriadoEvent event) {
        Long pedidoId = event.getPedidoId();
        Optional<Pedido> opt = pedidoRepository.findByIdWithItensAndProdutos(pedidoId);
        if (opt.isEmpty()) {
            return;
        }
        Pedido pedido = opt.get();
        for (ItemPedido item : pedido.getItens()) {
            var produto = item.getProduto();
            int novoEstoque = produto.getEstoque() - item.getQuantidade();
            produto.setEstoque(Math.max(0, novoEstoque));
            produtoRepository.save(produto);
        }
        log.info("Estoque atualizado para pedido id={}", pedidoId);
        simularNotificacao(pedidoId);
    }

    private void simularNotificacao(Long pedidoId) {
        log.info("[Notificação simulada] Pedido {} criado. Webhook/email seria enviado aqui.", pedidoId);
    }
}
