package com.wferdinando.poc_park_api.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wferdinando.poc_park_api.entity.Cliente;
import com.wferdinando.poc_park_api.jwt.JwtUserDetails;
import com.wferdinando.poc_park_api.service.ClienteService;
import com.wferdinando.poc_park_api.service.UsuarioService;
import com.wferdinando.poc_park_api.web.dto.ClienteCreateDTO;
import com.wferdinando.poc_park_api.web.dto.ClienteResponseDTO;
import com.wferdinando.poc_park_api.web.dto.mapper.ClienteMapper;
import com.wferdinando.poc_park_api.web.exception.ErrorMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Clientes", description = "Contém todas as operações relativas aos recursos de um cliente.")
@RestController
@RequestMapping("api/v1/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final UsuarioService usuarioService;

    public ClienteController(ClienteService clienteService, UsuarioService usuarioService) {
        this.clienteService = clienteService;
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Criar um novo cliente", description = "Recurso para criar um novo cliente vinculado a um usuário cadatrado. "
            + "Requisição exige um Bearer Token. Acesso restrito a Role='CLIENTE'.", responses = {
                    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso!", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ClienteResponseDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permitido para o perdil de ADMIN.", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "409", description = "Cliente com o CPF informado já possui cadastro no sistema.", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não processado. Dados de entrada inválidos!", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDTO> create(
            @RequestBody @Valid ClienteCreateDTO clienteCreateDTO,
            @AuthenticationPrincipal JwtUserDetails userDetails) {

        Cliente cliente = ClienteMapper.toCliente(clienteCreateDTO);
        cliente.setUsuario(usuarioService.buscarPorId(userDetails.getId()));
        clienteService.salvar(cliente);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ClienteMapper.toDTO(cliente));
    }

}
