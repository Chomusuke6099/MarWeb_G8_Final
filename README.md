# 🍔 Bembos Delivery — Sistema de Pedidos Online

Aplicación web full-stack desarrollada con **Spring Boot + Thymeleaf + MySQL** para la gestión de pedidos de comida rápida con autenticación de usuarios, panel de administración y seguridad con JWT.

> **Curso:** Marcos de Desarrollo Web  
> **Proyecto:** Sistema de Gestión — Entregable Final  
> **Institución:** Universidad de Tecnología del Perú (UTP)

---

## 📋 Tabla de Contenidos

- [Descripción](#descripción)
- [Tecnologías](#tecnologías)
- [Requisitos Previos](#requisitos-previos)
- [Instalación y Configuración](#instalación-y-configuración)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Funcionalidades](#funcionalidades)
- [Seguridad](#seguridad)
- [Endpoints REST (JWT)](#endpoints-rest-jwt)
- [Credenciales de Prueba](#credenciales-de-prueba)
- [Entregables del Proyecto](#entregables-del-proyecto)

---

## Descripción

**Bembos Delivery** es una aplicación web que permite a los usuarios explorar un catálogo de hamburguesas y combos, gestionar un carrito de compras y realizar pedidos con selección de zona de delivery y método de pago. Los administradores cuentan con un panel exclusivo para gestionar productos y usuarios del sistema.

---

## Tecnologías

| Capa | Tecnología |
|------|-----------|
| Backend | Java 21, Spring Boot 3.5 |
| Vistas | Thymeleaf, Thymeleaf Security Extras |
| Seguridad | Spring Security 6, JWT (JJWT 0.12) |
| Persistencia | Spring Data JPA, Hibernate, MySQL 8 |
| Frontend | Bootstrap 5.3, Font Awesome 6.5 |
| Build | Apache Maven |
| Base de Datos | MySQL 8 con Stored Procedures |

---

## Requisitos Previos

Antes de ejecutar el proyecto asegúrate de tener instalado:

- **Java 21** → [Descargar Adoptium](https://adoptium.net)
- **Maven 3.8+** → incluido en IntelliJ o [descargar aquí](https://maven.apache.org)
- **MySQL 8** corriendo en `localhost:3306`
- **IDE:** IntelliJ IDEA o VS Code con extensión Java

Verifica las versiones desde la terminal:
```bash
java -version
mvn -version
```

---

## Instalación y Configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/bembos-delivery.git
cd bembos-delivery
```

### 2. Configurar la base de datos

Abre **MySQL Workbench** y ejecuta los scripts en este orden:

```sql
-- Paso 1: Estructura completa + datos iniciales (Avance 03)
source BASE DE DATOS/BDProyectoSistemaBembos.sql;

-- Paso 2: Tabla de usuarios + admin por defecto (Avance 04)
source BD_Avance04_Seguridad.sql;
```

### 3. Ajustar credenciales de MySQL

Edita `src/main/resources/application.properties` si tu MySQL tiene una contraseña diferente:

```properties
spring.datasource.username=root
spring.datasource.password=TU_CONTRASEÑA
```

### 4. Descargar dependencias

```bash
mvn clean install -DskipTests
```

### 5. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

La aplicación estará disponible en: **http://localhost:8080**

---

## Estructura del Proyecto

```
src/
└── main/
    ├── java/com/bembos/delivery/
    │   ├── config/
    │   │   ├── SecurityConfig.java       # Configuración Spring Security
    │   │   ├── JwtUtil.java              # Generación y validación de tokens JWT
    │   │   └── JwtAuthFilter.java        # Filtro JWT por petición
    │   ├── controller/
    │   │   ├── CatalogoController.java   # GET /
    │   │   ├── CarritoController.java    # GET /carrito
    │   │   ├── CheckoutController.java   # GET /checkout
    │   │   ├── ConfirmacionController.java
    │   │   ├── AuthController.java       # /login, /register, /recuperar-password
    │   │   ├── AdminController.java      # /admin/** (solo ADMIN)
    │   │   ├── ApiAuthController.java    # REST: /api/auth/login, /api/auth/register
    │   │   └── PedidoRestController.java # REST: /api/pedidos
    │   ├── model/
    │   │   ├── Usuario.java              # Entidad usuario (implementa UserDetails)
    │   │   ├── Rol.java                  # Enum: USER, ADMIN
    │   │   ├── Producto.java
    │   │   ├── Categoria.java
    │   │   ├── Pedido.java
    │   │   ├── PedidoItem.java
    │   │   ├── Cliente.java
    │   │   ├── MetodoPago.java
    │   │   └── ZonaDelivery.java
    │   ├── repository/                   # Interfaces JPA + Stored Procedures
    │   ├── service/
    │   │   ├── UsuarioService.java       # UserDetailsService + lógica de usuarios
    │   │   ├── ProductoService.java
    │   │   ├── PedidoService.java
    │   │   └── ClienteService.java
    │   └── dto/                          # Objetos de transferencia de datos
    └── resources/
        ├── templates/                    # Plantillas Thymeleaf
        │   ├── index.html               # Catálogo de productos
        │   ├── carrito.html
        │   ├── checkout.html
        │   ├── confirmacion.html
        │   ├── login.html
        │   ├── register.html
        │   ├── recuperar-password.html
        │   └── admin/
        │       ├── dashboard.html
        │       ├── usuarios.html
        │       ├── productos.html
        │       └── producto-form.html
        ├── static/
        │   ├── css/style.css
        │   ├── js/
        │   │   ├── common.js            # Carrito, notificaciones, utilidades
        │   │   ├── catalogo.js
        │   │   ├── carrito.js
        │   │   ├── checkout.js
        │   │   └── confirmacion.js
        │   └── imagenes/
        └── application.properties
```

---

## Funcionalidades

### Usuarios (Clientes)
- 🔍 Explorar catálogo de productos con imágenes y precios
- 🛒 Carrito de compras persistente e **independiente por cuenta de usuario**
- 📦 Proceso de checkout con selección de zona de delivery y método de pago
- ✅ Página de confirmación de pedido con resumen completo
- 👤 Registro de nuevos usuarios (`/register`)
- 🔑 Inicio y cierre de sesión (`/login`, `/logout`)
- 🔐 Recuperación de contraseña sin necesidad de email (`/recuperar-password`)

### Administradores
- 📊 Dashboard con estadísticas: total de pedidos, productos, clientes y usuarios
- 🍔 **CRUD completo de Productos** — crear, ver, editar y eliminar
- 👥 **Gestión de Usuarios** — cambiar rol USER↔ADMIN, activar/desactivar, eliminar
- 🔒 Acceso exclusivo a `/admin/**` restringido por rol

---

## Seguridad

La aplicación implementa una arquitectura de seguridad dual:

### Autenticación Web (sesión)
- Formulario de login en `/login` procesado por Spring Security
- Sesiones con cookies `JSESSIONID`
- Rutas protegidas redirigen a `/login` si no hay sesión activa
- CSRF activado para todas las rutas web (tokens automáticos via `th:action`)

### Autenticación API (JWT)
- Tokens generados con algoritmo **HS256** y expiración de 24 horas
- El filtro `JwtAuthFilter` valida el token en cada petición a `/api/**`
- CSRF desactivado para rutas `/api/**` (stateless por diseño)

### Control de Acceso por Roles

| Ruta | Acceso |
|------|--------|
| `/`, `/login`, `/register`, `/recuperar-password/**` | Público |
| `/css/**`, `/js/**`, `/imagenes/**` | Público |
| `/api/auth/**`, `/api/zonas/**`, `/api/metodos-pago/**` | Público |
| `/carrito`, `/checkout`, `/confirmacion` | Autenticado |
| `/api/pedidos/**` | Autenticado |
| `/admin/**` | Solo `ROLE_ADMIN` |

---

## Endpoints REST (JWT)

Útiles para probar con Postman o cualquier cliente HTTP.

### Registrar usuario
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "nuevo_usuario",
  "email": "correo@ejemplo.com",
  "password": "mipassword",
  "confirmarPassword": "mipassword"
}
```

### Obtener token JWT
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```
**Respuesta:**
```json
{
  "token": "eyJhbGci...",
  "tipo": "Bearer",
  "username": "admin",
  "rol": "ADMIN"
}
```

### Usar el token en peticiones protegidas
```http
POST /api/pedidos/crear
Authorization: Bearer eyJhbGci...
Content-Type: application/json
```

---

## Credenciales de Prueba

| Rol | Usuario | Contraseña | Acceso |
|-----|---------|------------|--------|
| ADMIN | `admin` | `admin123` | Panel completo `/admin/**` |
| USER | Crear en `/register` | La que elijas | Catálogo, carrito, checkout |

> ℹ️ El usuario administrador se crea automáticamente al ejecutar `BD_Avance04_Seguridad.sql`.

---

## Entregables del Proyecto

| Entregable | Descripción | Estado |
|-----------|-------------|--------|
| **Avance 01** | Interfaz de usuario con Bootstrap | ✅ Completado |
| **Avance 02** | Backend básico con Spring Boot y Thymeleaf | ✅ Completado |
| **Avance 03** | Conexión a base de datos MySQL y CRUD con JPA | ✅ Completado |
| **Avance Final** | Seguridad con Spring Security y JWT | ✅ Completado |

---

## Base de Datos

El esquema incluye las siguientes tablas principales:

`Categoria` · `Producto` · `Cliente` · `Pedido` · `Pedido_Item` · `Metodo_Pago` · `Zona_Delivery` · `Usuario`

Las operaciones de consulta utilizan **Stored Procedures** en MySQL. Las operaciones de escritura del módulo de seguridad usan JPA directamente.

---

<div align="center">
  <p>© 2026 Bembos Delivery — Proyecto de Ingeniería de Sistemas</p>
  <p>Desarrollado para el curso de Marcos de Desarrollo Web · UTP</p>
</div>
