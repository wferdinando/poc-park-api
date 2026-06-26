package com.wferdinando.poc_park_api.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wferdinando.poc_park_api.entity.Cliente;
import com.wferdinando.poc_park_api.exception.CpfUniqueViolationException;
import com.wferdinando.poc_park_api.exception.EntityNotFoundException;
import com.wferdinando.poc_park_api.repository.ClienteRepository;
import com.wferdinando.poc_park_api.repository.projection.ClienteProjection;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository cienteRepository) {
        this.clienteRepository = cienteRepository;
    }

    @Transactional
    public Cliente salvar(Cliente cliente) {
        try {
            return clienteRepository.save(cliente);
        } catch (DataIntegrityViolationException ex) {
            throw new CpfUniqueViolationException(
                    String.format("CPF '{%s}' não pode ser cadastrado, já existe no sistema!",
                            cliente.getCpf()));
        }
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Cliente id=%s nao encontrado!", id)));
    }

    @Transactional(readOnly = true)
    public Page<ClienteProjection> buscarTodos(Pageable pageable) {
        return clienteRepository.findAllPageable(pageable);
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorUsuarioId(Long id) {
        return clienteRepository.findByUsuarioId(id);
    }
}
