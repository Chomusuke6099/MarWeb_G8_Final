package com.bembos.delivery.repository;

import com.bembos.delivery.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    @Query(value = "CALL sp_listar_productos()", nativeQuery = true)
    List<Producto> listarProductos();

    @Query(value = "CALL sp_obtener_producto_por_id(:productoId)", nativeQuery = true)
    Producto obtenerProductoPorId(@Param("productoId") Integer productoId);

    @Query(value = "CALL sp_buscar_productos_por_categoria(:categoria)", nativeQuery = true)
    List<Producto> buscarPorCategoria(@Param("categoria") String categoria);
}

