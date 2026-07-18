-- ====================================================================
-- BASE DE DATOS - BEMBOS DELIVERY
-- ====================================================================

DROP DATABASE IF EXISTS bembos_delivery;
CREATE DATABASE IF NOT EXISTS bembos_delivery;
USE bembos_delivery;

-- ====================================================================
-- TABLAS
-- ====================================================================

-- 1. ZONAS DE DELIVERY
CREATE TABLE Zona_Delivery (
    ZonaID INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(50) NOT NULL,
    Palabras_Clave TEXT NOT NULL,
    Costo DECIMAL(10,2) NOT NULL,
    TiempoEstimado VARCHAR(50) DEFAULT '30-45 minutos'
);

-- 2. CLIENTES
CREATE TABLE Cliente (
    ClienteID INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(100) NOT NULL,
    Telefono VARCHAR(15) NOT NULL UNIQUE,
    Email VARCHAR(100) NULL,
    Direccion VARCHAR(255) NOT NULL,
    Referencia VARCHAR(255) NULL,
    ZonaID INT NULL,
    FechaRegistro DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cliente_zona FOREIGN KEY (ZonaID) REFERENCES Zona_Delivery(ZonaID) ON DELETE SET NULL
);

-- 3. CATEGORIAS
CREATE TABLE Categoria (
    CategoriaID INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(50) NOT NULL UNIQUE
);

-- 4. PRODUCTOS
CREATE TABLE Producto (
    ProductoID INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(100) NOT NULL,
    Descripcion VARCHAR(500),
    Precio DECIMAL(10,2) NOT NULL,
    Imagen VARCHAR(255),
    CategoriaID INT NOT NULL,
    CONSTRAINT fk_producto_categoria FOREIGN KEY (CategoriaID) REFERENCES Categoria(CategoriaID)
);

-- 5. METODOS DE PAGO
CREATE TABLE Metodo_Pago (
    MetodoPagoID INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(50) NOT NULL UNIQUE
);

-- 6. PEDIDOS
CREATE TABLE Pedido (
    PedidoID INT AUTO_INCREMENT PRIMARY KEY,
    Fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    ClienteID INT NULL,
    MetodoPagoID INT NOT NULL,
    Estado VARCHAR(20) NOT NULL DEFAULT 'Pendiente',
    Subtotal DECIMAL(10,2) NOT NULL,
    Delivery DECIMAL(10,2) NOT NULL,
    Total DECIMAL(10,2) NOT NULL,
    TiempoEstimado VARCHAR(50) NULL,
    NumeroOperacion VARCHAR(100) NULL,
    FechaConfirmacion DATETIME NULL,
    CONSTRAINT fk_pedido_cliente FOREIGN KEY (ClienteID) REFERENCES Cliente(ClienteID) ON DELETE SET NULL,
    CONSTRAINT fk_pedido_metodopago FOREIGN KEY (MetodoPagoID) REFERENCES Metodo_Pago(MetodoPagoID)
);

-- 7. ITEMS DEL PEDIDO
CREATE TABLE Pedido_Item (
    PedidoItemID INT AUTO_INCREMENT PRIMARY KEY,
    PedidoID INT NOT NULL,
    ProductoID INT NOT NULL,
    Cantidad INT NOT NULL,
    PrecioUnitario DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_pedidoitem_pedido FOREIGN KEY (PedidoID) REFERENCES Pedido(PedidoID) ON DELETE CASCADE,
    CONSTRAINT fk_pedidoitem_producto FOREIGN KEY (ProductoID) REFERENCES Producto(ProductoID)
);

