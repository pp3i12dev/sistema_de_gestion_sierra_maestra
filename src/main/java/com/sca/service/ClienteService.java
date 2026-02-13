package com.sca.service;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

import com.sca.model.Cliente;
import com.sca.model.Respuesta;

public interface ClienteService {

    public ResponseEntity<Object> save(Cliente cliente, BindingResult bindingResult) throws BindException;

    public ResponseEntity<Object> update(Cliente cliente, BindingResult bindingResult) throws BindException;

    public Respuesta delete(Long id);

    public Respuesta findAll();

    public Respuesta findById(Long id);

    public Respuesta contarClientes();

    // 🔹 Nuevo: login
    public Respuesta login(String documento, String contrasenia);
    
    // 🔹 Nuevo: registro de cliente público
    public ResponseEntity<Object> registrarCliente(Cliente cliente, BindingResult bindingResult) throws BindException;
}
