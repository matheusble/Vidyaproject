package com.exemplo.demo.integration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ViaCepResponse {

    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String localidade;
    private String uf;
    private String erro;

    public boolean isErro() {
        return "true".equalsIgnoreCase(erro) || (logradouro == null && localidade == null);
    }

    public String formatarEndereco() {
        if (isErro()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        if (logradouro != null && !logradouro.isBlank()) {
            sb.append(logradouro);
        }
        if (bairro != null && !bairro.isBlank()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(bairro);
        }
        if (localidade != null && !localidade.isBlank()) {
            if (sb.length() > 0) sb.append(" - ");
            sb.append(localidade);
        }
        if (uf != null && !uf.isBlank()) {
            if (sb.length() > 0) sb.append("/");
            sb.append(uf);
        }
        return sb.length() > 0 ? sb.toString() : null;
    }
}
