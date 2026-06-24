package com.wferdinando.poc_park_api.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wferdinando.poc_park_api.entity.Cliente;
import com.wferdinando.poc_park_api.exception.CpfUniqueViolationException;
import com.wferdinando.poc_park_api.repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository cienteRepository;

    public ClienteService(ClienteRepository cienteRepository) {
        this.cienteRepository = cienteRepository;
    }

    @Transactional
    public Cliente salvar(Cliente cliente) {
        try {
            return cienteRepository.save(cliente);
        } catch (DataIntegrityViolationException ex) {
            throw new CpfUniqueViolationException(
                    String.format("CPF '{%s}' não pode ser cadastrado, já existe no sistema!",
                            cliente.getCpf()));
        }
    }
}
