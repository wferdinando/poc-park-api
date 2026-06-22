package com.wferdinando.poc_park_api.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wferdinando.poc_park_api.entity.Usuario;
import com.wferdinando.poc_park_api.service.UsuarioService;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UsuarioService usuarioService;

    public JwtUserDetailsService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.buscarPorUsername(username);
        return new JwtUserDetails(usuario);
    }

    public JwtToken getTokenAuthenticated(String username) {
        Usuario.Role role = usuarioService.buscarRolePorUserName(username);
        return JwtUtils.createToken(username, role.name().substring("ROLE_".length()));
    }

}
