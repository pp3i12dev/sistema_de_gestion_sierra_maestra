package com.sca.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.sca.model.Respuesta;
import com.sca.model.SueldoBasico;
import com.sca.repository.SueldoBasicoRepository;
import com.sca.service.SueldoBasicoService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SueldoBasicoServiceImpl extends ResponseEntityExceptionHandler implements SueldoBasicoService {

    @Autowired
    SueldoBasicoRepository sueldoBasicoRepository;

    Respuesta respuesta;
    String resp = "";

    @Override
    public ResponseEntity<Object> save(SueldoBasico sueldoBasico, BindingResult bindingResult) throws BindException {
        respuesta = new Respuesta();
        try {
            log.info("Guardando sueldo basico: {}", sueldoBasico);
            
            // Validar si hay errores de binding
            if (bindingResult.hasErrors()) {
                bindingResult.getAllErrors().forEach(r -> {
                    resp = resp + r.getDefaultMessage() + ";";
                });
                respuesta.setCodigo(String.valueOf(HttpStatus.BAD_REQUEST.value()));
                respuesta.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
                respuesta.setDescripcion("Errores de validación");
                respuesta.setData(resp);
                resp = "";
                return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
            }
            
            SueldoBasico saved = sueldoBasicoRepository.save(sueldoBasico);
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Se agregó un SueldoBasico");
            respuesta.setData(saved);
            
        } catch (Exception e) {
            log.error("Error al guardar sueldo basico: {}", e.getMessage());
            respuesta.setCodigo(String.valueOf(HttpStatus.BAD_REQUEST.value()));
            respuesta.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
            respuesta.setDescripcion("No se pudo agregar el SueldoBasico");
            respuesta.setData(e.getMessage());
            return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
    }

    @Override
    public Respuesta delete(Long id) {
        respuesta = new Respuesta();
        log.info("Eliminando sueldo basico con id: {}", id);
        try {
            if (sueldoBasicoRepository.existsById(id)) {
                SueldoBasico sueldoBasico = sueldoBasicoRepository.findById(id).get();
                sueldoBasicoRepository.deleteById(id);
                respuesta.setCodigo("200");
                respuesta.setStatus("Ok");
                respuesta.setDescripcion("Se eliminó un SueldoBasico");
                respuesta.setData(sueldoBasico);
            } else {
                respuesta.setCodigo("404");
                respuesta.setStatus("No encontrado");
                respuesta.setDescripcion("No se encontró el SueldoBasico con id: " + id);
                respuesta.setData(null);
            }
        } catch (Exception e) {
            log.error("Error al eliminar sueldo basico: {}", e.getMessage());
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudo eliminar el SueldoBasico");
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
            respuesta.setDescripcion("Se muestran todos los SueldoBasico");
            respuesta.setData(sueldoBasicoRepository.findAll());
        } catch (Exception e) {
            log.error("Error al obtener sueldos basicos: {}", e.getMessage());
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudieron mostrar los SueldoBasico");
            respuesta.setData(e.getMessage());
        }
        return respuesta;
    }

    @Override
    public Respuesta findById(Long id) {
        respuesta = new Respuesta();
        try {
            if (sueldoBasicoRepository.existsById(id)) {
                respuesta.setCodigo("200");
                respuesta.setStatus("Ok");
                respuesta.setDescripcion("Datos del SueldoBasico");
                respuesta.setData(sueldoBasicoRepository.findById(id));
            } else {
                respuesta.setCodigo("404");
                respuesta.setStatus("No encontrado");
                respuesta.setDescripcion("No se encontró el SueldoBasico con id: " + id);
                respuesta.setData(null);
            }
        } catch (Exception e) {
            log.error("Error al obtener sueldo basico por id: {}", e.getMessage());
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudieron mostrar los datos del SueldoBasico");
            respuesta.setData(e.getMessage());
        }
        return respuesta;
    }

    @Override
    public ResponseEntity<Object> update(SueldoBasico sueldoBasico, BindingResult bindingResult) throws BindException {
        respuesta = new Respuesta();
        try {
            log.info("Actualizando sueldo basico: {}", sueldoBasico);
            
            // Validar si hay errores de binding
            if (bindingResult.hasErrors()) {
                bindingResult.getAllErrors().forEach(r -> {
                    resp = resp + r.getDefaultMessage() + ";";
                });
                respuesta.setCodigo(String.valueOf(HttpStatus.BAD_REQUEST.value()));
                respuesta.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
                respuesta.setDescripcion("Errores de validación");
                respuesta.setData(resp);
                resp = "";
                return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
            }
            
            if (sueldoBasicoRepository.existsById(sueldoBasico.getId())) {
                SueldoBasico updated = sueldoBasicoRepository.save(sueldoBasico);
                respuesta.setCodigo("200");
                respuesta.setStatus("Ok");
                respuesta.setDescripcion("Se modificaron los datos del SueldoBasico");
                respuesta.setData(updated);
            } else {
                respuesta.setCodigo("404");
                respuesta.setStatus("No encontrado");
                respuesta.setDescripcion("No se encontró el SueldoBasico para actualizar");
                respuesta.setData(null);
                return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error al actualizar sueldo basico: {}", e.getMessage());
            respuesta.setCodigo(String.valueOf(HttpStatus.BAD_REQUEST.value()));
            respuesta.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
            respuesta.setDescripcion("No se pudo modificar el SueldoBasico");
            respuesta.setData(e.getMessage());
            return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }
}