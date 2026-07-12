package com.bembos.delivery.repository;

import com.bembos.delivery.model.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Integer> {

    @Query(value = "CALL sp_listar_metodos_pago()", nativeQuery = true)
    List<MetodoPago> listarMetodosPago();
}


