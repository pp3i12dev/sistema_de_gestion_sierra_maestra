package com.sca.controller;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sca.model.Cliente;
import com.sca.model.Respuesta;
import com.sca.service.impl.ClienteServiceImpl;

import java.util.HashMap;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(tags = "Cliente")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class ClienteController {

    @Autowired
    ClienteServiceImpl clientesServiceImpl;

    @PostMapping(value = "/addCliente", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Agrega un Cliente", notes = "Esta operación agrega un Cliente a la base de datos")
    public ResponseEntity<Object> addCliente(@RequestBody @Validated Cliente cliente, BindingResult bindingResult) throws BindException {
        return clientesServiceImpl.save(cliente,bindingResult);
    }

    @GetMapping(value = "/getAllCliente", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Consultar Cliente", notes = "Devuelve todos los clientes")
    public Respuesta getAllCliente() {
        return clientesServiceImpl.findAll();
    }

    @GetMapping(value = "/getByIdCliente/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Consultar Cliente por id", notes = "Consulta un cliente por su identificador")
    public Respuesta getByIdCliente(@PathParam("id") @PathVariable Long id) {
        return clientesServiceImpl.findById(id);
    }

    @DeleteMapping(value = "/deleteCliente/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Eliminar un Cliente", notes = "Elimina un cliente de la base de datos")
    public Respuesta deleteCliente(@PathParam("id") @PathVariable Long id) {
        return clientesServiceImpl.delete(id);
    }

    @PutMapping(value = "/updateCliente", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Actualizar un Cliente", notes = "Actualiza un cliente en la base de datos")
    public ResponseEntity<Object> updateCliente(@RequestBody Cliente cliente, BindingResult bindingResult) throws BindException {
        return clientesServiceImpl.update(cliente, bindingResult);
    }

    @GetMapping(value = "/contarClientes", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Contar Clientes", notes = "Cuenta todos los clientes en la base de datos")
    public Respuesta contarClientes() {
        return clientesServiceImpl.contarClientes();
    }

    // 🔹 Nuevo: login
    @PostMapping(value = "/loginCliente", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Login Cliente", notes = "Valida documento y contraseña de un cliente")
    public Respuesta loginCliente(@RequestBody Cliente loginRequest) {
        return clientesServiceImpl.login(loginRequest.getDocumento(), loginRequest.getContrasenia());
    }

    // 🔹 Nuevo: registro público de cliente
    @PostMapping(value = "/registrarCliente", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Registrar Cliente", notes = "Registra un nuevo cliente con validaciones de unicidad para DNI y email")
    public ResponseEntity<Object> registrarCliente(@RequestBody @Validated Cliente cliente, BindingResult bindingResult) throws BindException {
        return clientesServiceImpl.registrarCliente(cliente, bindingResult);
    }

    // 🔹 Endpoint de prueba para validar DNI
    @PostMapping(value = "/validarDNI", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Validar DNI", notes = "Endpoint de prueba para validar formato de DNI")
    public ResponseEntity<Object> validarDNI(@RequestBody Map<String, String> request) {
        String dni = request.get("dni");
        log.info("Validando DNI: {}", dni);
        
        // Verificar si el DNI cumple con la expresión regular simplificada
        boolean esValido = dni != null && dni.matches("^\\d+$");
        
        Map<String, Object> response = new HashMap<>();
        response.put("dni", dni);
        response.put("esValido", esValido);
        response.put("regex", "^\\d+$");
        response.put("longitud", dni != null ? dni.length() : 0);
        response.put("soloNumeros", dni != null ? dni.matches("^\\d+$") : false);
        
        // Prueba manual con tu DNI específico
        String tuDNI = "37043109";
        boolean tuDNIEsValido = tuDNI.matches("^\\d+$");
        response.put("tuDNI", tuDNI);
        response.put("tuDNIEsValido", tuDNIEsValido);
        
        return ResponseEntity.ok(response);
    }
}
