package com.exemplo.demo.web.dto;

import com.exemplo.demo.domain.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoResponse {

    private Long id;
    private Long clienteId;
    private String clienteNome;
    private Instant dataPedido;
    private StatusPedido status;
    private BigDecimal total;
    private List<ItemPedidoResponse> itens;
}
