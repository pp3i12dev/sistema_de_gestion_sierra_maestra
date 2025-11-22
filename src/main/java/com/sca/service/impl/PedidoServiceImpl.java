package com.sca.service.impl;

import java.util.List;
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

import com.sca.model.Pedido;
import com.sca.model.Respuesta;
import com.sca.model.HistorialCambio;
import com.sca.repository.PedidoRepository;
import com.sca.service.PedidoService;
import com.sca.service.BarrilService;
import com.sca.service.AccesorioService;
import com.sca.service.HistorialService;

@Service
public class PedidoServiceImpl extends ResponseEntityExceptionHandler implements PedidoService {

    Logger log = LoggerFactory.getLogger(PedidoServiceImpl.class);

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private BarrilService barrilService;

    @Autowired
    private AccesorioService accesorioService;

    // ✅ NUEVA DEPENDENCIA AGREGADA
    @Autowired
    private HistorialService historialService;

    private Respuesta respuesta;
    private String resp = "";

    // ✅ MÉTODO PARA REGISTRAR CAMBIOS EN HISTORIAL
    private void registrarCambio(Pedido pedido, String accion, String detalles, String usuario) {
        try {
            HistorialCambio cambio = new HistorialCambio(usuario, accion, detalles);
            String nuevoHistorial = historialService.agregarEntradaHistorial(
                pedido.getHistorialCambios(), cambio);
            pedido.setHistorialCambios(nuevoHistorial);
        } catch (Exception e) {
            log.error("Error registrando cambio en historial: {}", e.getMessage());
        }
    }

    // ✅ MÉTODO PARA REGISTRAR CAMBIOS CON DETALLES ESPECÍFICOS
    private void registrarCambioConDetalles(Pedido pedido, String accion, String detalles, String cambiosEspecificos, String usuario) {
        try {
            HistorialCambio cambio = new HistorialCambio(usuario, accion, detalles, cambiosEspecificos);
            String nuevoHistorial = historialService.agregarEntradaHistorial(
                pedido.getHistorialCambios(), cambio);
            pedido.setHistorialCambios(nuevoHistorial);
        } catch (Exception e) {
            log.error("Error registrando cambio en historial: {}", e.getMessage());
        }
    }

    // ✅ MÉTODO PARA DETECTAR CAMBIOS ESPECÍFICOS
    private String detectarCambios(Pedido original, Pedido nuevo) {
        StringBuilder cambios = new StringBuilder();
        
        // Cambio de estado
        if (original.getEstado() != null && nuevo.getEstado() != null && 
            !original.getEstado().equals(nuevo.getEstado())) {
            cambios.append("Estado: ").append(original.getEstado())
                   .append(" → ").append(nuevo.getEstado()).append("; ");
        }
        
        // Cambio en estado de pago
        if (original.getEstadoPago() != null && nuevo.getEstadoPago() != null &&
            !original.getEstadoPago().equals(nuevo.getEstadoPago())) {
            cambios.append("Pago: ").append(original.getEstadoPago())
                   .append(" → ").append(nuevo.getEstadoPago()).append("; ");
        }
        
        // Cambio en total
        if (original.getTotalGral() != null && nuevo.getTotalGral() != null &&
            !original.getTotalGral().equals(nuevo.getTotalGral())) {
            cambios.append("Total: $").append(original.getTotalGral())
                   .append(" → $").append(nuevo.getTotalGral()).append("; ");
        }
        
        return cambios.toString();
    }

