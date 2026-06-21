package com.wferdinando.poc_park_api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wferdinando.poc_park_api.entity.Usuario;
import com.wferdinando.poc_park_api.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Usuario salvar(Usuario usuario) {
        return repository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @Transactional
    public Usuario editarSenha(Long id, String senhaAtual, String novaSenha, String confirmaSenha) {

        if (!novaSenha.equals(confirmaSenha)) {
            throw new RuntimeException("Nova senha não confere com a confirmação de senha!");
        }

        Usuario usuario = buscarPorId(id);

        if (!usuario.getPassword().equals(senhaAtual)) {
            throw new RuntimeException("Sua senha atual está incorreta!");
        }
        usuario.setPassword(novaSenha);
        return usuario;
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos() {
        return repository.findAll();
    }

}
