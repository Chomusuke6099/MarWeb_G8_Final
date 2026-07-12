package com.bembos.delivery.controller;

import com.bembos.delivery.dto.PedidoRequestDTO;
import com.bembos.delivery.dto.PedidoResponseDTO;
import com.bembos.delivery.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoRestController {

    private final PedidoService pedidoService;

    @PostMapping("/crear")
    public ResponseEntity<PedidoResponseDTO> crearPedido(@RequestBody PedidoRequestDTO request) {
        PedidoResponseDTO response = pedidoService.guardarPedido(request);
        return ResponseEntity.ok(response);
    }
}


