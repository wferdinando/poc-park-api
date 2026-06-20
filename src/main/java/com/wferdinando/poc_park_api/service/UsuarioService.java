package com.wferdinando.poc_park_api.service;

import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    
    private final UsuarioService service;

    public UsuarioService(UsuarioService service) {
        this.service = service;
    }

    
}
