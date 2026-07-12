package com.bembos.delivery.repository;

import com.bembos.delivery.model.ZonaDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ZonaDeliveryRepository extends JpaRepository<ZonaDelivery, Integer> {

    @Query(value = "CALL sp_listar_zonas_delivery()", nativeQuery = true)
    List<ZonaDelivery> listarZonasDelivery();
    
    @Query(value = "CALL sp_calcular_zona_por_direccion(:direccion)", nativeQuery = true)
    List<Object[]> calcularZonaPorDireccion(@Param("direccion") String direccion);
    
    @Query(value = "CALL sp_obtener_costo_delivery(:zonaId)", nativeQuery = true)
    List<Object[]> obtenerCostoDelivery(@Param("zonaId") Integer zonaId);
}


