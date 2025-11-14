package com.sca.controller;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.sca.model.Pedido;
import com.sca.model.Respuesta;
import com.sca.service.impl.PedidoServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(tags = "Pedido")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@Slf4j
public class PedidoController {

    @Autowired
    PedidoServiceImpl pedidosServiceImpl;

    @PostMapping(value = "/addPedido", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Agrega un Pedido", notes = "Esta operación agrega un Pedido a la base de datos")
    public ResponseEntity<Object> addPedido(@RequestBody @Validated Pedido pedido, BindingResult bindingResult) throws BindException {
        return pedidosServiceImpl.save(pedido, bindingResult);
    }

    @GetMapping(value = "/getAllPedido", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Consultar Pedido", notes = "Devuelve todos los pedidos")
    public Respuesta getAllPedido() {
        return pedidosServiceImpl.findAll();
    }

    @GetMapping(value = "/getByIdPedido/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Consultar Pedido por id", notes = "Consulta un Pedido por su identificador")
    public Respuesta getByIdPedido(@PathParam("id") @PathVariable Long id) {
        return pedidosServiceImpl.findById(id);
    }

    @DeleteMapping(value = "/deletePedido/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Eliminar un Pedido", notes = "Elimina un Pedido de la base de datos")
    public Respuesta deletePedido(@PathVariable Long id) {
        return pedidosServiceImpl.delete(id);
    }

    @PutMapping(value = "/updatePedido", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Actualizar un Pedido", notes = "Actualiza un Pedido en la base de datos")
    public ResponseEntity<Object> updatePedido(@RequestBody Pedido pedido, BindingResult bindingResult) throws BindException {
        return pedidosServiceImpl.update(pedido, bindingResult);
    }

    // 🔹 Nuevo: pedidos por cliente
    @GetMapping(value = "/getPedidosByCliente/{clienteId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Consultar pedidos por cliente", notes = "Devuelve todos los pedidos de un cliente")
    public Respuesta getPedidosByCliente(@PathVariable Long clienteId) {
        return pedidosServiceImpl.findByCliente(clienteId);
    }

    // 🔹 Nuevo: cancelar pedido
    @PutMapping(value = "/cancelPedido/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Cancelar un Pedido", notes = "Marca un pedido como Cancelado sin eliminarlo")
    public Respuesta cancelPedido(@PathVariable Long id) {
        return pedidosServiceImpl.cancel(id);
    }

    // 🔹 Nuevo: actualizar solo estado
    @PutMapping(value = "/updatePedidoEstado/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Actualizar estado del Pedido", notes = "Actualiza únicamente el estado de un pedido")
    public Respuesta updateEstadoPedido(@PathVariable Long id, @RequestParam String estado) {
        return pedidosServiceImpl.updateEstado(id, estado);
    }
}
