package com.bembos.delivery.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Pedido_Item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PedidoItemID")
    private Integer pedidoItemId;

    @Column(name = "Cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "PrecioUnitario", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double precioUnitario;

    @ManyToOne
    @JoinColumn(name = "PedidoID", referencedColumnName = "PedidoID", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "ProductoID", referencedColumnName = "ProductoID", nullable = false)
    private Producto producto;
}

