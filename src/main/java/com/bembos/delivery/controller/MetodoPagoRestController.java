package com.bembos.delivery.controller;

import com.bembos.delivery.model.MetodoPago;
import com.bembos.delivery.repository.MetodoPagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/metodos-pago")
@RequiredArgsConstructor
public class MetodoPagoRestController {

    private final MetodoPagoRepository metodoPagoRepository;

    @GetMapping
    public List<MetodoPago> listarMetodosPago() {
        return metodoPagoRepository.listarMetodosPago();
    }
}


