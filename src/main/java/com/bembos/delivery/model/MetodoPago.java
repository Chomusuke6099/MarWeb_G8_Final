package com.bembos.delivery.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Metodo_Pago")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetodoPago {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MetodoPagoID")
    private Integer metodoPagoId;
    
    @Column(name = "Nombre", nullable = false, unique = true, length = 50)
    private String nombre;
    
}


