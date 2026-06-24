package com.wferdinando.poc_park_api.web.dto.mapper;

import org.modelmapper.ModelMapper;

import com.wferdinando.poc_park_api.entity.Cliente;
import com.wferdinando.poc_park_api.web.dto.ClienteCreateDTO;
import com.wferdinando.poc_park_api.web.dto.ClienteResponseDTO;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteMapper {

    public static Cliente toCliente(ClienteCreateDTO dto) {
        return new ModelMapper().map(dto, Cliente.class);
    }

    public static ClienteResponseDTO toDTO(Cliente cliente) {
        return new ModelMapper().map(cliente, ClienteResponseDTO.class);
    }

}