-- 8. USUARIOS DEL SISTEMA
CREATE TABLE IF NOT EXISTS Usuario (
    UsuarioID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(50) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    Email VARCHAR(100) NULL,
    Rol VARCHAR(20) NOT NULL DEFAULT 'USER',
    Activo TINYINT(1) NOT NULL DEFAULT 1,
    FechaRegistro DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ====================================================================
-- DATOS INICIALES
-- ====================================================================

INSERT INTO Zona_Delivery (Nombre, Palabras_Clave, Costo, TiempoEstimado) VALUES
('Zona Norte', 'José Leonardo Ortiz, Santa Victoria, La Victoria norte, Av. José Leonardo Ortiz', 5.00, '35-45 minutos'),
('Zona Centro', 'Chiclayo centro, Av. Grau, Av. Balta, Centro cívico, Calle San José, Mercado modelo', 4.50, '25-35 minutos'),
('Zona Sur', 'La Victoria sur, Ciudad Jardin, Los Parques, Urb. Santa Maria, Av. Salaverry', 5.50, '35-45 minutos'),
('Zona Oeste', 'Monsefú, Eten, Puerto Eten, Santa Rosa', 6.50, '45-55 minutos'),
('Zona Este', 'Picsi, Cayaltí, Pátapo, Tumán, Av. Circunvalación este', 6.00, '40-50 minutos');

INSERT INTO Metodo_Pago (Nombre) VALUES
('Yape'),
('Plin');

INSERT INTO Categoria (Nombre) VALUES
('Hamburguesas'),
('Combos'),
('Pollos'),
('Complementos'),
('Bebidas'),
('Postres');

INSERT INTO Producto (Nombre, Descripcion, Precio, Imagen, CategoriaID) VALUES
('Hamburguesa Clásica', 'Deliciosa hamburguesa con carne de res, lechuga, tomate y salsa especial', 15.90, 'imagenes/Hamburguesa_clasica.jpg', 1),
('Combo Familiar', '2 hamburguesas clásicas, papas fritas grandes y 2 bebidas', 35.50, 'imagenes/Combo_familiar.jpg', 2),
('Pollo Crispy', 'Trozos de pollo crujiente con salsa BBQ o mayonesa', 18.90, 'imagenes/Pollo_crispy.jpg', 3),
('Papas Fritas Grandes', 'Porción grande de papas fritas doradas y crujientes', 8.50, 'imagenes/Papas_fritas_grandes.jpg', 4),
('Bebida Grande', 'Refresco de 500ml (Inca Kola, Coca Cola, Sprite)', 5.00, 'imagenes/Bebida_grande.jpg', 5),
('Helado Sundae', 'Helado con topping de chocolate y nueces', 12.00, 'imagenes/Helado_sundae.jpg', 6);

INSERT INTO Usuario (Username, Password, Email, Rol) VALUES 
('admin', '$2a$10$zZdavjMRmaA4MpaXgDVqSOI8Q2BijgOtee07pmSy26qvze8UXkBeO', 'admin@bembos.com', 'ADMIN');

-- ====================================================================
-- PROCEDIMIENTOS ALMACENADOS
-- ====================================================================

DELIMITER //

-- ZONAS DELIVERY
CREATE PROCEDURE sp_calcular_zona_por_direccion (IN p_direccion VARCHAR(255))
BEGIN
    SELECT ZonaID, Costo, Nombre, TiempoEstimado
    FROM Zona_Delivery
    WHERE p_direccion LIKE CONCAT('%', Palabras_Clave, '%')
       OR Palabras_Clave LIKE CONCAT('%', p_direccion, '%')
    LIMIT 1;
END //

CREATE PROCEDURE sp_listar_zonas_delivery()
BEGIN
    SELECT ZonaID, Nombre, Costo, TiempoEstimado
    FROM Zona_Delivery
    ORDER BY Costo ASC;
END //

CREATE PROCEDURE sp_obtener_costo_delivery(IN p_zonaId INT)
BEGIN
    SELECT Costo, TiempoEstimado FROM Zona_Delivery WHERE ZonaID = p_zonaId;
END //

-- PRODUCTOS
CREATE PROCEDURE sp_listar_productos()
BEGIN
    SELECT ProductoID, Nombre, Descripcion, Precio, Imagen, CategoriaID 
    FROM Producto;
END //

CREATE PROCEDURE sp_obtener_producto_por_id(IN p_productoId INT)
BEGIN
    SELECT ProductoID, Nombre, Descripcion, Precio, Imagen, CategoriaID
    FROM Producto
    WHERE ProductoID = p_productoId;
END //

-- CLIENTES
CREATE PROCEDURE sp_guardar_cliente(
    IN p_nombre VARCHAR(100),
    IN p_telefono VARCHAR(15),
    IN p_email VARCHAR(100),
    IN p_direccion VARCHAR(255),
    IN p_referencia VARCHAR(255),
    IN p_zonaId INT
)
BEGIN
    DECLARE v_clienteId INT;
    SELECT ClienteID INTO v_clienteId FROM Cliente WHERE Telefono = p_telefono LIMIT 1;
    
    IF v_clienteId IS NULL THEN
        INSERT INTO Cliente (Nombre, Telefono, Email, Direccion, Referencia, ZonaID)
        VALUES (p_nombre, p_telefono, p_email, p_direccion, p_referencia, p_zonaId);
        SET v_clienteId = LAST_INSERT_ID();
    ELSE
        UPDATE Cliente
        SET Nombre = p_nombre,
            Email = p_email,
            Direccion = p_direccion,
            Referencia = p_referencia,
            ZonaID = p_zonaId
        WHERE ClienteID = v_clienteId;
    END IF;
    SELECT v_clienteId AS ClienteID;
END //

CREATE PROCEDURE sp_buscar_cliente_por_telefono(IN p_telefono VARCHAR(15))
BEGIN
    SELECT ClienteID, Nombre, Telefono, Email, Direccion, Referencia, ZonaID
    FROM Cliente
    WHERE Telefono = p_telefono;
END //

-- PEDIDOS
CREATE PROCEDURE sp_crear_pedido(
    IN p_clienteId INT,
    IN p_metodoPagoId INT,
    IN p_subtotal DECIMAL(10,2),
    IN p_delivery DECIMAL(10,2),
    IN p_total DECIMAL(10,2),
    IN p_tiempoEstimado VARCHAR(50),
    IN p_numeroOperacion VARCHAR(100)
)
BEGIN
    INSERT INTO Pedido (
        ClienteID, MetodoPagoID, Estado, Subtotal, Delivery,
        Total, TiempoEstimado, NumeroOperacion
    ) VALUES (
        p_clienteId, p_metodoPagoId, 'Pendiente', p_subtotal, p_delivery,
        p_total, p_tiempoEstimado, p_numeroOperacion
    );
    SELECT LAST_INSERT_ID() AS PedidoID;
END //

CREATE PROCEDURE sp_agregar_item_pedido(
    IN p_pedidoId INT,
    IN p_productoId INT,
    IN p_cantidad INT,
    IN p_precioUnitario DECIMAL(10,2)
)
BEGIN
    INSERT INTO Pedido_Item (PedidoID, ProductoID, Cantidad, PrecioUnitario)
    VALUES (p_pedidoId, p_productoId, p_cantidad, p_precioUnitario);
END //

CREATE PROCEDURE sp_confirmar_pedido (IN p_pedidoId INT)
BEGIN
    UPDATE Pedido
    SET Estado = 'Confirmado',
        FechaConfirmacion = NOW()
    WHERE PedidoID = p_pedidoId;
END //

CREATE PROCEDURE sp_obtener_pedido_completo (IN p_pedidoId INT)
BEGIN
    SELECT p.PedidoID, p.Fecha, p.Estado, p.Subtotal, p.Delivery, p.Total,
           p.TiempoEstimado, p.NumeroOperacion,
           mp.Nombre AS MetodoPago,
           c.Nombre AS ClienteNombre, c.Telefono, c.Direccion, c.Referencia,
           z.Nombre AS Zona, z.Costo AS DeliveryCosto
    FROM Pedido p
    LEFT JOIN Cliente c ON p.ClienteID = c.ClienteID
    LEFT JOIN Zona_Delivery z ON c.ZonaID = z.ZonaID
    LEFT JOIN Metodo_Pago mp ON p.MetodoPagoID = mp.MetodoPagoID
    WHERE p.PedidoID = p_pedidoId;
END //

CREATE PROCEDURE sp_listar_items_por_pedido(IN p_pedidoId INT)
BEGIN
    SELECT pi.PedidoItemID, pr.Nombre AS Producto, pi.Cantidad,
           pi.PrecioUnitario, (pi.Cantidad * pi.PrecioUnitario) AS Importe
    FROM Pedido_Item pi
    INNER JOIN Producto pr ON pi.ProductoID = pr.ProductoID
    WHERE pi.PedidoID = p_pedidoId;
END //

-- MÉTODOS DE PAGO
CREATE PROCEDURE sp_listar_metodos_pago()
BEGIN
    SELECT MetodoPagoID, Nombre FROM Metodo_Pago;
END //

-- PROCEDIMIENTO ADICIONAL
CREATE PROCEDURE sp_buscar_productos_por_categoria(
    IN p_categoria VARCHAR(50)
)
BEGIN
    SELECT p.ProductoID, p.Nombre, p.Descripcion, p.Precio, p.Imagen, p.CategoriaID
    FROM Producto p
    INNER JOIN Categoria c ON p.CategoriaID = c.CategoriaID
    WHERE c.Nombre = p_categoria;
END //

DELIMITER ;