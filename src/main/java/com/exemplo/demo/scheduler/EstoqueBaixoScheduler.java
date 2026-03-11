package com.exemplo.demo.scheduler;

import com.exemplo.demo.domain.Produto;
import com.exemplo.demo.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class EstoqueBaixoScheduler {

    private final ProdutoRepository produtoRepository;

    @Value("${app.scheduler.estoque-baixo-limite:5}")
    private int estoqueBaixoLimite;

    @Scheduled(fixedDelayString = "${app.scheduler.estoque-baixo-interval-ms}")
    @Transactional(readOnly = true)
    public void reportarEstoqueBaixo() {
        List<Produto> produtos = produtoRepository.findByEstoqueLessThan(estoqueBaixoLimite);
        if (produtos.isEmpty()) {
            return;
        }
        log.warn("Estoque baixo (limite={}): {} produto(s) - {}", estoqueBaixoLimite, produtos.size(),
                produtos.stream()
                        .map(p -> p.getNome() + " (id=" + p.getId() + ", estoque=" + p.getEstoque() + ")")
                        .toList());
    }
}
