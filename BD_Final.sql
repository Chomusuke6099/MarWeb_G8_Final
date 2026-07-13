-- ==============================================================
--BASE DE DATOS - BEMBOS DELIVERY: Tabla de Usuarios del Sistema
-- ==============================================================

USE bembos_delivery;

-- ──────────────────────────────────────────────────────────────
-- TABLA USUARIO
-- Almacena las cuentas de acceso al sistema.
-- ──────────────────────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS Usuario (
    UsuarioID      INT           AUTO_INCREMENT PRIMARY KEY,
    Username       VARCHAR(50)   NOT NULL UNIQUE,
    Password       VARCHAR(255)  NOT NULL,          -- BCrypt hash
    Email          VARCHAR(100)  NULL,
    Rol            VARCHAR(20)   NOT NULL DEFAULT 'USER',   -- USER | ADMIN
    Activo         TINYINT(1)    NOT NULL DEFAULT 1,
    FechaRegistro  DATETIME      DEFAULT CURRENT_TIMESTAMP
);


-- ──────────────────────────────────────────────────────────────
-- USUARIO ADMINISTRADOR POR DEFECTO
-- Password: admin123  (hash BCrypt generado con strength 10)
-- ──────────────────────────────────────────────────────────────

INSERT INTO Usuario (Username, Password, Email, Rol)
VALUES (
    'admin',
    '$2a$10$zZdavjMRmaA4MpaXgDVqSOI8Q2BijgOteeO7pmSy26qvze8UXkBeO',
    'admin@bembos.com',
    'ADMIN'
);


-- ──────────────────────────────────────────────────────────────
-- PROCEDIMIENTO ALMACENADO FALTANTE
-- ──────────────────────────────────────────────────────────────

DELIMITER //

CREATE PROCEDURE IF NOT EXISTS sp_buscar_productos_por_categoria(
    IN p_categoria VARCHAR(50)
)
BEGIN
    SELECT p.ProductoID, p.Nombre, p.Descripcion, p.Precio,
           p.Imagen, c.Nombre AS Categoria
    FROM Producto p
    INNER JOIN Categoria c ON p.CategoriaID = c.CategoriaID
    WHERE c.Nombre = p_categoria;
END //

DELIMITER ;
