package com.wferdinando.poc_park_api.web.controller;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.wferdinando.poc_park_api.entity.Vaga;
import com.wferdinando.poc_park_api.service.VagaService;
import com.wferdinando.poc_park_api.web.dto.VagaCreateDTO;
import com.wferdinando.poc_park_api.web.dto.VagaResponseDTO;
import com.wferdinando.poc_park_api.web.dto.mapper.VagaMapper;
import com.wferdinando.poc_park_api.web.exception.ErrorMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/vagas")
public class VagaController {

    private final VagaService vagaService;

    public VagaController(VagaService vagaService) {
        this.vagaService = vagaService;
    }

    @Operation(summary = "Criar uma nova caga", description = "Recurso para criar uma nova vaga. "
            + "Requisição exige um Bearer Token. Acesso restrito a Role='CLIENTE'.", security = @SecurityRequirement(name = "security"), responses = {
                    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso!", headers = @Header(name = HttpHeaders.LOCATION, description = "URL do recurso criado")),
                    @ApiResponse(responseCode = "409", description = "Vaga já cadastrada no sistema.", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permitido para o perfil de CLIENTE.", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não processado. Dados de entrada inválidos!", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@RequestBody @Valid VagaCreateDTO dto) {
        Vaga vaga = VagaMapper.toVaga(dto);
        vagaService.salvar(vaga);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{codigo}")
                .buildAndExpand(vaga.getCodigo())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Localizar uma vaga", description = "Recurso para retornar uma caga pelo seu código. "
			+ "Requisição exige um Bearer Token. Acesso restrito a Role='ADMIN'.", security = @SecurityRequirement(name = "security"), responses = {
					@ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso!", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = VagaResponseDTO.class))),
					@ApiResponse(responseCode = "403", description = "Recurso não permitido para o perfil de CLIENTE.", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
					@ApiResponse(responseCode = "404", description = "Vaga não encontrada.", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
			})
    @GetMapping("/{codigo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VagaResponseDTO> getByCodigo(@PathVariable(name = "codigo") String codigo) {
        Vaga vaga = vagaService.buscarPorCodigo(codigo);
        return ResponseEntity.ok(VagaMapper.toDTO(vaga));
    }

}
