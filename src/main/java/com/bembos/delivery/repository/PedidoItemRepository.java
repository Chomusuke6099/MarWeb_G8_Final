package com.bembos.delivery.repository;

import com.bembos.delivery.model.PedidoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PedidoItemRepository extends JpaRepository<PedidoItem, Integer> {

    @Query(value = "CALL sp_listar_items_por_pedido(:pedidoId)", nativeQuery = true)
    List<PedidoItem> listarItemsPorPedido(@Param("pedidoId") Integer pedidoId);
}


