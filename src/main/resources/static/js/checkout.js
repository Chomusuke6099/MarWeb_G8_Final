document.addEventListener('DOMContentLoaded', function() {
    initCheckout();
    initMetodosPago();
    initValidaciones(); 
});


// VALIDACIONES EN TIEMPO REAL
function initValidaciones() {
    // Teléfono
    const telefonoInput = document.getElementById('telefono');
    if (telefonoInput) {
        telefonoInput.addEventListener('input', function(e) {
            this.value = this.value.replace(/[^0-9]/g, '').slice(0, 9);
            validarCampo(this, /^[0-9]{9}$/, 'Ingresa un teléfono válido de 9 dígitos');
        });
        telefonoInput.addEventListener('blur', function() {
            validarCampo(this, /^[0-9]{9}$/, 'Ingresa un teléfono válido de 9 dígitos');
        });
    }
    
    // Nombre
    const nombreInput = document.getElementById('nombre');
    if (nombreInput) {
        nombreInput.addEventListener('input', function(e) {
            this.value = this.value.replace(/[^a-zA-ZáéíóúñÁÉÍÓÚÑ\s]/g, '');
            validarCampo(this, /^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]{3,}$/, 'Ingresa un nombre válido (mínimo 3 caracteres)');
        });
        nombreInput.addEventListener('blur', function() {
            validarCampo(this, /^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]{3,}$/, 'Ingresa un nombre válido (mínimo 3 caracteres)');
        });
    }
    
    // Dirección
    const direccionInput = document.getElementById('direccion');
    if (direccionInput) {
        direccionInput.addEventListener('blur', function() {
            validarCampo(this, /^.{5,}$/, 'Ingresa una dirección válida (mínimo 5 caracteres)');
        });
    }
    
    // Zona
    const zonaSelect = document.getElementById('zona');
    if (zonaSelect) {
        zonaSelect.addEventListener('change', function() {
            validarSelect(this, 'Selecciona una zona de delivery');
        });
    }
    
    // Número de operación
    const numeroOpInput = document.getElementById('numero-operacion');
    if (numeroOpInput) {
        numeroOpInput.addEventListener('input', function(e) {
            this.value = this.value.replace(/[^0-9]/g, '').slice(0, 20);
        });
    }
}


// Funciones auxiliares de validación
function validarCampo(input, regex, mensaje) {
    const valor = input.value.trim();
    if (!regex.test(valor)) {
        input.classList.add('is-invalid');
        input.classList.remove('is-valid');
        mostrarError(input, mensaje);
        return false;
    } else {
        input.classList.remove('is-invalid');
        input.classList.add('is-valid');
        ocultarError(input);
        return true;
    }
}

function validarSelect(select, mensaje) {
    if (!select.value) {
        select.classList.add('is-invalid');
        mostrarError(select, mensaje);
        return false;
    } else {
        select.classList.remove('is-invalid');
        select.classList.add('is-valid');
        ocultarError(select);
        return true;
    }
}

function mostrarError(input, mensaje) {
    let errorDiv = input.parentElement.querySelector('.error-message');
    if (!errorDiv) {
        errorDiv = document.createElement('div');
        errorDiv.className = 'error-message';
        input.parentElement.appendChild(errorDiv);
    }
    errorDiv.innerHTML = `<i class="fas fa-exclamation-circle"></i> ${mensaje}`;
}

function ocultarError(input) {
    const errorDiv = input.parentElement.querySelector('.error-message');
    if (errorDiv) {
        errorDiv.remove();
    }
}


