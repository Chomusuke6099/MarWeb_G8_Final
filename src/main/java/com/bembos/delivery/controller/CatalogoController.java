package com.bembos.delivery.controller;

import com.bembos.delivery.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CatalogoController {

    private final ProductoService productoService;

    @GetMapping("/")
    public String catalogo(Model model) {
        model.addAttribute("productos", productoService.obtenerTodos());
        return "index";
    }
}
