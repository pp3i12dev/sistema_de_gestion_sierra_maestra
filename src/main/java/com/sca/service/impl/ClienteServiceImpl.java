package com.sca.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.sca.model.Cliente;
import com.sca.model.Respuesta;
import com.sca.repository.ClienteRepository;
import com.sca.service.ClienteService;
import com.sca.service.EmailService;

import java.util.Optional;
import java.util.UUID;

@Service
public class ClienteServiceImpl extends ResponseEntityExceptionHandler implements ClienteService {

    Logger log = LoggerFactory.getLogger(String.class);

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    private EmailService emailService;

    Respuesta respuesta;
    String resp = "";

    @ExceptionHandler(BindException.class)
    @Override
    public ResponseEntity<Object> save(Cliente cliente, BindingResult bindingResult) throws BindException {
        respuesta = new Respuesta();
        try {
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Se agregó un Cliente");
            respuesta.setData(clienteRepository.save(cliente));
        } catch (Exception e) {
            respuesta.setCodigo(String.valueOf(HttpStatus.BAD_REQUEST.value()));
            respuesta.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
            respuesta.setDescripcion("No se pudo agregar el Cliente");
            if (bindingResult.hasErrors()) {
                bindingResult.getAllErrors().forEach(r -> resp = resp + r.getDefaultMessage() + ";");
                respuesta.setData(resp);
                resp = "";
                return handleExceptionInternal(e, respuesta, new HttpHeaders(), HttpStatus.BAD_REQUEST, null);
            } else {
                respuesta.setData(e.getMessage());
                return handleExceptionInternal(e, respuesta, new HttpHeaders(), HttpStatus.BAD_REQUEST, null);
            }
        }
        return new ResponseEntity<Object>(respuesta, null, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> update(Cliente cliente, BindingResult bindingResult) throws BindException {
        respuesta = new Respuesta();
        try {
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Se modificaron los datos del Cliente");
            respuesta.setData(clienteRepository.save(cliente));
        } catch (Exception e) {
            respuesta.setCodigo(String.valueOf(HttpStatus.BAD_REQUEST.value()));
            respuesta.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
            respuesta.setDescripcion("No se pudo modificar el Cliente");
            if (bindingResult.hasErrors()) {
                bindingResult.getAllErrors().forEach(r -> resp = resp + r.getDefaultMessage() + ";");
                respuesta.setData(resp);
                resp = "";
                return handleExceptionInternal(e, respuesta, new HttpHeaders(), HttpStatus.BAD_REQUEST, null);
            } else {
                respuesta.setData(e.getMessage());
                return handleExceptionInternal(e, respuesta, new HttpHeaders(), HttpStatus.BAD_REQUEST, null);
            }
        }
        return new ResponseEntity<Object>(respuesta, null, HttpStatus.CREATED);
    }

    @Override
    public Respuesta delete(Long id) {
        respuesta = new Respuesta();
        try {
            Cliente cliente = clienteRepository.findById(id).get();
            clienteRepository.deleteById(id);
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Se eliminó un Cliente");
            respuesta.setData(cliente);
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudo eliminar el Cliente");
            respuesta.setData(e.getMessage());
        }
        return respuesta;
    }

    @Override
    public Respuesta findAll() {
        respuesta = new Respuesta();
        try {
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Se muestran todos los Clientes");
            respuesta.setData(clienteRepository.findAll());
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudieron mostrar los Clientes");
            respuesta.setData(e.getMessage());
        }
        return respuesta;
    }

    @Override
    public Respuesta findById(Long id) {
        respuesta = new Respuesta();
        try {
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Datos del Cliente");
            respuesta.setData(clienteRepository.findById(id).orElse(null));
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudieron mostrar los datos del Cliente");
            respuesta.setData(e.getMessage());
        }
        return respuesta;
    }

    @Override
    public Respuesta contarClientes() {
        respuesta = new Respuesta();
        try {
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Cantidad de clientes");
            respuesta.setData(clienteRepository.count());
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudo contar los clientes");
            respuesta.setData(e.getMessage());
        }
        return respuesta;
    }
    @Override
    public Respuesta login(String documento, String contrasenia) {
        respuesta = new Respuesta();
        try {
            Cliente cliente = clienteRepository.findByDocumentoAndContrasenia(documento, contrasenia);
            if (cliente != null) {
                respuesta.setCodigo("200");
                respuesta.setStatus("Ok");
                respuesta.setDescripcion("Login exitoso");
                respuesta.setData(cliente);
            } else {
                respuesta.setCodigo("401");
                respuesta.setStatus("Unauthorized");
                respuesta.setDescripcion("Documento o contraseña incorrectos");
                respuesta.setData(null);
            }
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("Error en login");
            respuesta.setData(e.getMessage());
        }
        return respuesta;
    }

    @Override
    public ResponseEntity<Object> registrarCliente(Cliente cliente, BindingResult bindingResult) throws BindException {
        respuesta = new Respuesta();
        try {
            if (clienteRepository.existsByDocumento(cliente.getDocumento())) {
                respuesta.setCodigo("409");
                respuesta.setStatus("Conflict");
                respuesta.setDescripcion("El DNI ya está registrado en el sistema");
                respuesta.setData("El documento " + cliente.getDocumento() + " ya está en uso");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
            }

            if (clienteRepository.existsByMail(cliente.getMail())) {
                respuesta.setCodigo("409");
                respuesta.setStatus("Conflict");
                respuesta.setDescripcion("El email ya está registrado en el sistema");
                respuesta.setData("El email " + cliente.getMail() + " ya está en uso");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
            }

            cliente.setActivo(true);
            cliente.setTipo("Cliente");
            cliente.setLegajo("CLI-" + System.currentTimeMillis());

            Cliente clienteGuardado = clienteRepository.save(cliente);

            respuesta.setCodigo("201");
            respuesta.setStatus("Created");
            respuesta.setDescripcion("Cliente registrado exitosamente");
            respuesta.setData(clienteGuardado);

            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);

        } catch (Exception e) {
            respuesta.setCodigo(String.valueOf(HttpStatus.BAD_REQUEST.value()));
            respuesta.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
            respuesta.setDescripcion("Error al registrar el cliente");

            if (bindingResult.hasErrors()) {
                bindingResult.getAllErrors().forEach(r -> resp = resp + r.getDefaultMessage() + "; ");
                respuesta.setData(resp);
                resp = "";
            } else {
                respuesta.setData(e.getMessage());
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
        }
    }

    public ResponseEntity<?> findByMail(String mail) {
        try {
            String normalizedMail = mail == null ? null : mail.trim().toLowerCase();
            Optional<Cliente> clienteOpt = clienteRepository.findByMail(normalizedMail);
            if (clienteOpt.isPresent()) {
                return ResponseEntity.ok(clienteOpt.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Cliente no encontrado con el mail: " + mail);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar cliente por mail: " + e.getMessage());
        }
    }

    public ResponseEntity<?> aplicarResetPassword(String token, String nuevaContrasenia) {
        try {
            Optional<Cliente> clienteOpt = clienteRepository.findByTokenRecuperacion(token);
            if (clienteOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Token inválido o expirado");
            }

            Cliente cliente = clienteOpt.get();
            cliente.setContrasenia(nuevaContrasenia);
            cliente.setTokenRecuperacion(null); // ✅ invalidar token después de usarlo
            clienteRepository.save(cliente);

            return ResponseEntity.ok("Contraseña actualizada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar la contraseña: " + e.getMessage());
        }
    }


    public ResponseEntity<?> forgotPassword(String email) {
        try {
            String normalizedMail = email == null ? null : email.trim().toLowerCase();
            Optional<Cliente> clienteOpt = clienteRepository.findByMail(normalizedMail);

            if (clienteOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No existe un cliente con el email: " + email);
            }

            Cliente cliente = clienteOpt.get();

            String token = UUID.randomUUID().toString();
            log.info("Token de recuperación generado para {}: {}", normalizedMail, token);

            // ✅ Guardar el token en el cliente
            cliente.setTokenRecuperacion(token);
            clienteRepository.save(cliente); // ✅ Persistir en la base

            String asunto = "Recuperación de contraseña";
            String cuerpoHtml = "Hola " + cliente.getNombre() + ",<br><br>"
                    + "Para recuperar tu contraseña, hacé clic en el siguiente enlace:<br>"
                    + "<a href='http://localhost:8080/clientes/reset-password?token=" + token + "'>Recuperar contraseña</a><br><br>"
                    + "Si no solicitaste esto, ignorá este mensaje.";

            log.info("Preparando envío de correo a: {}", normalizedMail);
            emailService.sendEmail(normalizedMail, asunto, cuerpoHtml);
            log.info("Correo enviado (o en proceso) a: {}", normalizedMail);

            return ResponseEntity.ok("Se envió un enlace de recuperación al correo: " + email);

        } catch (Exception e) {
            log.error("Error al enviar correo de recuperación: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la recuperación de contraseña: " + e.getMessage());
        }
    }

    // 🔹 Método para testear envío de correo
    public void testEmail(String to, String subject, String htmlBody) {
        log.info("Enviando correo de prueba a: {}", to);
        emailService.sendEmail(to, subject, htmlBody);
        log.info("Correo de prueba enviado.");
    }

    public ResponseEntity<?> validarTokenResetPassword(String token) {
        try {
            Optional<Cliente> clienteOpt = clienteRepository.findByTokenRecuperacion(token);
            if (clienteOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Token inválido o expirado");
            }

            Cliente cliente = clienteOpt.get();
            return ResponseEntity.ok("Token válido para: " + cliente.getMail());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al validar el token: " + e.getMessage());
        }
    }

}