// MÉTODOS DE PAGO
function initMetodosPago() {
    const container = document.getElementById('metodos-pago-container');
    if (!container) return;
    
    fetch('/api/metodos-pago')
        .then(response => response.json())
        .then(data => {
            if (!Array.isArray(data)) {
                console.error('Error: data no es un array', data);
                return;
            }
            
            container.innerHTML = '';
            data.forEach(metodo => {
                const col = document.createElement('div');
                col.className = 'col-md-6';
                col.innerHTML = `
                    <div class="form-check p-3 border rounded-3 mb-2 metodo-pago-option" style="cursor: pointer;">
                        <input class="form-check-input" type="radio" 
                               name="metodoPago" id="metodo-${metodo.metodoPagoId}" 
                               value="${metodo.metodoPagoId}" data-nombre="${metodo.nombre}">
                        <label class="form-check-label w-100" for="metodo-${metodo.metodoPagoId}" style="cursor: pointer;">
                            <i class="fas ${metodo.nombre === 'Yape' ? 'fa-mobile-alt' : (metodo.nombre === 'Plin' ? 'fa-mobile-alt' : 'fa-money-bill-wave')} me-2 fa-lg"></i>
                            <strong>${metodo.nombre}</strong>
                        </label>
                    </div>
                `;
                container.appendChild(col);
            });
            
            document.querySelectorAll('.metodo-pago-option').forEach(option => {
                option.addEventListener('click', function() {
                    const radio = this.querySelector('input[type="radio"]');
                    radio.checked = true;
                    radio.dispatchEvent(new Event('change'));
                });
            });
            
            document.querySelectorAll('input[name="metodoPago"]').forEach(radio => {
                radio.addEventListener('change', function() {
                    const metodoNombre = this.getAttribute('data-nombre');
                    const comprobanteFields = document.getElementById('comprobante-fields');
                    if (comprobanteFields) {
                        comprobanteFields.style.display = (metodoNombre === 'Yape' || metodoNombre === 'Plin') ? 'block' : 'none';
                        if ((metodoNombre === 'Yape' || metodoNombre === 'Plin')) {
                            document.getElementById('numero-operacion')?.focus();
                        }
                    }
                });
            });
        })
        .catch(error => console.error('Error cargando métodos de pago:', error));
}


function initCheckout() {
    renderResumenPedido();
}

function renderResumenPedido() {
    const carrito = getCarrito();
    const contenedor = document.getElementById('resumen-items');
    if (!contenedor) return;

    contenedor.innerHTML = '';

    if (carrito.length === 0) {
        contenedor.innerHTML = `<p class="text-muted text-center py-4">No hay productos</p>`;
        return;
    }

    carrito.forEach(item => {
        const div = document.createElement('div');
        div.className = 'd-flex justify-content-between mb-2';
        div.innerHTML = `
            <span>${item.nombre} <span class="text-muted">x${item.cantidad}</span></span>
            <span class="fw-bold">${formatPrecio(item.precio * item.cantidad)}</span>
        `;
        contenedor.appendChild(div);
    });

    actualizarTotales();
}

function actualizarTotales() {
    const subtotal = getTotalCarrito();
    const total = subtotal + deliveryCosto;

    document.getElementById('subtotal').textContent = formatPrecio(subtotal);
    document.getElementById('delivery').textContent = formatPrecio(deliveryCosto);
    document.getElementById('total').textContent = formatPrecio(total);
}


// VALIDACIÓN FINAL ANTES DE ENVIAR
function validarFormulario() {
    let isValid = true;
    
    const nombre = document.getElementById('nombre');
    if (!validarCampo(nombre, /^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]{3,}$/, 'Ingresa un nombre válido')) isValid = false;
    
    const telefono = document.getElementById('telefono');
    if (!validarCampo(telefono, /^[0-9]{9}$/, 'Ingresa un teléfono válido de 9 dígitos')) isValid = false;
    
    const direccion = document.getElementById('direccion');
    if (!validarCampo(direccion, /^.{5,}$/, 'Ingresa una dirección válida')) isValid = false;
    
    const zonaSelect = document.getElementById('zona');
    if (!validarSelect(zonaSelect, 'Selecciona una zona de delivery')) isValid = false;
    
    const metodoPago = document.querySelector('input[name="metodoPago"]:checked');
    if (!metodoPago) {
        mostrarModal('Selecciona un método de pago', 'error');
        isValid = false;
    } else {
        const metodoNombre = metodoPago.getAttribute('data-nombre');
        if (metodoNombre === 'Yape' || metodoNombre === 'Plin') {
            const numeroOperacion = document.getElementById('numero-operacion')?.value.trim();
            if (!numeroOperacion) {
                mostrarModal('Ingresa el número de operación de tu transferencia', 'error');
                isValid = false;
            } else if (numeroOperacion.length < 6) {
                mostrarModal('El número de operación debe tener al menos 6 dígitos', 'error');
                isValid = false;
            }
        }
    }
    
    return isValid;
}


