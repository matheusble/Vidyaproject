package com.exemplo.demo.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProdutoRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 200)
    private String nome;

    @Size(max = 1000)
    private String descricao;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0", inclusive = false)
    private BigDecimal preco;

    @NotNull(message = "Estoque é obrigatório")
    @PositiveOrZero
    private Integer estoque;
}
