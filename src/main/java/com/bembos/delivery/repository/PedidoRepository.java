package com.bembos.delivery.repository;

import com.bembos.delivery.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    @Query(value = "CALL sp_crear_pedido(:clienteId, :metodoPagoId, :subtotal, :delivery, :total, :tiempoEstimado, :numeroOperacion)", nativeQuery = true)
    Integer crearPedido(
        @Param("clienteId") Integer clienteId,
        @Param("metodoPagoId") Integer metodoPagoId,
        @Param("subtotal") Double subtotal,
        @Param("delivery") Double delivery,
        @Param("total") Double total,
        @Param("tiempoEstimado") String tiempoEstimado,
        @Param("numeroOperacion") String numeroOperacion
    );
    
    @Modifying
    @Transactional
    @Query(value = "CALL sp_agregar_item_pedido(:pedidoId, :productoId, :cantidad, :precioUnitario)", nativeQuery = true)
    void agregarItemPedido(
        @Param("pedidoId") Integer pedidoId,
        @Param("productoId") Integer productoId,
        @Param("cantidad") Integer cantidad,
        @Param("precioUnitario") Double precioUnitario
    );
    
    @Modifying
    @Transactional
    @Query(value = "CALL sp_confirmar_pedido(:pedidoId)", nativeQuery = true)
    void confirmarPedido(@Param("pedidoId") Integer pedidoId);
    
    @Query(value = "CALL sp_obtener_pedido_completo(:pedidoId)", nativeQuery = true)
    List<Object[]> obtenerPedidoCompleto(@Param("pedidoId") Integer pedidoId);
    
    @Query(value = "CALL sp_listar_items_por_pedido(:pedidoId)", nativeQuery = true)
    List<Object[]> listarItemsPorPedido(@Param("pedidoId") Integer pedidoId);
}