    @ExceptionHandler(BindException.class)
    @Override
    public ResponseEntity<Object> save(Pedido pedido, BindingResult bindingResult) throws BindException {
        respuesta = new Respuesta();
        try {
            pedido.setId(null); // ✅ Forzar creación de nuevo pedido

            // ✅ REGISTRAR CREACIÓN EN HISTORIAL
            registrarCambio(pedido, "CREACIÓN", "Pedido creado inicialmente", "Sistema");

            Pedido pedidoGuardado = pedidoRepository.save(pedido);

            // ✅ Cambiar estado de barriles a "Alquilado"
            if (pedidoGuardado.getBarriles() != null && !pedidoGuardado.getBarriles().isEmpty()) {
                barrilService.marcarComoAlquilados(
                    pedidoGuardado.getBarriles().stream().map(b -> b.getId()).collect(Collectors.toList())
                );
            }

            // ✅ Cambiar estado de accesorios a "Alquilado"
            if (pedidoGuardado.getAccesorios() != null && !pedidoGuardado.getAccesorios().isEmpty()) {
                accesorioService.marcarComoAlquilados(
                    pedidoGuardado.getAccesorios().stream().map(a -> a.getId()).collect(Collectors.toList())
                );
            }

            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Se agregó un Pedido");
            respuesta.setData(pedidoGuardado);

        } catch (Exception e) {
            respuesta.setCodigo(String.valueOf(HttpStatus.BAD_REQUEST.value()));
            respuesta.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
            respuesta.setDescripcion("No se pudo agregar el Pedido");
            if (bindingResult.hasErrors()) {
                bindingResult.getAllErrors().forEach(r -> resp = resp + r.getDefaultMessage() + ";");
                respuesta.setData(resp);
                resp = "";
            } else {
                respuesta.setData(e.getMessage());
            }
            return handleExceptionInternal(e, respuesta, new HttpHeaders(), HttpStatus.BAD_REQUEST, null);
        }
        return new ResponseEntity<>(respuesta, null, HttpStatus.CREATED);
    }

    @Override
    public Respuesta delete(Long id) {
        respuesta = new Respuesta();
        try {
            pedidoRepository.deleteById(id);
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Se eliminó un Pedido");
            respuesta.setData(null);
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudo eliminar el Pedido");
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
            respuesta.setDescripcion("Se muestran todos los Pedidos");
            respuesta.setData(pedidoRepository.findAll());
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudieron mostrar los Pedidos");
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
            respuesta.setDescripcion("Datos del Pedido");
            respuesta.setData(pedidoRepository.findById(id).orElse(null));
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudieron mostrar los datos del Pedido");
            respuesta.setData(e.getMessage());
        }
        return respuesta;
    }

    @Override
    public ResponseEntity<Object> update(Pedido pedido, BindingResult bindingResult) throws BindException {
        respuesta = new Respuesta();
        try {
            // ✅ Obtener pedido original para comparar
            Pedido pedidoOriginal = pedidoRepository.findById(pedido.getId()).orElse(null);
            
            String cambiosDetectados = "";
            if (pedidoOriginal != null) {
                // ✅ DETECTAR CAMBIOS Y REGISTRARLOS
                cambiosDetectados = detectarCambios(pedidoOriginal, pedido);
                if (!cambiosDetectados.isEmpty()) {
                    registrarCambioConDetalles(pedido, "ACTUALIZACIÓN", 
                        "Pedido modificado", cambiosDetectados, "Usuario");
                }
            }

            Pedido pedidoGuardado = pedidoRepository.save(pedido);

            if (pedidoGuardado.getBarriles() != null && !pedidoGuardado.getBarriles().isEmpty()) {
                barrilService.marcarComoAlquilados(
                    pedidoGuardado.getBarriles().stream().map(b -> b.getId()).collect(Collectors.toList())
                );
            }
            if (pedidoGuardado.getAccesorios() != null && !pedidoGuardado.getAccesorios().isEmpty()) {
                accesorioService.marcarComoAlquilados(
                    pedidoGuardado.getAccesorios().stream().map(a -> a.getId()).collect(Collectors.toList())
                );
            }

            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Se modificaron los datos del Pedido");
            respuesta.setData(pedidoGuardado);

        } catch (Exception e) {
            respuesta.setCodigo(String.valueOf(HttpStatus.BAD_REQUEST.value()));
            respuesta.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
            respuesta.setDescripcion("No se pudo modificar el Pedido");
            if (bindingResult.hasErrors()) {
                bindingResult.getAllErrors().forEach(r -> resp = resp + r.getDefaultMessage() + ";");
                respuesta.setData(resp);
                resp = "";
            } else {
                respuesta.setData(e.getMessage());
            }
            return handleExceptionInternal(e, respuesta, new HttpHeaders(), HttpStatus.BAD_REQUEST, null);
        }
        return new ResponseEntity<>(respuesta, null, HttpStatus.CREATED);
    }

