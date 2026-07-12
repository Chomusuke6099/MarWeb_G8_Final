let zonasDelivery = [];
let deliveryCosto = 0;


// ZONAS DE DELIVERY
function cargarZonasDelivery() {
    fetch('/api/zonas')
        .then(response => response.json())
        .then(data => {
            zonasDelivery = data;
            actualizarSelectZonas();
        })
        .catch(error => console.error('Error cargando zonas:', error));
}

function actualizarSelectZonas() {
    const zonaSelect = document.getElementById('zona');
    if (!zonaSelect) return;
    
    zonaSelect.innerHTML = '<option value="">Selecciona tu zona</option>';
    
    for (const zona of zonasDelivery) {
        const option = document.createElement('option');
        option.value = zona.id;
        option.setAttribute('data-costo', zona.costo);
        option.setAttribute('data-tiempo', zona.tiempoEstimado);
        option.textContent = `${zona.nombre} - S/ ${parseFloat(zona.costo).toFixed(2)}`;
        zonaSelect.appendChild(option);
    }
}


// EVENTOS CUANDO SE SELECCIONA UNA ZONA
function initZonaEvent() {
    const zonaSelect = document.getElementById('zona');
    if (!zonaSelect) return;
    
    zonaSelect.addEventListener('change', function() {
        const selectedOption = this.options[this.selectedIndex];
        if (this.value) {
            deliveryCosto = parseFloat(selectedOption.getAttribute('data-costo'));
            const tiempoEstimado = selectedOption.getAttribute('data-tiempo');
            document.getElementById('costo-delivery').value = `S/ ${deliveryCosto.toFixed(2)} (${tiempoEstimado})`;
        } else {
            deliveryCosto = 0;
            document.getElementById('costo-delivery').value = '';
        }
        
        if (typeof actualizarTotales === 'function') {
            actualizarTotales();
        }
    });
}


// MÉTODOS DE PAGO
function cargarMetodosPago() {
    return fetch('/api/metodos-pago')
        .then(response => response.json())
        .then(data => {
            window.metodosPago = data;
            return data;
        })
        .catch(error => console.error('Error cargando métodos de pago:', error));
}


// CARRITO (localStorage)
function getCarrito() {
    try {
        return JSON.parse(localStorage.getItem('carrito')) || [];
    } catch {
        return [];
    }
}

function saveCarrito(carrito) {
    localStorage.setItem('carrito', JSON.stringify(carrito));
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
    const nuevo = getCarrito().filter(item => item.id !== productoId);
    saveCarrito(nuevo);
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


// NOTIFICACIONES
function mostrarNotificacion(mensaje, tipo = 'success') {
    const notif = document.createElement('div');
    notif.className = `custom-toast ${tipo}`;
    notif.innerHTML = `
        <div class="toast-body">
            <i class="fas fa-check-circle me-2"></i>
            ${mensaje}
        </div>
    `;

    document.body.appendChild(notif);

    setTimeout(() => notif.classList.add('show'), 100);

    setTimeout(() => {
        notif.classList.remove('show');
        setTimeout(() => notif.remove(), 300);
    }, 2500);
}


// FORMATO DE PRECIOS
function formatPrecio(precio) {
    return `S/ ${precio.toFixed(2)}`;
}


// INICIALIZACIÓN
document.addEventListener('DOMContentLoaded', function() {
    cargarZonasDelivery();
    cargarMetodosPago();
    updateCartCount();
    initZonaEvent();
});


