package com.bembos.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO de respuesta tras autenticación JWT exitosa.
 */
@Data
@AllArgsConstructor
public class JwtResponseDTO {

    private String token;
    private String tipo = "Bearer";
    private String username;
    private String rol;

    public JwtResponseDTO(String token, String username, String rol) {
        this.token = token;
        this.username = username;
        this.rol = rol;
    }
}
