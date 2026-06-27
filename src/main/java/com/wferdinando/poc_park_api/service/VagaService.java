package com.wferdinando.poc_park_api.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wferdinando.poc_park_api.entity.Vaga;
import com.wferdinando.poc_park_api.exception.CodigoUniqueViolationException;
import com.wferdinando.poc_park_api.exception.EntityNotFoundException;
import com.wferdinando.poc_park_api.repository.VagaRepository;

@Service
public class VagaService {

    private final VagaRepository vagaRepository;

    public VagaService(VagaRepository vagaRepository) {
        this.vagaRepository = vagaRepository;
    }

    @Transactional
    public Vaga salvar(Vaga vaga) {
        try {
            return vagaRepository.save(vaga);
        } catch (DataIntegrityViolationException ex) {
            throw new CodigoUniqueViolationException(
                    String.format("Codigo '{%s}' ja cadastrado!", vaga.getCodigo()));
        }
    }

    @Transactional(readOnly = true)
    public Vaga buscarPorCodigo(String codigo) {
        return vagaRepository.findByCodigo(codigo).orElseThrow(
                () -> new EntityNotFoundException(String.format("Vaga com código '%s' nao foi encontrada!", codigo)));
    }

}
