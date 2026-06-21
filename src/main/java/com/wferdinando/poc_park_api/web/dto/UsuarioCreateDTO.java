package com.wferdinando.poc_park_api.web.dto;

import jakarta.validation.constraints.Email;
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
public class UsuarioCreateDTO {

    @NotBlank(message = "O campo username é obrigatório!")
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Formato de e-mail inválido!")
    private String username;

    @NotBlank(message = "O campo password é obrigatório!")
    @Size(min = 6, max = 6, message = "A senha deve ter no mínimo 6 e no máximo 6caracteres!")
    private String password;

}
