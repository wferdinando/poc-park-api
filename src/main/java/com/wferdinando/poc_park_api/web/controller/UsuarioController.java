package com.wferdinando.poc_park_api.web.controller;

import static com.wferdinando.poc_park_api.web.dto.mapper.UsuarioMapper.toDTO;
import static com.wferdinando.poc_park_api.web.dto.mapper.UsuarioMapper.toListDTO;
import static com.wferdinando.poc_park_api.web.dto.mapper.UsuarioMapper.toUsuario;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wferdinando.poc_park_api.entity.Usuario;
import com.wferdinando.poc_park_api.service.UsuarioService;
import com.wferdinando.poc_park_api.web.dto.UsuarioCreateDTO;
import com.wferdinando.poc_park_api.web.dto.UsuarioResponseDTO;
import com.wferdinando.poc_park_api.web.dto.UsuarioSenhaDTO;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> create(@RequestBody UsuarioCreateDTO usuarioCreateDTO) {
        Usuario usuarioSalvo = service.salvar(toUsuario(usuarioCreateDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(
                toDTO(usuarioSalvo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> getById(@PathVariable(name = "id") Long id) {
        Usuario usuario = service.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(toDTO(usuario));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable(name = "id") Long id,
            @RequestBody UsuarioSenhaDTO usuarioSenhaDTO) {
        service.editarSenha(
                id,
                usuarioSenhaDTO.getSenhaAtual(),
                usuarioSenhaDTO.getNovaSenha(),
                usuarioSenhaDTO.getConfirmaSenha());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> findAll() {
        List<Usuario> listaUsuarios = service.buscarTodos();
        return ResponseEntity.status(HttpStatus.OK).body(toListDTO(listaUsuarios));
    }

}
