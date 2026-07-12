package com.bembos.delivery.controller;

import com.bembos.delivery.model.Rol;
import com.bembos.delivery.repository.ClienteRepository;
import com.bembos.delivery.repository.PedidoRepository;
import com.bembos.delivery.repository.ProductoRepository;
import com.bembos.delivery.repository.UsuarioRepository;
import com.bembos.delivery.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador del panel de administración.
 * Accesible únicamente para usuarios con rol ADMIN.
 * La restricción se aplica tanto aquí (@PreAuthorize)
 * como en SecurityConfig (hasRole("ADMIN")).
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;

    // ── Dashboard ──────────────────────────────────────────────────────────────

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("totalPedidos",   pedidoRepository.count());
        model.addAttribute("totalProductos", productoRepository.count());
        model.addAttribute("totalClientes",  clienteRepository.count());
        model.addAttribute("totalUsuarios",  usuarioRepository.count());
        model.addAttribute("usuarios",       usuarioService.listarTodos());
        return "admin/dashboard";
    }

    // ── Gestión de usuarios ────────────────────────────────────────────────────

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "admin/usuarios";
    }

    @PostMapping("/usuarios/{id}/rol")
    public String cambiarRol(
            @PathVariable Integer id,
            @RequestParam Rol rol) {
        usuarioService.cambiarRol(id, rol);
        return "redirect:/admin/usuarios?actualizado";
    }

    @PostMapping("/usuarios/{id}/toggle")
    public String toggleActivo(@PathVariable Integer id) {
        usuarioService.toggleActivo(id);
        return "redirect:/admin/usuarios";
    }
}
