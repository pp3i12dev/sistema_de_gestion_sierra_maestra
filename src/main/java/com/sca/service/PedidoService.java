package com.sca.service;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

import com.sca.model.Pedido;
import com.sca.model.Respuesta;

public interface PedidoService {
    ResponseEntity<Object> save(Pedido pedido, BindingResult bindingResult) throws BindException;
    ResponseEntity<Object> update(Pedido pedido, BindingResult bindingResult) throws BindException;
    Respuesta delete(Long id);
    Respuesta findById(Long id);
    Respuesta findAll();

    // 🔹 nuevos
    Respuesta findByCliente(Long clienteId);
    Respuesta cancel(Long id);
    
    // Actualizar solo el estado de un pedido
    Respuesta updateEstado(Long id, String estado);
}
