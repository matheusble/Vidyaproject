package com.exemplo.demo.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

@Component
@Slf4j
public class ViaCepClient {

    private final WebClient viaCepWebClient;

    public ViaCepClient(@Qualifier("viaCepWebClient") WebClient viaCepWebClient) {
        this.viaCepWebClient = viaCepWebClient;
    }

    /**
     * Busca endereço pelo CEP. Em caso de falha (timeout, CEP inválido), retorna Optional vazio
     * para não quebrar o cadastro do cliente.
     */
    public Optional<String> buscarEnderecoPorCep(String cep) {
        if (cep == null || cep.isBlank()) {
            return Optional.empty();
        }
        String cepLimpo = cep.replaceAll("\\D", "");
        if (cepLimpo.length() != 8) {
            return Optional.empty();
        }
        try {
            ViaCepResponse response = viaCepWebClient
                    .get()
                    .uri("/{cep}/json", cepLimpo)
                    .retrieve()
                    .bodyToMono(ViaCepResponse.class)
                    .block();
            if (response != null && !response.isErro()) {
                return Optional.ofNullable(response.formatarEndereco());
            }
        } catch (WebClientResponseException e) {
            log.warn("ViaCEP retornou erro para CEP {}: {}", cep, e.getStatusCode());
        } catch (Exception e) {
            log.warn("Falha ao consultar ViaCEP para CEP {}: {}", cep, e.getMessage());
        }
        return Optional.empty();
    }
}
