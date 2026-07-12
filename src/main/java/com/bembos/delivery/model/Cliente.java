package com.bembos.delivery.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ClienteID")
    private Integer clienteId;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "Nombre", nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El teléfono es obligatorio")
    @Column(name = "Telefono", nullable = false, length = 15)
    private String telefono;

    @Email(message = "Email inválido")
    @Column(name = "Email", length = 100)
    private String email;

    @Column(name = "Direccion", nullable = false, length = 255)
    private String direccion;
    
    @Column(name = "Referencia", length = 255)
    private String referencia;

    @Column(name = "ZonaID", nullable = true)
    private Integer zonaId;
    
    @Column(name = "FechaRegistro")
    private java.time.LocalDateTime fechaRegistro;
}


