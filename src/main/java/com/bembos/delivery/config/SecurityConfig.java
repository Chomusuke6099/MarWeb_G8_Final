package com.bembos.delivery.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    // Cadena de filtros de seguridad

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // CSRF: desactivado solo para /api/** (llamadas fetch desde JS)
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**")
            )

            // Reglas de autorización
            .authorizeHttpRequests(auth -> auth

                // Recursos estáticos → siempre públicos
                .requestMatchers("/css/**", "/js/**", "/imagenes/**").permitAll()

                // Páginas de autenticación → públicas
                .requestMatchers("/login", "/register", "/recuperar-password", "/recuperar-password/**").permitAll()

                // Catálogo principal → público (cualquiera puede ver el menú)
                .requestMatchers("/").permitAll()

                // API de autenticación JWT → pública (aquí se obtiene el token)
                .requestMatchers("/api/auth/**").permitAll()

                // APIs auxiliares usadas en checkout → públicas
                .requestMatchers("/api/zonas/**", "/api/metodos-pago/**").permitAll()

                // Panel de administración → solo ADMIN
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // Resto de rutas (carrito, checkout, confirmacion, /api/pedidos)
                // → requieren autenticación
                .anyRequest().authenticated()
            )

            // Formulario de login personalizado
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error")
                .permitAll()
            )

            // Logout
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )

            // Manejo de errores de autenticación:
            // API → devuelve JSON 401; rutas web → redirige a /login
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, e) -> {
                    if (request.getRequestURI().startsWith("/api/")) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("application/json;charset=UTF-8");
                        response.getWriter().write("{\"error\":\"No autenticado. Proporciona un token JWT válido.\"}");
                    } else {
                        response.sendRedirect("/login");
                    }
                })
                .accessDeniedHandler((request, response, e) -> {
                    if (request.getRequestURI().startsWith("/api/")) {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.setContentType("application/json;charset=UTF-8");
                        response.getWriter().write("{\"error\":\"Acceso denegado. Se requiere rol ADMIN.\"}");
                    } else {
                        response.sendRedirect("/login?acceso-denegado");
                    }
                })
            )

            // Proveedor de autenticación con BCrypt
            .authenticationProvider(authenticationProvider())

            // Agregar filtro JWT antes del filtro de username/password
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Beans de seguridad

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @SuppressWarnings("deprecation")
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}