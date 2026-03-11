package com.exemplo.demo.scheduler;

import com.exemplo.demo.domain.Pedido;
import com.exemplo.demo.domain.StatusPedido;
import com.exemplo.demo.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FechamentoPedidosScheduler {

    private final PedidoRepository pedidoRepository;

    @Value("${app.scheduler.pedidos-antigos-dias:30}")
    private int pedidosAntigosDias;

    @Scheduled(cron = "${app.scheduler.fechamento-pedidos-cron}")
    @Transactional
    public void fecharPedidosAntigos() {
        Instant dataLimite = Instant.now().minus(pedidosAntigosDias, ChronoUnit.DAYS);
        List<Pedido> pedidos = pedidoRepository.findByStatusAndDataPedidoBefore(StatusPedido.ABERTO, dataLimite);
        for (Pedido p : pedidos) {
            p.setStatus(StatusPedido.FECHADO);
            pedidoRepository.save(p);
        }
        if (!pedidos.isEmpty()) {
            log.info("Fechamento automático: {} pedido(s) atualizado(s) para FECHADO", pedidos.size());
        }
    }
}
