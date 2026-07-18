package com.bembos.delivery.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Zona_Delivery")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZonaDelivery {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ZonaID")
    private Integer zonaId;
    
    @Column(name = "Nombre", nullable = false, length = 50)
    private String nombre;
    
    @Column(name = "Costo", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double costo;
    
    @Column(name = "TiempoEstimado", length = 50)
    private String tiempoEstimado;
    
    @Column(name = "PalabrasClave")
    private String palabrasClave;
}


