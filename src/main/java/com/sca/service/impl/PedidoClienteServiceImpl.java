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

import com.sca.model.PedidoCliente;
import com.sca.model.Respuesta;
import com.sca.repository.PedidoClienteRepository;
import com.sca.service.PedidoClienteService;

@Service
public class PedidoClienteServiceImpl extends ResponseEntityExceptionHandler implements PedidoClienteService {

    Logger log = LoggerFactory.getLogger(String.class);

    @Autowired
    PedidoClienteRepository pedidoClienteRepository;

    Respuesta respuesta;
    String resp = "";

    @ExceptionHandler(BindException.class)
    @Override
    public ResponseEntity<Object> save(PedidoCliente pedidoCliente, BindingResult bindingResult) throws BindException {
        respuesta = new Respuesta();
        try {
            // 🔹 VALIDACIÓN DE NEGOCIO: Verificar que si hay accesorios, debe haber al menos una cerveza
            boolean tieneAccesorios = pedidoCliente.getAccesorios() != null && !pedidoCliente.getAccesorios().isEmpty();
            boolean tieneCervezas = pedidoCliente.getCervezas() != null && !pedidoCliente.getCervezas().isEmpty();
            
            if (tieneAccesorios && !tieneCervezas) {
                respuesta.setCodigo(String.valueOf(HttpStatus.BAD_REQUEST.value()));
                respuesta.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
                respuesta.setDescripcion("No se puede crear un pedido de accesorios sin al menos una cerveza");
                respuesta.setData("Los accesorios deben ir acompañados de cerveza");
                return new ResponseEntity<Object>(respuesta, new HttpHeaders(), HttpStatus.BAD_REQUEST);
            }
            
            // Limpiar relaciones para evitar errores de objetos transitorios
            pedidoCliente.setAccesorios(null);
            pedidoCliente.setCervezas(null);
            pedidoCliente.setBarriles(null);
            
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Se agregó un Pedido de Cliente");
            respuesta.setData(pedidoClienteRepository.save(pedidoCliente));
        } catch (Exception e) {
            respuesta.setCodigo(String.valueOf(HttpStatus.BAD_REQUEST.value()));
            respuesta.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
            respuesta.setDescripcion("No se pudo agregar el Pedido de Cliente");
            if (bindingResult != null && bindingResult.hasErrors()) {
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
            PedidoCliente pedidoCliente = pedidoClienteRepository.findById(id).get();
            pedidoClienteRepository.deleteById(id);
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Se eliminó un Pedido de Cliente");
            respuesta.setData(pedidoCliente);
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudo eliminar el Pedido de Cliente");
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
            respuesta.setDescripcion("Se muestran todos los Pedidos de Cliente");
            respuesta.setData(pedidoClienteRepository.findAll());
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudieron mostrar los Pedidos de Cliente");
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
            respuesta.setDescripcion("Datos del Pedido de Cliente");
            respuesta.setData(pedidoClienteRepository.findById(id).orElse(null));
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudieron mostrar los datos del Pedido de Cliente");
            respuesta.setData(e.getMessage());
        }
        return respuesta;
    }

    @Override
    public ResponseEntity<Object> update(PedidoCliente pedidoCliente, BindingResult bindingResult) throws BindException {
        respuesta = new Respuesta();
        try {
            // 🔹 VALIDACIÓN DE NEGOCIO: Verificar que si hay accesorios, debe haber al menos una cerveza
            boolean tieneAccesorios = pedidoCliente.getAccesorios() != null && !pedidoCliente.getAccesorios().isEmpty();
            boolean tieneCervezas = pedidoCliente.getCervezas() != null && !pedidoCliente.getCervezas().isEmpty();
            
            if (tieneAccesorios && !tieneCervezas) {
                respuesta.setCodigo(String.valueOf(HttpStatus.BAD_REQUEST.value()));
                respuesta.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
                respuesta.setDescripcion("No se puede crear un pedido de accesorios sin al menos una cerveza");
                respuesta.setData("Los accesorios deben ir acompañados de cerveza");
                return new ResponseEntity<Object>(respuesta, new HttpHeaders(), HttpStatus.BAD_REQUEST);
            }
            
            // Limpiar relaciones para evitar errores de objetos transitorios
            pedidoCliente.setAccesorios(null);
            pedidoCliente.setCervezas(null);
            pedidoCliente.setBarriles(null);
            
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Se modificaron los datos del Pedido de Cliente");
            respuesta.setData(pedidoClienteRepository.save(pedidoCliente));
        } catch (Exception e) {
            respuesta.setCodigo(String.valueOf(HttpStatus.BAD_REQUEST.value()));
            respuesta.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
            respuesta.setDescripcion("No se pudo modificar el Pedido de Cliente");
            if (bindingResult != null && bindingResult.hasErrors()) {
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
    public Respuesta findByNombreCliente(String nombreCliente) {
        respuesta = new Respuesta();
        try {
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Pedidos del cliente: " + nombreCliente);
            respuesta.setData(pedidoClienteRepository.findByNombreClienteContainingIgnoreCase(nombreCliente));
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudieron obtener los pedidos del cliente");
            respuesta.setData(e.getMessage());
        }
        return respuesta;
    }

    @Override
    public Respuesta findByEmailCliente(String emailCliente) {
        respuesta = new Respuesta();
        try {
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Pedidos del email: " + emailCliente);
            respuesta.setData(pedidoClienteRepository.findByEmailClienteContainingIgnoreCase(emailCliente));
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudieron obtener los pedidos del email");
            respuesta.setData(e.getMessage());
        }
        return respuesta;
    }

    @Override
    public Respuesta findByEstado(String estado) {
        respuesta = new Respuesta();
        try {
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Pedidos con estado: " + estado);
            respuesta.setData(pedidoClienteRepository.findByEstado(estado));
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudieron obtener los pedidos por estado");
            respuesta.setData(e.getMessage());
        }
        return respuesta;
    }

    @Override
    public Respuesta findByEstadoPago(String estadoPago) {
        respuesta = new Respuesta();
        try {
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Pedidos con estado de pago: " + estadoPago);
            respuesta.setData(pedidoClienteRepository.findByEstadoPago(estadoPago));
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudieron obtener los pedidos por estado de pago");
            respuesta.setData(e.getMessage());
        }
        return respuesta;
    }

    @Override
    public Respuesta cancel(Long id) {
        respuesta = new Respuesta();
        try {
            PedidoCliente pedidoCliente = pedidoClienteRepository.findById(id).orElse(null);
            if (pedidoCliente != null) {
                if ("Pendiente".equalsIgnoreCase(pedidoCliente.getEstado())) {
                    pedidoCliente.setEstado("Cancelado");
                    pedidoClienteRepository.save(pedidoCliente);
                    respuesta.setCodigo("200");
                    respuesta.setStatus("Ok");
                    respuesta.setDescripcion("Pedido de cliente cancelado correctamente");
                    respuesta.setData(pedidoCliente);
                } else {
                    respuesta.setCodigo("400");
                    respuesta.setStatus("Error");
                    respuesta.setDescripcion("Solo se pueden cancelar pedidos en estado Pendiente");
                    respuesta.setData(null);
                }
            } else {
                respuesta.setCodigo("404");
                respuesta.setStatus("Not Found");
                respuesta.setDescripcion("Pedido de cliente no encontrado");
                respuesta.setData(null);
            }
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("Error al cancelar el pedido de cliente");
            respuesta.setData(e.getMessage());
        }
        return respuesta;
    }
}
