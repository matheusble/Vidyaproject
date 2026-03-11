package com.exemplo.demo.service;

import com.exemplo.demo.domain.Cliente;
import com.exemplo.demo.integration.ViaCepClient;
import com.exemplo.demo.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ViaCepClient viaCepClient;

    @Transactional(readOnly = true)
    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    @Transactional
    public Cliente criar(Cliente cliente) {
        preencherEnderecoPorCep(cliente);
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Optional<Cliente> atualizar(Long id, Cliente dados) {
        return clienteRepository.findById(id)
                .map(existente -> {
                    existente.setNome(dados.getNome());
                    existente.setEmail(dados.getEmail());
                    existente.setCep(dados.getCep());
                    preencherEnderecoPorCep(existente);
                    return clienteRepository.save(existente);
                });
    }

    @Transactional
    public boolean excluir(Long id) {
        return clienteRepository.findById(id)
                .map(c -> {
                    clienteRepository.delete(c);
                    return true;
                })
                .orElse(false);
    }

    private void preencherEnderecoPorCep(Cliente cliente) {
        if (cliente.getCep() != null && !cliente.getCep().isBlank()) {
            viaCepClient.buscarEnderecoPorCep(cliente.getCep())
                    .ifPresent(cliente::setEndereco);
        }
    }
}
