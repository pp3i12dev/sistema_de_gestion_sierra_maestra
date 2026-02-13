package com.sca.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sca.model.Cliente;
import com.sca.model.Respuesta;
import com.sca.repository.ClienteRepository;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @PostMapping("/loginCliente")
    public ResponseEntity<Respuesta> loginCliente(
            @RequestParam String documento, 
            @RequestParam String contrasenia) {
        
        Respuesta respuesta = new Respuesta();
        try {

            Cliente cliente = clienteRepository.findByDocumentoAndContrasenia(documento, contrasenia);

            if (cliente != null) {
                respuesta.setCodigo("200");
                respuesta.setStatus("Ok");
                respuesta.setDescripcion("Login exitoso");
                respuesta.setData(cliente);
                return ResponseEntity.ok(respuesta);
            } else {
                respuesta.setCodigo("401");
                respuesta.setStatus("Unauthorized");
                respuesta.setDescripcion("Documento o contraseña incorrectos");
                respuesta.setData(null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
            }
        } catch (Exception e) {
            respuesta.setCodigo("500");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("Error en el login");
            respuesta.setData(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }
}
