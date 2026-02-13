package com.sca.controller;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.sca.model.PedidoCliente;
import com.sca.model.Respuesta;
import com.sca.service.impl.PedidoClienteServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Api(tags = "Pedido Cliente")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@Slf4j
public class PedidoClienteController {

    @Autowired
    PedidoClienteServiceImpl pedidoClienteServiceImpl;

    @PostMapping(value = "/addPedidoCliente", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Agrega un Pedido de Cliente", notes = "Esta operación agrega un Pedido de Cliente a la base de datos")
    public ResponseEntity<Object> addPedidoCliente(@RequestBody @Validated PedidoCliente pedidoCliente, BindingResult bindingResult) throws BindException {
        return pedidoClienteServiceImpl.save(pedidoCliente, bindingResult);
    }

    @GetMapping(value = "/getAllPedidoCliente", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Consultar Pedidos de Cliente", notes = "Devuelve todos los pedidos de cliente")
    public Respuesta getAllPedidoCliente() {
        return pedidoClienteServiceImpl.findAll();
    }

    @GetMapping(value = "/getByIdPedidoCliente/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Consultar Pedido de Cliente por id", notes = "Consulta un Pedido de Cliente por su identificador")
    public Respuesta getByIdPedidoCliente(@PathParam("id") @PathVariable Long id) {
        return pedidoClienteServiceImpl.findById(id);
    }

    @DeleteMapping(value = "/deletePedidoCliente/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Eliminar un Pedido de Cliente", notes = "Elimina un Pedido de Cliente de la base de datos")
    public Respuesta deletePedidoCliente(@PathParam("id") @PathVariable Long id) {
        return pedidoClienteServiceImpl.delete(id);
    }

    @PutMapping(value = "/updatePedidoCliente", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Actualizar un Pedido de Cliente", notes = "Actualiza un Pedido de Cliente en la base de datos")
    public ResponseEntity<Object> updatePedidoCliente(@RequestBody PedidoCliente pedidoCliente, BindingResult bindingResult) throws BindException {
        return pedidoClienteServiceImpl.update(pedidoCliente, bindingResult);
    }

    @GetMapping(value = "/getPedidosClienteByNombre/{nombreCliente}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Consultar pedidos por nombre de cliente", notes = "Devuelve todos los pedidos de un cliente por nombre")
    public Respuesta getPedidosClienteByNombre(@PathVariable String nombreCliente) {
        return pedidoClienteServiceImpl.findByNombreCliente(nombreCliente);
    }

    @GetMapping(value = "/getPedidosClienteByEmail/{emailCliente}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Consultar pedidos por email de cliente", notes = "Devuelve todos los pedidos de un cliente por email")
    public Respuesta getPedidosClienteByEmail(@PathVariable String emailCliente) {
        return pedidoClienteServiceImpl.findByEmailCliente(emailCliente);
    }

    @GetMapping(value = "/getPedidosClienteByEstado/{estado}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Consultar pedidos por estado", notes = "Devuelve todos los pedidos con un estado específico")
    public Respuesta getPedidosClienteByEstado(@PathVariable String estado) {
        return pedidoClienteServiceImpl.findByEstado(estado);
    }

    @GetMapping(value = "/getPedidosClienteByEstadoPago/{estadoPago}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Consultar pedidos por estado de pago", notes = "Devuelve todos los pedidos con un estado de pago específico")
    public Respuesta getPedidosClienteByEstadoPago(@PathVariable String estadoPago) {
        return pedidoClienteServiceImpl.findByEstadoPago(estadoPago);
    }

    @PutMapping(value = "/cancelPedidoCliente/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Cancelar un Pedido de Cliente", notes = "Marca un pedido de cliente como Cancelado sin eliminarlo")
    public Respuesta cancelPedidoCliente(@PathVariable Long id) {
        return pedidoClienteServiceImpl.cancel(id);
    }

}
