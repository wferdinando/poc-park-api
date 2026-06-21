package com.wferdinando.poc_park_api.web.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import com.wferdinando.poc_park_api.entity.Usuario;
import com.wferdinando.poc_park_api.web.dto.UsuarioCreateDTO;
import com.wferdinando.poc_park_api.web.dto.UsuarioResponseDTO;

public class UsuarioMapper {

    public static Usuario toUsuario(UsuarioCreateDTO usuarioCreateDTO) {
        return new ModelMapper().map(usuarioCreateDTO, Usuario.class);
    }

    public static UsuarioResponseDTO toDTO(Usuario usuario) {

        String role = usuario.getRole().name().substring("ROLE_".length());
        PropertyMap<Usuario, UsuarioResponseDTO> props = new PropertyMap<Usuario, UsuarioResponseDTO>() {
            @Override
            protected void configure() {
                map().setRole(role);
            }
        };

        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(props);
        return mapper.map(usuario, UsuarioResponseDTO.class);
    }

    public static List<UsuarioResponseDTO> toListDTO(List<Usuario> usuarios) {
        return usuarios.stream().map(usuario -> toDTO(usuario)).collect(Collectors.toList());
    }

}
