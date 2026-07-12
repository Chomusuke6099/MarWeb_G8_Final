package com.bembos.delivery.controller;

import com.bembos.delivery.config.JwtUtil;
import com.bembos.delivery.dto.AuthLoginDTO;
import com.bembos.delivery.dto.JwtResponseDTO;
import com.bembos.delivery.dto.RegistroDTO;
import com.bembos.delivery.model.Usuario;
import com.bembos.delivery.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador REST para autenticación JWT (Opcional Avanzado).
 *
 * POST /api/auth/login    → autentica y devuelve un token JWT
 * POST /api/auth/register → registra un usuario y devuelve token JWT
 *
 * Uso: incluir el token en las peticiones API como:
 *   Authorization: Bearer <token>
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ApiAuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;

    // ── POST /api/auth/login ───────────────────────────────────────────────────

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthLoginDTO dto) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );

            Usuario usuario = (Usuario) auth.getPrincipal();
            String token = jwtUtil.generarToken(usuario);

            return ResponseEntity.ok(new JwtResponseDTO(
                    token,
                    usuario.getUsername(),
                    usuario.getRol().name()
            ));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Usuario o contraseña incorrectos"));
        }
    }

    // ── POST /api/auth/register ────────────────────────────────────────────────

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistroDTO dto) {
        try {
            usuarioService.registrar(dto);
            return ResponseEntity.ok(Map.of("mensaje", "Usuario registrado correctamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
