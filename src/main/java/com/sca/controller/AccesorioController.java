package com.sca.controller;

import javax.websocket.server.PathParam;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sca.model.Accesorio;
import com.sca.model.Respuesta;
import com.sca.service.impl.AccesorioServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/accesorios")   // <- esto define el prefijo para todos los endpoints
@Api(tags = "Accesorio")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class AccesorioController {

	// Logger log = LoggerFactory.getLogger(String.class);
	
	@Autowired
	AccesorioServiceImpl accesoriosServiceImpl;
	
	@PostMapping(value = "/addAccesorio", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Agrega un Accesorio", notes = "Esta operación agrega un Accesorio a la base de datos")
	public ResponseEntity<Object> addAccesorio(@RequestBody @Validated Accesorio accesorio, BindingResult bindingResult) throws BindException{
		return accesoriosServiceImpl.save(accesorio,bindingResult);
	}
	
	@GetMapping(value = "/getAllAccesorio", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Consultar Accesorio", notes = "Esta operación devuelve todos los Accesorio a la base de datos")
	public Respuesta getAllAccesorio() {
		return accesoriosServiceImpl.findAll();
	}
	
	@GetMapping(value = "/getByIdAccesorio/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Consultar Accesorio por id", notes = "Esta operación consulta un Accesorio por su identificador personal")
	public Respuesta getByIdAccesorio(@PathVariable Long id) {
		return accesoriosServiceImpl.findById(id);
	}
	
	@DeleteMapping(value = "/deleteAccesorio/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Eliminar un Accesorios", notes = "Esta operación elimina un Accesorio de la base de datos")
	public Respuesta deleteAccesorio(@PathParam("id") @PathVariable Long id) {
		return accesoriosServiceImpl.delete(id);
	}
	
	@PutMapping(value = "/updateAccesorio", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Actualizar un Accesorio", notes = "Esta operación actualiza un Accesorio a la base de datos")
	public ResponseEntity<Object> updateAccesorio(@RequestBody Accesorio accesorio, BindingResult bindingResult) throws BindException {
		return accesoriosServiceImpl.update(accesorio, bindingResult);
	}

	@GetMapping(value = "/findAccesorioPorEstado/{estado}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Respuesta findAccesorioPorEstado(@PathVariable String estado) {
    	return accesoriosServiceImpl.findAccesorioPorEstado(estado);
	}


	@GetMapping(value = "/exportarExcel", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
	@ApiOperation(value = "Exportar accesorios a Excel", notes = "Descarga un archivo Excel con todos los accesorios")
	public ResponseEntity<byte[]> exportarAccesoriosExcel() {
		return accesoriosServiceImpl.exportarAccesoriosExcel();
	}



}
