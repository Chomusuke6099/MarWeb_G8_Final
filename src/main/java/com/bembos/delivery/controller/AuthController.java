package com.bembos.delivery.controller;

import com.bembos.delivery.dto.RegistroDTO;
import com.bembos.delivery.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador de autenticación para las vistas web.
 * Maneja el login (Spring Security lo procesa automáticamente)
 * y el registro de nuevos usuarios.
 */
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    // ── Login ──────────────────────────────────────────────────────────────────

    @GetMapping("/login")
    public String loginPage(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            Model model) {

        if (error != null) {
            model.addAttribute("errorMsg", "Usuario o contraseña incorrectos");
        }
        if (logout != null) {
            model.addAttribute("logoutMsg", "Sesión cerrada correctamente");
        }

        return "login";
    }

    // ── Registro ───────────────────────────────────────────────────────────────

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registroDTO", new RegistroDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @ModelAttribute RegistroDTO registroDTO,
            Model model) {
        try {
            usuarioService.registrar(registroDTO);
            return "redirect:/login?registered";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMsg", e.getMessage());
            model.addAttribute("registroDTO", registroDTO);
            return "register";
        }
    }
}
