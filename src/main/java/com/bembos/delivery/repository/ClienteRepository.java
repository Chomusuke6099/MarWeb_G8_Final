package com.bembos.delivery.repository;

import com.bembos.delivery.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List; 

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    @Query(value = "CALL sp_guardar_cliente(:nombre, :telefono, :email, :direccion, :referencia, :zonaId)", nativeQuery = true)
    Integer guardarCliente(
        @Param("nombre") String nombre,
        @Param("telefono") String telefono,
        @Param("email") String email,
        @Param("direccion") String direccion,
        @Param("referencia") String referencia,
        @Param("zonaId") Integer zonaId
    );
    
    @Query(value = "CALL sp_buscar_cliente_por_telefono(:telefono)", nativeQuery = true)
    List<Object[]> buscarPorTelefono(@Param("telefono") String telefono);
}


