package com.exemplo.demo.web;

import com.exemplo.demo.domain.ItemPedido;
import com.exemplo.demo.domain.Pedido;
import com.exemplo.demo.service.PedidoService;
import com.exemplo.demo.web.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "CRUD de pedidos com itens; criação dispara evento (estoque e notificação)")
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping
    @Operation(summary = "Listar pedidos")
    public ResponseEntity<List<PedidoResponse>> listar() {
        return ResponseEntity.ok(
                pedidoService.listar().stream()
                        .map(PedidoController::toResponse)
                        .toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID (com itens)")
    public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable Long id) {
        return pedidoService.buscarPorIdComItens(id)
                .or(() -> pedidoService.buscarPorId(id))
                .map(PedidoController::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar pedido (dispara atualização de estoque e notificação)")
    public ResponseEntity<PedidoResponse> criar(@Valid @RequestBody PedidoRequest request) {
        List<PedidoService.ItemPedidoCriar> itens = request.getItens().stream()
                .map(i -> {
                    var dto = new PedidoService.ItemPedidoCriar();
                    dto.setProdutoId(i.getProdutoId());
                    dto.setQuantidade(i.getQuantidade());
                    return dto;
                })
                .toList();
        Pedido pedido = pedidoService.criar(request.getClienteId(), itens);
        Pedido comItens = pedidoService.buscarPorIdComItens(pedido.getId()).orElse(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(comItens));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do pedido")
    public ResponseEntity<PedidoResponse> atualizarStatus(@PathVariable Long id,
                                                           @Valid @RequestBody StatusPedidoRequest request) {
        return pedidoService.atualizarStatus(id, request.getStatus())
                .map(PedidoController::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir pedido")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        return pedidoService.excluir(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    private static PedidoResponse toResponse(Pedido p) {
        List<ItemPedidoResponse> itens = p.getItens() != null
                ? p.getItens().stream().map(PedidoController::toItemResponse).toList()
                : List.of();
        return PedidoResponse.builder()
                .id(p.getId())
                .clienteId(p.getCliente().getId())
                .clienteNome(p.getCliente().getNome())
                .dataPedido(p.getDataPedido())
                .status(p.getStatus())
                .total(p.getTotal())
                .itens(itens)
                .build();
    }

    private static ItemPedidoResponse toItemResponse(ItemPedido i) {
        return ItemPedidoResponse.builder()
                .id(i.getId())
                .produtoId(i.getProduto().getId())
                .produtoNome(i.getProduto().getNome())
                .quantidade(i.getQuantidade())
                .precoUnitario(i.getPrecoUnitario())
                .subtotal(i.getSubtotal())
                .build();
    }
}
