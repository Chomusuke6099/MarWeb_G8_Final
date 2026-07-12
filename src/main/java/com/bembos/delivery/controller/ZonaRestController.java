package com.bembos.delivery.controller;

import com.bembos.delivery.model.ZonaDelivery;
import com.bembos.delivery.repository.ZonaDeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/zonas")
@RequiredArgsConstructor
public class ZonaRestController {

    private final ZonaDeliveryRepository zonaDeliveryRepository;

    @GetMapping
    public List<Map<String, Object>> obtenerZonas() {
        List<ZonaDelivery> zonasDB = zonaDeliveryRepository.listarZonasDelivery();
        List<Map<String, Object>> zonas = new ArrayList<>();
        
        for (ZonaDelivery zona : zonasDB) {
            Map<String, Object> mapa = new HashMap<>();
            mapa.put("id", zona.getZonaId());
            mapa.put("nombre", zona.getNombre());
            mapa.put("costo", zona.getCosto());
            mapa.put("tiempoEstimado", zona.getTiempoEstimado());
            zonas.add(mapa);
        }
        
        return zonas;
    }
    
    @PostMapping("/calcular")
    public ResponseEntity<?> calcularZona(@RequestBody Map<String, String> request) {
        String direccion = request.get("direccion");
        
        if (direccion == null || direccion.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Dirección requerida"));
        }
        
        List<Object[]> resultado = zonaDeliveryRepository.calcularZonaPorDireccion(direccion);
        
        if (resultado != null && !resultado.isEmpty()) {
            Object[] zona = resultado.get(0);
            Map<String, Object> response = new HashMap<>();
            response.put("zonaId", zona[0]);
            response.put("costo", zona[1]);
            response.put("zonaNombre", zona[2]);
            response.put("tiempoEstimado", zona[3]);
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.ok(Map.of("error", "Zona no encontrada", "mensaje", "No realizamos delivery en esta zona"));
    }
}


