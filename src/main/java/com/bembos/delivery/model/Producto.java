package com.bembos.delivery.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProductoID")
    private Integer productoId;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "Nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "Descripcion", length = 500)
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @Column(name = "Precio", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double precio;

    @Column(name = "Imagen")
    private String imagen;

    @Column(name = "CategoriaID")
    private Integer categoriaId;

    @Transient 
    private String categoria;
}


