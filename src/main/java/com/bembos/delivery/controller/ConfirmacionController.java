package com.bembos.delivery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConfirmacionController {

    @GetMapping("/confirmacion")
    public String confirmacion(Model model) {
        return "confirmacion";
    }
}
