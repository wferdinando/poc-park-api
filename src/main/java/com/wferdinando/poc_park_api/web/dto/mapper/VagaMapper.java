package com.wferdinando.poc_park_api.web.dto.mapper;

import org.modelmapper.ModelMapper;

import com.wferdinando.poc_park_api.entity.Vaga;
import com.wferdinando.poc_park_api.web.dto.VagaCreateDTO;
import com.wferdinando.poc_park_api.web.dto.VagaResponseDTO;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VagaMapper {

    public static Vaga toVaga(VagaCreateDTO vagaCreateDTO) {
        return new ModelMapper().map(vagaCreateDTO, Vaga.class);
    }

    public static VagaResponseDTO toDTO(Vaga vaga) {
        return new ModelMapper().map(vaga, VagaResponseDTO.class);
    }
}
