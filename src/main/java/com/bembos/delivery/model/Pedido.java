package com.bembos.delivery.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PedidoID")
    private Integer pedidoId;

    @Column(name = "Fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "Estado", nullable = false, length = 20)
    private String estado;

    @Column(name = "Subtotal", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double subtotal;

    @Column(name = "Delivery", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double delivery;

    @Column(name = "Total", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double total;

    @Column(name = "TiempoEstimado", length = 50)
    private String tiempoEstimado;

    @Column(name = "MetodoPagoID", nullable = false)
    private Integer metodoPagoId;

    @ManyToOne
    @JoinColumn(name = "ClienteID", referencedColumnName = "ClienteID", nullable = true)
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoItem> items = new ArrayList<>();
}


