function agregarAlCarrito(productoId) {
    const input = document.getElementById(`qty-${productoId}`);
    let cantidad = parseInt(input.value);

    if (isNaN(cantidad) || cantidad <= 0) {
        cantidad = 1;
        input.value = 1;
    }

    // Leer nombre, precio e imagen desde el DOM (Thymeleaf)
    const card = input.closest('.card');
    const nombre = card.querySelector('.card-title').textContent.trim();
    const precioTexto = card.querySelector('.product-price span').textContent.trim();
    const precio = parseFloat(precioTexto);
    const imagen = card.querySelector('.product-img').getAttribute('src');

    addToCarrito(productoId, cantidad, nombre, precio, imagen);

    mostrarFeedback(input);
}

function mostrarFeedback(input) {
    input.classList.add('is-valid');
    setTimeout(() => input.classList.remove('is-valid'), 800);
}
