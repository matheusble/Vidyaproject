package com.exemplo.demo.integration;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PedidoCriadoEvent extends ApplicationEvent {

    private final Long pedidoId;

    public PedidoCriadoEvent(Object source, Long pedidoId) {
        super(source);
        this.pedidoId = pedidoId;
    }
}
