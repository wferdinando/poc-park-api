package com.wferdinando.poc_park_api.web.controller;

import static com.wferdinando.poc_park_api.web.dto.mapper.UsuarioMapper.toDTO;
import static com.wferdinando.poc_park_api.web.dto.mapper.UsuarioMapper.toListDTO;
import static com.wferdinando.poc_park_api.web.dto.mapper.UsuarioMapper.toUsuario;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.wferdinando.poc_park_api.web.exception.ErrorMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Usuarios", description = "Contém todas as operações relativas aos recursos para cadastro, edição e leitura de usuários.")
@RestController
@RequestMapping("/api/v1/usuarios")
@Slf4j
public class UsuarioController {

        private final UsuarioService service;

        public UsuarioController(UsuarioService service) {
                this.service = service;
        }

        @Operation(summary = "Cria um novo usuário.", description = "Recurso para criar um novo usuário", responses = {
                        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDTO.class))),
                        @ApiResponse(responseCode = "409", description = "Usuário e-mail já cadastrado no sistema!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                        @ApiResponse(responseCode = "422", description = "Recurso não processado. Dados de entrada inválidos!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
        })
        @PostMapping
        public ResponseEntity<UsuarioResponseDTO> create(@Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO) {
                Usuario usuarioSalvo = service.salvar(toUsuario(usuarioCreateDTO));
                return ResponseEntity.status(HttpStatus.CREATED).body(
                                toDTO(usuarioSalvo));
        }

        @Operation(summary = "Recuperar um usuário pelo id.", description = "Requisição exige um Bearer Token. Acesso restrito a ADMIN|CLIENTE.", security = @SecurityRequirement(name = "security"), responses = {
                        @ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDTO.class))),
                        @ApiResponse(responseCode = "403", description = "Usuário sem permissão para acessar o recurso!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                        @ApiResponse(responseCode = "404", description = "Recurso não encontrado!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
        })
        @GetMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN') OR (hasRole('CLIENTE') AND #id == authentication.principal.id)")
        public ResponseEntity<UsuarioResponseDTO> getById(@PathVariable(name = "id") Long id) {
                Usuario usuario = service.buscarPorId(id);
                return ResponseEntity.status(HttpStatus.OK).body(toDTO(usuario));
        }

        @Operation(summary = "Atualizar senha.", description = "Requisição exige um Bearer Token. Acesso restrito a ADMIN|CLIENTE", security = @SecurityRequirement(name = "security"), responses = {
                        @ApiResponse(responseCode = "204", description = "Senha atualizada com sucesso!", content = @Content),
                        @ApiResponse(responseCode = "400", description = "Senha incorreta, tente novamente!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                        @ApiResponse(responseCode = "403", description = "Usuário sem permissão para acessar o recurso!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                        @ApiResponse(responseCode = "422", description = "Campos inválidos ou mal formatados!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
        })
        @PatchMapping("/{id}")
        @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE') AND (#id == authentication.principal.id)")
        public ResponseEntity<Void> updatePassword(@PathVariable(name = "id") Long id,
                        @Valid @RequestBody UsuarioSenhaDTO usuarioSenhaDTO) {
                service.editarSenha(
                                id,
                                usuarioSenhaDTO.getSenhaAtual(),
                                usuarioSenhaDTO.getNovaSenha(),
                                usuarioSenhaDTO.getConfirmaSenha());
                log.info("DTO recebido: {}", usuarioSenhaDTO);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        @Operation(summary = "Listar todos os usuários cadastrados.", description = "Requisição exige um Bearer Token. Acesso restrito a ADMIN.", security = @SecurityRequirement(name = "security"), responses = {
                        @ApiResponse(responseCode = "200", description = "Lista com todos os usuários cadastrados.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDTO.class)))),
                        @ApiResponse(responseCode = "403", description = "Usuário sem permissão para acessar o recurso!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
        })
        @GetMapping
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<List<UsuarioResponseDTO>> findAll() {
                List<Usuario> listaUsuarios = service.buscarTodos();
                return ResponseEntity.status(HttpStatus.OK).body(toListDTO(listaUsuarios));
        }

}
