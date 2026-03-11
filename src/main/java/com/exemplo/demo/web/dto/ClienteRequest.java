package com.exemplo.demo.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClienteRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 200)
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email
    @Size(max = 255)
    private String email;

    @Size(max = 9)
    private String cep;
}
