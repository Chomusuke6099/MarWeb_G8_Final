function getCarritoKey() {
    const user = (typeof USUARIO_ACTUAL !== 'undefined' && USUARIO_ACTUAL)
        ? USUARIO_ACTUAL
        : 'anonimo';
    return 'carrito_' + user;
}

// Zonas de delivery
const zonasDelivery = {
    "Centro": 5.00,
    "Norte":  8.00,
    "Sur":    7.00,
    "Este":   6.50,
    "Oeste":  6.50
};

// Carrito (localStorage con clave por usuario)

function getCarrito() {
    try {
        return JSON.parse(localStorage.getItem(getCarritoKey())) || [];
    } catch {
        return [];
    }
}

function saveCarrito(carrito) {
    localStorage.setItem(getCarritoKey(), JSON.stringify(carrito));
    updateCartCount();
}

function addToCarrito(productoId, cantidad, nombre, precio, imagen) {
    const carrito = getCarrito();
    const item = carrito.find(i => i.id === productoId);

    if (item) {
        item.cantidad += cantidad;
    } else {
        carrito.push({ id: productoId, nombre, precio, cantidad, imagen });
    }

    saveCarrito(carrito);
    mostrarNotificacion(`${nombre} agregado`, 'success');
}

function removeFromCarrito(productoId) {
    saveCarrito(getCarrito().filter(i => i.id !== productoId));
}

function updateCantidad(productoId, cantidad) {
    const carrito = getCarrito();
    const item = carrito.find(i => i.id === productoId);
    if (!item) return;

    if (cantidad <= 0) {
        removeFromCarrito(productoId);
        return;
    }
    item.cantidad = cantidad;
    saveCarrito(carrito);
}

// Totales

function getTotalCarrito() {
    return getCarrito().reduce((t, i) => t + i.precio * i.cantidad, 0);
}

function getCantidadTotal() {
    return getCarrito().reduce((t, i) => t + i.cantidad, 0);
}

function updateCartCount() {
    const el = document.getElementById('cart-count');
    if (el) el.textContent = getCantidadTotal();
}

// Notificaciones

function mostrarNotificacion(mensaje, tipo = 'success') {
    const notif = document.createElement('div');
    notif.className = `custom-toast ${tipo}`;
    notif.innerHTML = `<div class="toast-body">
        <i class="fas fa-check-circle me-2"></i>${mensaje}
    </div>`;
    document.body.appendChild(notif);
    setTimeout(() => notif.classList.add('show'), 100);
    setTimeout(() => {
        notif.classList.remove('show');
        setTimeout(() => notif.remove(), 300);
    }, 2500);
}

// Formato de precios

function formatPrecio(precio) {
    return `S/ ${precio.toFixed(2)}`;
}

document.addEventListener('DOMContentLoaded', updateCartCount);