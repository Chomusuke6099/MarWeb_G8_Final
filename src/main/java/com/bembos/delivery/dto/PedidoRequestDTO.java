package com.bembos.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequestDTO {

    private ClienteDTO cliente;
    private List<ItemPedidoDTO> productos;
    private PagoDTO pago;
    private TotalesDTO totales;
    private String tiempoEstimado;
    private String numeroOperacion;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClienteDTO {
        private String nombre;
        private String telefono;
        private String email;
        private String direccion;
        private String referencia;
        private Integer zonaId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemPedidoDTO {
        private Integer id;
        private String nombre;
        private Double precio;
        private Integer cantidad;
        private String imagen;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PagoDTO {
        private Integer metodoPagoId;
        private String metodo;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TotalesDTO {
        private Double subtotal;
        private Double delivery;
        private Double total;
    }
}


