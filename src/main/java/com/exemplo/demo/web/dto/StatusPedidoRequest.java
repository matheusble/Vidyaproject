package com.exemplo.demo.web.dto;

import com.exemplo.demo.domain.StatusPedido;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusPedidoRequest {

    @NotNull(message = "Status é obrigatório")
    private StatusPedido status;
}
