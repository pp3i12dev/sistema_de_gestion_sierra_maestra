package com.sca.controller;

import javax.websocket.server.PathParam;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
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

import com.sca.model.AsociadosCondicion;
import com.sca.model.Respuesta;
import com.sca.service.impl.AsociadosCondicionServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(tags = "AsociadosCondicion")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class AsociadosCondicionController {

    // Logger log = LoggerFactory.getLogger(String.class);
	
	@Autowired
	AsociadosCondicionServiceImpl asociadosCondicionServiceImpl;
	
	@PostMapping(value = "/addAsociadosCondicion", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Agrega un AsociadosCondicion", notes = "Esta operación agrega un AsociadosCondicion a la base de datos")
	public ResponseEntity<Object> addAsociadosCondicion(@RequestBody @Validated AsociadosCondicion asociadosCondicion, BindingResult bindingResult) throws BindException{
		return asociadosCondicionServiceImpl.save(asociadosCondicion,bindingResult);
	}
	
	@GetMapping(value = "/getAllAsociadosCondicion", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Consultar AsociadosCondicion", notes = "Esta operación devuelve todos los AsociadosCondicion a la base de datos")
	public Respuesta getAllAsociadosCondicion() {
		return asociadosCondicionServiceImpl.findAll();
	}
	
	@GetMapping(value = "/getByIdAsociadosCondicion/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Consultar AsociadosCondicion por id", notes = "Esta operación consulta un AsociadosCondicion por su identificador personal")
	public Respuesta getByIdAsociadosCondicion(@PathParam("id") @PathVariable Long id) {
		return asociadosCondicionServiceImpl.findById(id);
	}
	
	@DeleteMapping(value = "/deleteAsociadosCondicion/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Eliminar un AsociadosCondicion", notes = "Esta operación elimina un AsociadosCondicion de la base de datos")
	public Respuesta deleteAsociadosCondicion(@PathParam("id") @PathVariable Long id) {
		return asociadosCondicionServiceImpl.delete(id);
	}
	
	@PutMapping(value = "/updateAsociadosCondicion", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Actualizar un AsociadosCondicion", notes = "Esta operación actualiza un AsociadosCondicion a la base de datos")
	public ResponseEntity<Object> updateAsociadosCondicion( @RequestBody AsociadosCondicion asociadosCondicion, BindingResult bindingResult) throws BindException {
		return asociadosCondicionServiceImpl.update(asociadosCondicion, bindingResult);
	}
}
