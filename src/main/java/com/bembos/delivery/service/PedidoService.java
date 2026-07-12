package com.bembos.delivery.service;

import com.bembos.delivery.dto.PedidoRequestDTO;
import com.bembos.delivery.dto.PedidoResponseDTO;
import com.bembos.delivery.repository.ClienteRepository;
import com.bembos.delivery.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;

    @Transactional
    public PedidoResponseDTO guardarPedido(PedidoRequestDTO request) {
        
        System.out.println("=== INICIANDO GUARDAR PEDIDO ===");
        
        // 1. Guardar cliente
        System.out.println("1. Guardando cliente...");
        clienteRepository.guardarCliente(
            request.getCliente().getNombre(),
            request.getCliente().getTelefono(),
            request.getCliente().getEmail(),
            request.getCliente().getDireccion(),
            request.getCliente().getReferencia(),
            request.getCliente().getZonaId()
        );
        
        // 2. Obtener el cliente creado
        List<Object[]> clienteData = clienteRepository.buscarPorTelefono(request.getCliente().getTelefono());
        Integer clienteId = (Integer) clienteData.get(0)[0];
        System.out.println("Cliente ID: " + clienteId);
        
        // 3. Crear pedido
        System.out.println("2. Creando pedido...");
        Integer pedidoId = pedidoRepository.crearPedido(
            clienteId,
            request.getPago().getMetodoPagoId(),
            request.getTotales().getSubtotal(),
            request.getTotales().getDelivery(),
            request.getTotales().getTotal(),
            request.getTiempoEstimado(),
            request.getNumeroOperacion()
        );
        System.out.println("Pedido ID: " + pedidoId);
        
        // 4. Agregar items
        System.out.println("3. Agregando " + request.getProductos().size() + " items...");
        for (PedidoRequestDTO.ItemPedidoDTO item : request.getProductos()) {
            pedidoRepository.agregarItemPedido(
                pedidoId,
                item.getId().intValue(),
                item.getCantidad(),
                item.getPrecio()
            );
            System.out.println("   Producto: " + item.getNombre());
        }
        
        // 5. Confirmar pedido
        System.out.println("4. Confirmando pedido...");
        pedidoRepository.confirmarPedido(pedidoId);
        
        System.out.println("=== PEDIDO COMPLETADO ===");
        
        PedidoResponseDTO response = new PedidoResponseDTO();
        response.setPedidoId(pedidoId);
        response.setMensaje("Pedido confirmado");
        response.setEstado("CONFIRMADO");
        response.setTotal(request.getTotales().getTotal());
        
        return response;
    }
}


