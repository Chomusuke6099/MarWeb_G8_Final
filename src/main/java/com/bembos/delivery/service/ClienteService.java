package com.bembos.delivery.service;

import com.bembos.delivery.model.Cliente;
import com.bembos.delivery.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List; 

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public Integer guardarCliente(String nombre, String telefono, String email, String direccion, String referencia, Integer zonaId) {
        return clienteRepository.guardarCliente(nombre, telefono, email, direccion, referencia, zonaId);
    }
    
    public Cliente buscarPorTelefono(String telefono) {
        List<Object[]> resultado = clienteRepository.buscarPorTelefono(telefono);
        if (resultado != null && !resultado.isEmpty()) {
            Object[] row = resultado.get(0);
            Cliente cliente = new Cliente();
            cliente.setClienteId(((Number) row[0]).intValue());
            cliente.setNombre((String) row[1]);
            cliente.setTelefono((String) row[2]);
            cliente.setEmail((String) row[3]);
            cliente.setDireccion((String) row[4]);
            cliente.setReferencia((String) row[5]);
            if (row[6] != null) {
                cliente.setZonaId(((Number) row[6]).intValue());
            }
            return cliente;
        }
        return null;
    }
}


