package com.bembos.delivery.dto;

import lombok.Data;

/**
 * DTO para el endpoint REST de autenticación JWT (/api/auth/login).
 */
@Data
public class AuthLoginDTO {

    private String username;
    private String password;
}
