package com.sca.service;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

import com.sca.model.PedidoCliente;
import com.sca.model.Respuesta;

public interface PedidoClienteService {
    ResponseEntity<Object> save(PedidoCliente pedidoCliente, BindingResult bindingResult) throws BindException;
    ResponseEntity<Object> update(PedidoCliente pedidoCliente, BindingResult bindingResult) throws BindException;
    Respuesta delete(Long id);
    Respuesta findById(Long id);
    Respuesta findAll();
    Respuesta findByNombreCliente(String nombreCliente);
    Respuesta findByEmailCliente(String emailCliente);
    Respuesta findByEstado(String estado);
    Respuesta findByEstadoPago(String estadoPago);
    Respuesta cancel(Long id);
}
