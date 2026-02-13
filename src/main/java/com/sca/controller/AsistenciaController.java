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

import com.sca.model.Asistencia;
import com.sca.model.Respuesta;
import com.sca.service.impl.AsistenciaServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(tags = "Asistencia")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class AsistenciaController {

	// Logger log = LoggerFactory.getLogger(String.class);
	
	@Autowired
	AsistenciaServiceImpl asistenciasServiceImpl;
	
	@PostMapping(value = "/addAsistencia", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Agrega un Asistencia", notes = "Esta operación agrega un Asistencia a la base de datos")
	public ResponseEntity<Object> addAsistencia(@RequestBody @Validated Asistencia asistencia, BindingResult bindingResult) throws BindException{
		return asistenciasServiceImpl.save(asistencia,bindingResult);
	}
	
	@GetMapping(value = "/getAllAsistencia", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Consultar Asistencia", notes = "Esta operación devuelve todos los Asistencia a la base de datos")
	public Respuesta getAllAsistencia() {
		return asistenciasServiceImpl.findAll();
	}
	
	@GetMapping(value = "/getByIdAsistencia/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Consultar Asistencia por id", notes = "Esta operación consulta un Asistencia por su identificador personal")
	public Respuesta getByIdAsistencia(@PathParam("id") @PathVariable Long id) {
		return asistenciasServiceImpl.findById(id);
	}
	
	@DeleteMapping(value = "/deleteAsistencia/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Eliminar un Asistencias", notes = "Esta operación elimina un Asistencia de la base de datos")
	public Respuesta deleteAsistencia(@PathParam("id") @PathVariable Long id) {
		return asistenciasServiceImpl.delete(id);
	}
	
	@PutMapping(value = "/updateAsistencia", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Actualizar un Asistencia", notes = "Esta operación actualiza un Asistencia a la base de datos")
	public ResponseEntity<Object> updateAsistencia(@RequestBody Asistencia asistencia, BindingResult bindingResult) throws BindException {
		return asistenciasServiceImpl.update(asistencia, bindingResult);
	}
}
