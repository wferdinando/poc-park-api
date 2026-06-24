package com.wferdinando.poc_park_api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.wferdinando.poc_park_api.web.dto.UsuarioCreateDTO;
import com.wferdinando.poc_park_api.web.dto.UsuarioResponseDTO;
import com.wferdinando.poc_park_api.web.dto.UsuarioSenhaDTO;
import com.wferdinando.poc_park_api.web.exception.ErrorMessage;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Sql(scripts = "classpath:sql/usuarios/usuarios-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sql/usuarios/usuarios-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UsuarioIntegration {

	@Autowired
	WebTestClient webTestClient;

	@Test
	void createUsuario_ComUsernameEPasswordValidos_RetornarUsuarioCriadoComStatus201() {
		UsuarioResponseDTO responseBody = webTestClient
				.post()
				.uri("/api/v1/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UsuarioCreateDTO("tody@email.com", "123456"))
				.exchange()
				.expectStatus().isCreated()
				.expectBody(UsuarioResponseDTO.class)
				.returnResult().getResponseBody();

		assertThat(responseBody).isNotNull();
		assertThat(responseBody.getId()).isNotNull();
		assertThat(responseBody.getUsername()).isEqualTo("tody@email.com");
		assertThat(responseBody.getRole()).isEqualTo("CLIENTE");
	}

	@Test
	void createUsuario_ComUsernameInvalidos_RetornarErrorMessageStatus422() {
		ErrorMessage responseBody = webTestClient
				.post()
				.uri("/api/v1/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UsuarioCreateDTO("", "123456"))
				.exchange()
				.expectStatus().isEqualTo(422)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		assertThat(responseBody).isNotNull();
		assertThat(responseBody.getStatus()).isEqualTo(422);

		responseBody = webTestClient
				.post()
				.uri("/api/v1/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UsuarioCreateDTO("tody@", "123456"))
				.exchange()
				.expectStatus().isEqualTo(422)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		assertThat(responseBody).isNotNull();
		assertThat(responseBody.getStatus()).isEqualTo(422);

		responseBody = webTestClient
				.post()
				.uri("/api/v1/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UsuarioCreateDTO("tody@email", "123456"))
				.exchange()
				.expectStatus().isEqualTo(422)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		assertThat(responseBody).isNotNull();
		assertThat(responseBody.getStatus()).isEqualTo(422);
	}

	@Test
	void createUsuario_ComPasswordInvalidos_RetornarErrorMessageStatus422() {
		ErrorMessage responseBody = webTestClient
				.post()
				.uri("/api/v1/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UsuarioCreateDTO("tody@email.com", ""))
				.exchange()
				.expectStatus().isEqualTo(422)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		assertThat(responseBody).isNotNull();
		assertThat(responseBody.getStatus()).isEqualTo(422);

		responseBody = webTestClient
				.post()
				.uri("/api/v1/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UsuarioCreateDTO("tody@email.com", "12345"))
				.exchange()
				.expectStatus().isEqualTo(422)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		assertThat(responseBody).isNotNull();
		assertThat(responseBody.getStatus()).isEqualTo(422);

		responseBody = webTestClient
				.post()
				.uri("/api/v1/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UsuarioCreateDTO("tody@email.com", "1234567"))
				.exchange()
				.expectStatus().isEqualTo(422)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		assertThat(responseBody).isNotNull();
		assertThat(responseBody.getStatus()).isEqualTo(422);
	}

	@Test
	void createUsuario_ComUsernameJaExistente_RetornarErrorMessageStatus409() {
		ErrorMessage responseBody = webTestClient
				.post()
				.uri("/api/v1/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UsuarioCreateDTO("ana@email.com", "123456"))
				.exchange()
				.expectStatus().isEqualTo(409)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		assertThat(responseBody).isNotNull();
		assertThat(responseBody.getStatus()).isEqualTo(409);
	}

	@Test
	void buscarUsuario_ComIdExistente_RetornarUsuarioComStatus200() {
		UsuarioResponseDTO responseBody = webTestClient
				.get()
				.uri("/api/v1/usuarios/100")
				.headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody(UsuarioResponseDTO.class)
				.returnResult().getResponseBody();

		assertThat(responseBody).isNotNull();
		assertThat(responseBody.getId()).isEqualTo(100L);
		assertThat(responseBody.getUsername()).isEqualTo("ana@email.com");
		assertThat(responseBody.getRole()).isEqualTo("ADMIN");

		responseBody = webTestClient
				.get()
				.uri("/api/v1/usuarios/101")
				.headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody(UsuarioResponseDTO.class)
				.returnResult().getResponseBody();

		assertThat(responseBody).isNotNull();
		assertThat(responseBody.getId()).isEqualTo(101L);
		assertThat(responseBody.getUsername()).isEqualTo("bia@email.com");
		assertThat(responseBody.getRole()).isEqualTo("CLIENTE");

		responseBody = webTestClient
				.get()
				.uri("/api/v1/usuarios/101")
				.headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "bia@email.com", "123456"))
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody(UsuarioResponseDTO.class)
				.returnResult().getResponseBody();

		assertThat(responseBody).isNotNull();
		assertThat(responseBody.getId()).isEqualTo(101L);
		assertThat(responseBody.getUsername()).isEqualTo("bia@email.com");
		assertThat(responseBody.getRole()).isEqualTo("CLIENTE");
	}

	@Test
	void buscarUsuario_ComIdInexistente_RetornarErrorMessageStatus404() {
		ErrorMessage responseBody = webTestClient
				.get()
				.uri("/api/v1/usuarios/0")
				.headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isNotFound()
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		assertThat(responseBody).isNotNull();
		assertThat(responseBody.getStatus()).isEqualTo(404);
	}

	@Test
	void buscarUsuario_ComUsuarioClieenteBuscandoOutroCliente_RetornarErrorMessageStatus403() {
		ErrorMessage responseBody = webTestClient
				.get()
				.uri("/api/v1/usuarios/102")
				.headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "bia@email.com", "123456"))
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isForbidden()
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		assertThat(responseBody).isNotNull();
		assertThat(responseBody.getStatus()).isEqualTo(403);

	}

	@Test
	void editarSenha_ComDadosValidos_RetornaStatus204() {
		webTestClient
				.patch()
				.uri("/api/v1/usuarios/100")
				.headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UsuarioSenhaDTO("123456", "123456", "123456"))
				.exchange()
				.expectStatus().isNoContent();

		webTestClient
				.patch()
				.uri("/api/v1/usuarios/101")
				.headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "bia@email.com", "123456"))
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UsuarioSenhaDTO("123456", "123456", "123456"))
				.exchange()
				.expectStatus().isNoContent();
	}

	@Test
	void editarSenha_ComIUsuariosDiferentes_RetornarErrorMessageStatus403() {
		ErrorMessage responseBody = webTestClient
				.patch()
				.uri("/api/v1/usuarios/0")
				.headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UsuarioSenhaDTO("123456", "123456", "123456"))
				.exchange()
				.expectStatus().isForbidden()
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		assertThat(responseBody).isNotNull();
		assertThat(responseBody.getStatus()).isEqualTo(403);

		responseBody = webTestClient
				.patch()
				.uri("/api/v1/usuarios/0")
				.headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "bia@email.com", "123456"))
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UsuarioSenhaDTO("123456", "123456", "123456"))
				.exchange()
				.expectStatus().isForbidden()
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		assertThat(responseBody).isNotNull();
		assertThat(responseBody.getStatus()).isEqualTo(403);

	}

	@Test
	void editarSenha_ComCamposInvalidos_RetornarErrorMessageStatus422() {
		ErrorMessage responseBody = webTestClient
				.patch()
				.uri("/api/v1/usuarios/100")
				.headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UsuarioSenhaDTO("", "", ""))
				.exchange()
				.expectStatus().isEqualTo(422)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		assertThat(responseBody).isNotNull();
		assertThat(responseBody.getStatus()).isEqualTo(422);

		responseBody = webTestClient
				.patch()
				.uri("/api/v1/usuarios/100")
				.headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UsuarioSenhaDTO("12345", "12345", "12345"))
				.exchange()
				.expectStatus().isEqualTo(422)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		assertThat(responseBody).isNotNull();
		assertThat(responseBody.getStatus()).isEqualTo(422);

		responseBody = webTestClient
				.patch()
				.uri("/api/v1/usuarios/100")
				.headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UsuarioSenhaDTO("1234567", "1234567", "1234567"))
				.exchange()
				.expectStatus().isEqualTo(422)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		assertThat(responseBody).isNotNull();
		assertThat(responseBody.getStatus()).isEqualTo(422);

	}

	@Test
	void editarSenha_ComSenhasInvalidas_RetornarErrorMessageStatus400() {
		ErrorMessage responseBody = webTestClient
				.patch()
				.uri("/api/v1/usuarios/100")
				.headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UsuarioSenhaDTO("123456", "123456", "000000"))
				.exchange()
				.expectStatus().isEqualTo(400)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		assertThat(responseBody).isNotNull();
		assertThat(responseBody.getStatus()).isEqualTo(400);

		responseBody = webTestClient
				.patch()
				.uri("/api/v1/usuarios/100")
				.headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UsuarioSenhaDTO("000000", "123456", "123456"))
				.exchange()
				.expectStatus().isEqualTo(400)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		assertThat(responseBody).isNotNull();
		assertThat(responseBody.getStatus()).isEqualTo(400);

	}

	@Test
	void listarUsuarios_SemQualuquerParametro_RetornarListaDeUsuariosComStatus200() {
		List<UsuarioResponseDTO> responseBody = webTestClient
				.get()
				.uri("/api/v1/usuarios")
				.headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(UsuarioResponseDTO.class)
				.returnResult().getResponseBody();

		assertThat(responseBody).isNotNull();
		assertThat(responseBody.size()).isEqualTo(3);

	}

	@Test
	void listarUsuarios_ComUsuariosSemPermissao_RetornarErrorMessageStatus403() {
		ErrorMessage responseBody = webTestClient
				.get()
				.uri("/api/v1/usuarios")
				.headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "bia@email.com", "123456"))
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isForbidden()
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		assertThat(responseBody).isNotNull();
		assertThat(responseBody.getStatus()).isEqualTo(403);
	}
}
