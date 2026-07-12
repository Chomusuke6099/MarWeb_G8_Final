package com.bembos.delivery.service;

import com.bembos.delivery.model.Producto;
import com.bembos.delivery.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    public List<Producto> obtenerTodos() {
        return productoRepository.listarProductos();
    }

    public Producto buscarPorId(Integer id) {
        return productoRepository.obtenerProductoPorId(id);
    }
    
    public List<Producto> buscarPorCategoria(String categoria) {
    return productoRepository.buscarPorCategoria(categoria);
    }
}


