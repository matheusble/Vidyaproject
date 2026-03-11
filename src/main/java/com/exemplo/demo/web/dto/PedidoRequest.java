package com.exemplo.demo.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class PedidoRequest {

    @NotNull(message = "ID do cliente é obrigatório")
    private Long clienteId;

    @NotNull(message = "Itens são obrigatórios")
    @Size(min = 1, message = "Pedido deve ter ao menos um item")
    @Valid
    private List<ItemPedidoRequest> itens;
}
