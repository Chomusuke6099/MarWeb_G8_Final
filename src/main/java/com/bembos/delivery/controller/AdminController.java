package com.bembos.delivery.controller;

import com.bembos.delivery.model.Producto;
import com.bembos.delivery.model.Rol;
import com.bembos.delivery.repository.*;
import com.bembos.delivery.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@SuppressWarnings("null")
public class AdminController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;
    private final CategoriaRepository categoriaRepository;

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

    // ── CRUD Usuarios ──────────────────────────────────────────────────────────

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "admin/usuarios";
    }

    @PostMapping("/usuarios/{id}/rol")
    public String cambiarRol(@PathVariable Integer id, @RequestParam Rol rol) {
        usuarioService.cambiarRol(id, rol);
        return "redirect:/admin/usuarios?actualizado";
    }

    @PostMapping("/usuarios/{id}/toggle")
    public String toggleActivo(@PathVariable Integer id) {
        usuarioService.toggleActivo(id);
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/usuarios/{id}/eliminar")
    public String eliminarUsuario(@PathVariable Integer id) {
        usuarioService.eliminar(id);
        return "redirect:/admin/usuarios?eliminado";
    }

    // ── CRUD Productos ─────────────────────────────────────────────────────────

    @GetMapping("/productos")
    public String listarProductos(Model model) {
        model.addAttribute("productos",   productoRepository.listarProductos());
        model.addAttribute("categorias",  categoriaRepository.findAll());
        return "admin/productos";
    }

    @GetMapping("/productos/nuevo")
    public String nuevoProducto(Model model) {
        model.addAttribute("producto",   new Producto());
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("accion",     "Nuevo");
        return "admin/producto-form";
    }

    @PostMapping("/productos/guardar")
    public String guardarProducto(@ModelAttribute Producto producto) {
        productoRepository.save(producto);
        return "redirect:/admin/productos?guardado";
    }

    @GetMapping("/productos/{id}/editar")
    public String editarProducto(@PathVariable Integer id, Model model) {
        Producto p = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        model.addAttribute("producto",   p);
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("accion",     "Editar");
        return "admin/producto-form";
    }

    @PostMapping("/productos/{id}/eliminar")
    public String eliminarProducto(@PathVariable Integer id) {
        productoRepository.deleteById(id);
        return "redirect:/admin/productos?eliminado";
    }
}