package com.sca.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

import com.sca.model.Accesorio;
import com.sca.model.Respuesta;
import com.sca.repository.AccesorioRepository;
import com.sca.service.AccesorioService;

@Service
public class AccesorioServiceImpl extends ResponseEntityExceptionHandler implements AccesorioService {

    Logger log = LoggerFactory.getLogger(String.class);

    @Autowired
    private AccesorioRepository accesorioRepository;

    private Respuesta respuesta;
    private String resp = "";

    @ExceptionHandler(BindException.class)
    @Override
    public ResponseEntity<Object> save(Accesorio accesorio, BindingResult bindingResult) throws BindException {
        respuesta = new Respuesta();
        try {
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Se agrego un Accesorio");
            respuesta.setData(accesorioRepository.save(accesorio));
        } catch (Exception e) {
            respuesta.setCodigo(String.valueOf(HttpStatus.BAD_REQUEST.value()));
            respuesta.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
            respuesta.setDescripcion("No se pudo agregar el Accesorio");
            if (bindingResult.hasErrors()) {
                bindingResult.getAllErrors().forEach(r -> {
                    resp = resp + r.getDefaultMessage() + ";";
                });
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
            Accesorio accesorio = accesorioRepository.findById(id).orElse(null);
            if (accesorio != null) {
                accesorioRepository.deleteById(id);
                respuesta.setCodigo("200");
                respuesta.setStatus("Ok");
                respuesta.setDescripcion("Se eliminó un Accesorio");
                respuesta.setData(accesorio);
            } else {
                respuesta.setCodigo("404");
                respuesta.setStatus("Not Found");
                respuesta.setDescripcion("Accesorio no encontrado");
                respuesta.setData(null);
            }
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudo eliminar el Accesorio");
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
            respuesta.setDescripcion("Se muestran todos los Accesorios");
            respuesta.setData(accesorioRepository.findAll());
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudieron mostrar los Accesorios");
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
            respuesta.setDescripcion("Datos del Accesorio");
            respuesta.setData(accesorioRepository.findById(id));
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudieron mostrar los datos del Accesorio");
            respuesta.setData(e.getMessage());
        }
        return respuesta;
    }

    @Override
    public ResponseEntity<Object> update(Accesorio accesorio, BindingResult bindingResult) throws BindException {
        respuesta = new Respuesta();
        try {
            log.info("📤 Actualizando accesorio - ID: {}, sessionReserva: {}, timestampReserva: {}", 
                    accesorio.getId(), accesorio.getSessionReserva(), accesorio.getTimestampReserva());
            
            // ✅ BUSCAR EL ACCESORIO EXISTENTE EN LA BD
            Optional<Accesorio> optionalAccesorio = accesorioRepository.findById(accesorio.getId());
            if (!optionalAccesorio.isPresent()) {
                respuesta.setCodigo("404");
                respuesta.setStatus("Not Found");
                respuesta.setDescripcion("Accesorio no encontrado");
                respuesta.setData(null);
                return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
            }
            
            Accesorio accesorioExistente = optionalAccesorio.get();
            
            // ✅ ACTUALIZAR SOLO LOS CAMPOS NECESARIOS
            accesorioExistente.setNombre(accesorio.getNombre());
            accesorioExistente.setEstado(accesorio.getEstado());
            accesorioExistente.setNotas(accesorio.getNotas());
            //accesorioExistente.setPrecio(accesorio.getPrecio());
            
            // ✅ ACTUALIZAR CAMPOS DE RESERVA (CRÍTICO)
            accesorioExistente.setSessionReserva(accesorio.getSessionReserva());
            accesorioExistente.setTimestampReserva(accesorio.getTimestampReserva());
            
            Accesorio accesorioActualizado = accesorioRepository.save(accesorioExistente);
            
            log.info("✅ Accesorio actualizado exitosamente - ID: {}, Nuevo estado: {}, sessionReserva: {}", 
                    accesorioActualizado.getId(), accesorioActualizado.getEstado(), accesorioActualizado.getSessionReserva());
            
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Se modificaron los datos del Accesorio");
            respuesta.setData(accesorioActualizado);
            
        } catch (Exception e) {
            log.error("❌ Error actualizando accesorio ID {}: {}", accesorio.getId(), e.getMessage());
            respuesta.setCodigo(String.valueOf(HttpStatus.BAD_REQUEST.value()));
            respuesta.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
            respuesta.setDescripcion("No se pudo modificar el Accesorio");
            
            if (bindingResult.hasErrors()) {
                bindingResult.getAllErrors().forEach(r -> {
                    resp = resp + r.getDefaultMessage() + ";";
                });
                respuesta.setData(resp);
                resp = "";
                return handleExceptionInternal(e, respuesta, new HttpHeaders(), HttpStatus.BAD_REQUEST, null);
            } else {
                respuesta.setData(e.getMessage());
                return handleExceptionInternal(e, respuesta, new HttpHeaders(), HttpStatus.BAD_REQUEST, null);
            }
        }
        return new ResponseEntity<Object>(respuesta, null, HttpStatus.OK);
    }

    // ✅ NUEVO MÉTODO PARA ACTUALIZAR RESERVAS
    @Override
    public ResponseEntity<Object> updateReservaAccesorio(Accesorio accesorio) {
        try {
            log.info("📤 Actualizando reserva accesorio - ID: {}, sessionReserva: {}, timestampReserva: {}", 
                    accesorio.getId(), accesorio.getSessionReserva(), accesorio.getTimestampReserva());
            
            // Buscar el accesorio existente
            Optional<Accesorio> optionalAccesorio = accesorioRepository.findById(accesorio.getId());
            if (!optionalAccesorio.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Accesorio no encontrado");
            }
            
            Accesorio accesorioExistente = optionalAccesorio.get();
            
            // ✅ SOLO ACTUALIZAR CAMPOS DE RESERVA Y ESTADO
            accesorioExistente.setSessionReserva(accesorio.getSessionReserva());
            accesorioExistente.setTimestampReserva(accesorio.getTimestampReserva());
            accesorioExistente.setEstado(accesorio.getEstado());
            
            Accesorio accesorioActualizado = accesorioRepository.save(accesorioExistente);
            
            log.info("✅ Reserva de accesorio actualizada - ID: {}, Estado: {}, Session: {}", 
                    accesorioActualizado.getId(), accesorioActualizado.getEstado(), accesorioActualizado.getSessionReserva());
            
            return ResponseEntity.ok(accesorioActualizado);
            
        } catch (Exception e) {
            log.error("❌ Error actualizando reserva de accesorio: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error actualizando reserva: " + e.getMessage());
        }
    }

    @Override
    public Respuesta findAccesoriosPorEstado(String estado) {
        respuesta = new Respuesta();
        try {
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Datos de los Accesorios por Estado");
            respuesta.setData(
                accesorioRepository.findAll()
                                   .stream()
                                   .filter(a -> estado.equalsIgnoreCase(a.getEstado()))
                                   .collect(Collectors.toList())
            );
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudieron mostrar los datos de los Accesorios");
            respuesta.setData(e.getMessage());
        }
        return respuesta;
    }

    @Override
    public void marcarComoAlquilados(List<Long> ids) {
        ids.forEach(id -> {
            var a = accesorioRepository.findById(id).orElse(null);
            if (a != null) {
                a.setEstado("Alquilado");
                accesorioRepository.save(a);
            }
        });
    }

    @Override
    public void marcarComoDisponibles(List<Long> ids) {
    ids.forEach(id -> {
        var a = accesorioRepository.findById(id).orElse(null);
        if (a != null) {
            a.setEstado("Disponible");
            accesorioRepository.save(a);
        }
    });
    }

    @Override
    public Respuesta findDisponiblesByNombre(String nombre) {
        respuesta = new Respuesta();
        try {
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Accesorios disponibles por nombre");
            respuesta.setData(
                accesorioRepository.findAll()
                    .stream()
                    .filter(a -> "Disponible".equalsIgnoreCase(a.getEstado()) 
                              && nombre.equalsIgnoreCase(a.getNombre()))
                    .collect(Collectors.toList())
            );
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudieron obtener los accesorios disponibles");
            respuesta.setData(e.getMessage());
        }
        return respuesta;
    }
}