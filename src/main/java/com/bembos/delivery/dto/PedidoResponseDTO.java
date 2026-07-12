package com.bembos.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseDTO {
    private Integer pedidoId;
    private String mensaje;
    private String estado;
    private Double total;
}


