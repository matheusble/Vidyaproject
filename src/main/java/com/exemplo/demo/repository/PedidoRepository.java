package com.exemplo.demo.repository;

import com.exemplo.demo.domain.Pedido;
import com.exemplo.demo.domain.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByStatusAndDataPedidoBefore(StatusPedido status, Instant dataLimite);

    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.itens i LEFT JOIN FETCH i.produto WHERE p.id = :id")
    Optional<Pedido> findByIdWithItensAndProdutos(Long id);
}
