package com.wferdinando.poc_park_api.web.dto;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClienteCreateDTO {

    @NotBlank(message = "O campo nome é obrigatório!")
    @Size(min = 3, max = 100, message = "O nome deve ter no mínimo 3 e no máximo 100 caracteres!")
    private String nome;

    @CPF
    @NotBlank(message = "O campo CPF é obrigatório!")
    @Size(min = 11, max = 11, message = "O CPF deve ter no mínimo 11 e no máximo 11 caracteres!")
    private String cpf;
}
