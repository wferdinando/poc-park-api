package com.wferdinando.poc_park_api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.wferdinando.poc_park_api.web.dto.ClienteCreateDTO;
import com.wferdinando.poc_park_api.web.dto.ClienteResponseDTO;
import com.wferdinando.poc_park_api.web.exception.ErrorMessage;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Sql(scripts = "classpath:sql/clientes/clientes-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sql/clientes/clientes-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClienteIntegration {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void criarCliente_ComDadosValidos_RetornarClienteComStatus201() {
        ClienteResponseDTO responseBody = webTestClient
                .post()
                .uri("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "toby@email.com", "123456"))
                .bodyValue(new ClienteCreateDTO("Tobias Ferreira", "91191064085"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ClienteResponseDTO.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getId()).isNotNull();
        assertThat(responseBody.getNome()).isEqualTo("Tobias Ferreira");
        assertThat(responseBody.getCpf()).isEqualTo("91191064085");
    }

    @Test
    void criarCliente_ComDadosInvalidos_RetornarErrorMessageStatus422() {
        ErrorMessage responseBody = webTestClient
                .post()
                .uri("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "toby@email.com", "123456"))
                .bodyValue(new ClienteCreateDTO("", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient
                .post()
                .uri("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "toby@email.com", "123456"))
                .bodyValue(new ClienteCreateDTO("To", "12345678911"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient
                .post()
                .uri("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "toby@email.com", "123456"))
                .bodyValue(new ClienteCreateDTO("To", "911.910.640-85"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    void criarCliente_ComCpfJaCadastrado_RetornarErrorMessageStatus409() {
        ErrorMessage responseBody = webTestClient
                .post()
                .uri("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "toby@email.com", "123456"))
                .bodyValue(new ClienteCreateDTO("Tobias Ferreira", "55352517047"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(409);

    }

    @Test
    void criarCliente_ComUsuarioNaoPermitido_RetornarErrorMessageStatus403() {
        ErrorMessage responseBody = webTestClient
                .post()
                .uri("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
                .bodyValue(new ClienteCreateDTO("Tobias Ferreira", "91191064085"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(403);

    }
}