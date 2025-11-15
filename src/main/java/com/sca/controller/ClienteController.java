package com.sca.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.ui.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sca.model.Cliente;
import com.sca.model.Respuesta;
import com.sca.service.impl.ClienteServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(tags = "Cliente")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@Slf4j
public class ClienteController {

    @Autowired
    ClienteServiceImpl clientesServiceImpl;

    @PostMapping(value = "/addCliente", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Agrega un Cliente", notes = "Esta operación agrega un Cliente a la base de datos")
    public ResponseEntity<Object> addCliente(@RequestBody @Validated Cliente cliente, BindingResult bindingResult) throws BindException {
        return clientesServiceImpl.save(cliente, bindingResult);
    }

    @GetMapping(value = "/getAllCliente", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Consultar Cliente", notes = "Devuelve todos los clientes")
    public Respuesta getAllCliente() {
        return clientesServiceImpl.findAll();
    }

    @GetMapping(value = "/getByIdCliente/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Consultar Cliente por id", notes = "Consulta un cliente por su identificador")
    public Respuesta getByIdCliente(@PathVariable Long id) {
        return clientesServiceImpl.findById(id);
    }

    @DeleteMapping(value = "/deleteCliente/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Eliminar un Cliente", notes = "Elimina un cliente de la base de datos")
    public Respuesta deleteCliente(@PathVariable Long id) {
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

    // Login
    @PostMapping(value = "/loginCliente", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Login Cliente", notes = "Valida documento y contraseña de un cliente")
    public Respuesta loginCliente(@RequestBody Cliente loginRequest) {
        return clientesServiceImpl.login(loginRequest.getDocumento(), loginRequest.getContrasenia());
    }

    // Registro público de cliente
    @PostMapping(value = "/registrarCliente", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Registrar Cliente", notes = "Registra un nuevo cliente con validaciones de unicidad para DNI y email")
    public ResponseEntity<Object> registrarCliente(@RequestBody @Validated Cliente cliente, BindingResult bindingResult) throws BindException {
        return clientesServiceImpl.registrarCliente(cliente, bindingResult);
    }

    // Endpoint de prueba para validar DNI
    @PostMapping(value = "/validarDNI", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Validar DNI", notes = "Endpoint de prueba para validar formato de DNI")
    public ResponseEntity<Object> validarDNI(@RequestBody Map<String, String> request) {
        String dni = request.get("dni");
        log.info("Validando DNI: {}", dni);

        boolean esValido = dni != null && dni.matches("^\\d+$");

        Map<String, Object> response = new HashMap<>();
        response.put("dni", dni);
        response.put("esValido", esValido);
        response.put("regex", "^\\d+$");
        response.put("longitud", dni != null ? dni.length() : 0);
        response.put("soloNumeros", dni != null ? dni.matches("^\\d+$") : false);

        String tuDNI = "37043109";
        boolean tuDNIEsValido = tuDNI.matches("^\\d+$");
        response.put("tuDNI", tuDNI);
        response.put("tuDNIEsValido", tuDNIEsValido);

        return ResponseEntity.ok(response);
    }

    // Buscar cliente por email
    @GetMapping(value = "/getByMailCliente/{mail}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Consultar Cliente por email", notes = "Consulta un cliente por su email")
    public ResponseEntity<?> getByMailCliente(@PathVariable String mail) {
        return clientesServiceImpl.findByMail(mail);
    }

    // Recuperación de contraseña: genera token y envía link
    @PostMapping(value = "/clientes/forgot-password", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Recuperar contraseña", notes = "Genera un token y envía un enlace de recuperación al email del cliente")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        return clientesServiceImpl.forgotPassword(email);
    }


    // Confirmación de reset: recibe token y nueva contraseña
    @PostMapping(value = "/clientes/reset-password", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Confirmar cambio de contraseña", notes = "Aplica el cambio de contraseña usando el token de recuperación")
    public ResponseEntity<?> confirmarResetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String nuevaContrasenia = request.get("nuevaContrasenia");
        return clientesServiceImpl.aplicarResetPassword(token, nuevaContrasenia);
    }

    // Test de envío de correo
    @GetMapping("/test-email")
    @ApiOperation(value = "Test de envío de correo", notes = "Envía un correo de prueba al destinatario indicado")
    public ResponseEntity<?> testEmail() {
        String destinatario = "stationnet2@gmail.com";
        String asunto = "Correo de prueba";
        String cuerpoHtml = "<h3>Este es un correo de prueba</h3><p>Si lo recibís, el sistema de envío funciona correctamente.</p>";

        try {
            clientesServiceImpl.testEmail(destinatario, asunto, cuerpoHtml);
            return ResponseEntity.ok("Correo de prueba enviado a: " + destinatario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al enviar correo de prueba: " + e.getMessage());
        }
    }
}