    @Override
    public Respuesta findByCliente(Long clienteId) {
        respuesta = new Respuesta();
        try {
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Pedidos del cliente " + clienteId);
            respuesta.setData(pedidoRepository.findByClienteId(clienteId));
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudieron obtener los pedidos");
            respuesta.setData(e.getMessage());
        }
        return respuesta;
    }

    @Override
    public Respuesta cancel(Long id) {
        respuesta = new Respuesta();
        try {
            Pedido pedido = pedidoRepository.findById(id).orElse(null);
            if (pedido != null) {
                if ("Pendiente".equalsIgnoreCase(pedido.getEstado())) {
                    // ✅ REGISTRAR CANCELACIÓN EN HISTORIAL
                    registrarCambio(pedido, "CANCELACIÓN", 
                        "Pedido cancelado y recursos liberados", "Sistema");
                    
                    pedido.setEstado("Cancelado");
                    pedidoRepository.save(pedido);

                    if (pedido.getBarriles() != null && !pedido.getBarriles().isEmpty()) {
                        barrilService.marcarComoCargados(
                            pedido.getBarriles().stream().map(b -> b.getId()).collect(Collectors.toList())
                        );
                    }

                    if (pedido.getAccesorios() != null && !pedido.getAccesorios().isEmpty()) {
                        accesorioService.marcarComoDisponibles(
                            pedido.getAccesorios().stream().map(a -> a.getId()).collect(Collectors.toList())
                        );
                    }

                    respuesta.setCodigo("200");
                    respuesta.setStatus("Ok");
                    respuesta.setDescripcion("Pedido cancelado correctamente y stock actualizado");
                    respuesta.setData(pedido);
                } else {
                    respuesta.setCodigo("400");
                    respuesta.setStatus("Error");
                    respuesta.setDescripcion("Solo se pueden cancelar pedidos en estado Pendiente");
                    respuesta.setData(null);
                }
            } else {
                respuesta.setCodigo("404");
                respuesta.setStatus("Not Found");
                respuesta.setDescripcion("Pedido no encontrado");
                respuesta.setData(null);
            }
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("Error al cancelar el pedido");
            respuesta.setData(e.getMessage());
        }
        return respuesta;
    }

    @Override
    public Respuesta updateEstado(Long id, String estado) {
        respuesta = new Respuesta();
        try {
            Pedido pedido = pedidoRepository.findById(id).orElse(null);
            if (pedido == null) {
                respuesta.setCodigo("404");
                respuesta.setStatus("Not Found");
                respuesta.setDescripcion("Pedido no encontrado");
                respuesta.setData(null);
                return respuesta;
            }

            // ✅ REGISTRAR CAMBIO DE ESTADO EN HISTORIAL
            String cambioEstado = "Estado: " + pedido.getEstado() + " → " + estado;
            registrarCambioConDetalles(pedido, "CAMBIO_ESTADO", 
                "Estado del pedido actualizado", cambioEstado, "Usuario");

            // Actualizar estado
            pedido.setEstado(estado);
            Pedido saved = pedidoRepository.save(pedido);

            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Estado del pedido actualizado");
            respuesta.setData(saved);
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudo actualizar el estado del pedido");
            respuesta.setData(e.getMessage());
        }
        return respuesta;
    }
}