let productoAEliminar = null;

document.addEventListener('DOMContentLoaded', function () {
    renderCarrito();
});

function renderCarrito() {
    const carrito = getCarrito();
    const carritoVacio = document.getElementById('carrito-vacio');
    const carritoContenido = document.getElementById('carrito-contenido');
    const carritoItems = document.getElementById('carrito-items');

    if (carrito.length === 0) {
        carritoVacio.style.display = 'block';
        carritoContenido.style.display = 'none';
        return;
    }

    carritoVacio.style.display = 'none';
    carritoContenido.style.display = 'block';
    carritoItems.innerHTML = '';

    carrito.forEach(item => {
        carritoItems.appendChild(crearItem(item));
    });

    actualizarResumen();
}

function crearItem(item) {
    const div = document.createElement('div');
    div.className = 'cart-item d-flex align-items-center';

    div.innerHTML = `
        <img src="${item.imagen}" class="me-3 rounded shadow-sm"
             style="width: 80px; height: 80px; object-fit: cover;">
        <div class="flex-grow-1">
            <h6 class="mb-1 fw-bold">${item.nombre}</h6>
            <small class="text-muted">${formatPrecio(item.precio)}</small>
            <div class="d-flex align-items-center mt-2 gap-2">
                <button class="btn btn-sm btn-outline-secondary rounded-circle d-flex align-items-center justify-content-center" 
                        style="width: 32px; height: 32px; font-weight: bold; font-size: 1.2rem; padding: 0;"
                        onclick="cambiarCantidad(${item.id}, ${item.cantidad - 1})">
                    -
                </button>
                <span class="fw-bold" style="min-width: 30px; text-align: center;">${item.cantidad}</span>
                <button class="btn btn-sm btn-outline-secondary rounded-circle d-flex align-items-center justify-content-center" 
                        style="width: 32px; height: 32px; font-weight: bold; font-size: 1.2rem; padding: 0;"
                        onclick="cambiarCantidad(${item.id}, ${item.cantidad + 1})">
                    +
                </button>
                <button class="btn btn-sm btn-outline-danger ms-2"
                        onclick="abrirModalEliminar(${item.id})">
                    <i class="fas fa-trash me-1"></i>Eliminar
                </button>
            </div>
        </div>
        <div class="text-end">
            <strong class="text-primary">${formatPrecio(item.precio * item.cantidad)}</strong>
        </div>
    `;

    return div;
}

function cambiarCantidad(productoId, nuevaCantidad) {
    if (nuevaCantidad <= 0) return;
    updateCantidad(productoId, nuevaCantidad);
    renderCarrito();
}

function abrirModalEliminar(productoId) {
    productoAEliminar = productoId;
    const modal = new bootstrap.Modal(document.getElementById('modalConfirmacion'));
    modal.show();
}

document.addEventListener('DOMContentLoaded', function () {
    const btnConfirmar = document.getElementById('btn-confirmar-eliminar');
    if (btnConfirmar) {
        btnConfirmar.addEventListener('click', function () {
            if (productoAEliminar !== null) {
                removeFromCarrito(productoAEliminar);
                productoAEliminar = null;
                const modal = bootstrap.Modal.getInstance(document.getElementById('modalConfirmacion'));
                modal.hide();
                renderCarrito();
            }
        });
    }
});

function actualizarResumen() {
    const subtotal = getTotalCarrito();
    document.getElementById('subtotal').textContent = formatPrecio(subtotal);
    document.getElementById('delivery').textContent = formatPrecio(0);
    document.getElementById('total').textContent = formatPrecio(subtotal);
}


