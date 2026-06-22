package com.wferdinando.poc_park_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wferdinando.poc_park_api.entity.Usuario;
import com.wferdinando.poc_park_api.entity.Usuario.Role;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    @Query("SELECT u.role FROM Usuario u WHERE u.username like :username")
    Role findRoleByUsername(String username);

}
