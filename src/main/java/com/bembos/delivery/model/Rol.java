package com.bembos.delivery.model;

/**
 * Roles de usuario del sistema.
 * USER  → cliente registrado, puede hacer pedidos.
 * ADMIN → administrador, accede a /admin/** y gestiona usuarios.
 */
public enum Rol {
    USER,
    ADMIN
}
