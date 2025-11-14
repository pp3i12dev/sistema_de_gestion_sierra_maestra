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
import com.sca.repository.PedidoRepository;
import com.sca.service.PedidoService;
import com.sca.service.BarrilService;
import com.sca.service.AccesorioService;

@Service
public class PedidoServiceImpl extends ResponseEntityExceptionHandler implements PedidoService {

    Logger log = LoggerFactory.getLogger(PedidoServiceImpl.class);

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private BarrilService barrilService;

    @Autowired
    private AccesorioService accesorioService;

    private Respuesta respuesta;
    private String resp = "";

    @ExceptionHandler(BindException.class)
    @Override
    public ResponseEntity<Object> save(Pedido pedido, BindingResult bindingResult) throws BindException {
        respuesta = new Respuesta();
        try {
            pedido.setId(null); // ✅ Forzar creación de nuevo pedido

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
