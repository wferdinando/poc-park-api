package com.wferdinando.poc_park_api.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VagaResponseDTO {
    
    private Long id;
    private String codigo;
    private String status;
}
