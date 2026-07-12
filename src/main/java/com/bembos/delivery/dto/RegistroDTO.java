package com.bembos.delivery.dto;

import lombok.Data;

/**
 * DTO para el formulario de registro de usuarios.
 */
@Data
public class RegistroDTO {

    private String username;
    private String email;
    private String password;
    private String confirmarPassword;
}
