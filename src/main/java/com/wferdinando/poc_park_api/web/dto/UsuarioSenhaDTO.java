package com.wferdinando.poc_park_api.web.dto;

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
public class UsuarioSenhaDTO {

    @NotBlank(message = "O campo senha atual é obrigatório!")
    @Size(min = 6, max = 6, message = "Deve ter no mínimo 6 e no máximo 6 caracteres!")
    private String senhaAtual;

    @NotBlank(message = "O campo nova senha é obrigatório!")
    @Size(min = 6, max = 6, message = "Deve ter no mínimo 6 e no máximo 6 caracteres!")
    private String novaSenha;

    @NotBlank(message = "O campo confirmação de senha é obrigatório!")
    @Size(min = 6, max = 6, message = "Deve ter no mínimo 6 e no máximo 6 caracteres!")
    private String confirmaSenha;
}
