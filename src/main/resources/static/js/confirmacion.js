document.addEventListener('DOMContentLoaded', () => {
    initConfirmacion();
});

function initConfirmacion() {
    const pedido = getPedido();
    if (!pedido) {
        redirigirCatalogo();
        return;
    }
    renderCliente(pedido);
    renderProductos(pedido);
    renderTotales(pedido);
    renderExtras(pedido);
}

function getPedido() {
    try {
        return JSON.parse(localStorage.getItem('ultimoPedido'));
    } catch {
        return null;
    }
}

function redirigirCatalogo() {
    mostrarNotificacion('No hay pedido registrado', 'error');
    setTimeout(() => {
        window.location.href = '/';
    }, 1000);
}

function renderCliente(pedido) {
    setText('cliente-nombre', pedido.cliente.nombre);
    setText('cliente-telefono', pedido.cliente.telefono);
    setText('cliente-direccion', pedido.cliente.direccion);
    
    let zonaTexto = 'No especificada';
    if (pedido.cliente.zonaNombre) {
        zonaTexto = pedido.cliente.zonaNombre;
    } else if (pedido.cliente.zonaId) {
        zonaTexto = `Zona ID: ${pedido.cliente.zonaId}`;
    }
    setText('cliente-zona', zonaTexto);
}

function renderProductos(pedido) {
    const tbody = document.getElementById('productos-confirmacion');
    if (!tbody) return;

    tbody.innerHTML = '';
    
    if (!pedido.productos || pedido.productos.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" class="text-center text-muted">No hay productos</td></tr>';
        return;
    }

    let contador = 1;
    pedido.productos.forEach(item => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td class="text-center">${contador++}</td>
            <td><strong>${item.nombre}</strong></td>
            <td class="text-center">${item.cantidad}</td>
            <td class="text-end">${formatPrecio(item.precio)}</td>
            <td class="text-end fw-semibold">${formatPrecio(item.precio * item.cantidad)}</td>
        `;
        tbody.appendChild(tr);
    });
}

function renderTotales(pedido) {
    setText('subtotal-confirmacion', formatPrecio(pedido.totales.subtotal));
    setText('delivery-confirmacion', formatPrecio(pedido.totales.delivery));
    setText('total-confirmacion', formatPrecio(pedido.totales.total));
}

function renderExtras(pedido) {
    const metodoTexto = pedido.pago.metodo === 'tarjeta' ? 'Tarjeta de Crédito/Débito' : pedido.pago.metodo;
    setText('metodo-pago', metodoTexto);
    setText('tiempo-entrega', pedido.tiempoEstimado || '30-45 minutos');
    
    if (pedido.numeroOperacion) {
        const opElement = document.getElementById('numero-operacion-confirmacion');
        if (opElement) {
            opElement.innerHTML = `<hr class="my-2"><strong>N° Operación:</strong> ${pedido.numeroOperacion}`;
        }
    }
}

function setText(id, value) {
    const el = document.getElementById(id);
    if (el) el.textContent = value;
}

function mostrarNotificacion(mensaje, tipo = 'success') {
    const notif = document.createElement('div');
    notif.className = `custom-toast ${tipo}`;
    notif.innerHTML = `<i class="fas ${tipo === 'success' ? 'fa-check-circle' : 'fa-exclamation-circle'} me-2"></i>${mensaje}`;
    document.body.appendChild(notif);
    setTimeout(() => notif.classList.add('show'), 100);
    setTimeout(() => {
        notif.classList.remove('show');
        setTimeout(() => notif.remove(), 300);
    }, 2500);
}