// CONFIRMAR PEDIDO
async function confirmarPedido(event) {
    const carrito = getCarrito();

    if (carrito.length === 0) {
        mostrarModal('El carrito está vacío', 'error');
        return;
    }

    if (!validarFormulario()) return;

    const metodoPago = document.querySelector('input[name="metodoPago"]:checked');
    const metodoNombre = metodoPago.getAttribute('data-nombre');
    const zonaSelect = document.getElementById('zona');
    const zonaId = parseInt(zonaSelect.value);
    
    const pedido = {
        cliente: {
            nombre: document.getElementById('nombre').value.trim(),
            telefono: document.getElementById('telefono').value.trim(),
            email: null,
            direccion: document.getElementById('direccion').value.trim(),
            referencia: document.getElementById('referencia').value.trim() || '',
            zonaId: zonaId,
            zonaNombre: zonaSelect.options[zonaSelect.selectedIndex].text
        },
        productos: carrito.map(item => ({
            id: item.id,
            nombre: item.nombre,
            precio: item.precio,
            cantidad: item.cantidad,
            imagen: item.imagen
        })),
        pago: {
            metodoPagoId: parseInt(metodoPago.value),
            metodo: metodoNombre
        },
        totales: {
            subtotal: getTotalCarrito(),
            delivery: deliveryCosto,
            total: getTotalCarrito() + deliveryCosto
        },
        tiempoEstimado: '30-45 minutos',
        numeroOperacion: document.getElementById('numero-operacion')?.value.trim() || null
    };

    const btnConfirmar = event.target;
    const textoOriginal = btnConfirmar.innerHTML;
    btnConfirmar.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Procesando...';
    btnConfirmar.disabled = true;

    try {
        const response = await fetch('/api/pedidos/crear', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(pedido)
        });
        
        const data = await response.json();
        
        if (data.pedidoId) {
            localStorage.setItem('ultimoPedido', JSON.stringify(pedido));
            localStorage.removeItem('carrito');
            
            mostrarModal('¡Pedido confirmado correctamente!', 'success');
            
            setTimeout(() => {
                window.location.href = '/confirmacion';
            }, 1500);
        } else {
            mostrarModal('Error al procesar el pedido: ' + (data.mensaje || 'Error desconocido'), 'error');
            btnConfirmar.innerHTML = textoOriginal;
            btnConfirmar.disabled = false;
        }
    } catch (error) {
        console.error('Error:', error);
        mostrarModal('Error de conexión con el servidor', 'error');
        btnConfirmar.innerHTML = textoOriginal;
        btnConfirmar.disabled = false;
    }
}

function mostrarModal(mensaje, tipo = 'info') {
    const titulo = document.getElementById('modalTitulo');
    const texto = document.getElementById('modalTexto');

    let icono = 'fa-info-circle';
    let color = 'text-primary';

    if (tipo === 'error') { icono = 'fa-exclamation-circle'; color = 'text-danger'; }
    if (tipo === 'success') { icono = 'fa-check-circle'; color = 'text-success'; }

    titulo.innerHTML = `<i class="fas ${icono} me-2 ${color}"></i>Mensaje`;
    texto.textContent = mensaje;

    const modal = new bootstrap.Modal(document.getElementById('modalMensaje'));
    modal.show();
}


