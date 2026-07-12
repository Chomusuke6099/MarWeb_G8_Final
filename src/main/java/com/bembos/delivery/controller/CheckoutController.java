package com.bembos.delivery.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CheckoutController {

    @GetMapping("/checkout")
    public String checkout(Model model) {
        return "checkout";
    }
}


