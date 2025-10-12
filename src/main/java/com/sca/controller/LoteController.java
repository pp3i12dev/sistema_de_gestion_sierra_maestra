package com.sca.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sca.model.Lote;
import com.sca.model.Respuesta;
import com.sca.service.impl.LoteServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(tags = "Lote")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class LoteController {

	// Logger log = LoggerFactory.getLogger(String.class);
	
	@Autowired
	LoteServiceImpl lotesServiceImpl;
	
	@PostMapping(value = "/addLote", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Agrega un Lote", notes = "Esta operación agrega un Lote a la base de datos")
	public ResponseEntity<Object> addLote(@RequestBody @Validated Lote lote, BindingResult bindingResult) throws BindException{
		return lotesServiceImpl.save(lote,bindingResult);
	}
	
	@GetMapping(value = "/getAllLote", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Consultar Lote", notes = "Esta operación devuelve todos los Lote a la base de datos")
	public Respuesta getAllLote() {
		return lotesServiceImpl.findAll();
	}
	
	@GetMapping(value = "/getByIdLote/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Consultar Lote por id", notes = "Esta operación consulta un Lote por su identificador personal")
	public Respuesta getByIdLote(@PathParam("id") @PathVariable Long id) {
		return lotesServiceImpl.findById(id);
	}
	
	@DeleteMapping(value = "/deleteLote/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Eliminar un Lotes", notes = "Esta operación elimina un Lote de la base de datos")
	public Respuesta deleteLote(@PathParam("id") @PathVariable Long id) {
		return lotesServiceImpl.delete(id);
	}
	
	@PutMapping(value = "/updateLote", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Actualizar un Lote", notes = "Esta operación actualiza un Lote a la base de datos")
	public ResponseEntity<Object> updateLote(@RequestBody Lote lote, BindingResult bindingResult) throws BindException {
		return lotesServiceImpl.update(lote, bindingResult);
	}

	@GetMapping(value = "/lotes/buscarPorId", produces = MediaType.TEXT_HTML_VALUE)
	public String buscarPorId(@RequestParam Long id, Model model) {
		Lote lote = (Lote) lotesServiceImpl.findById(id).getData();
		model.addAttribute("lote", lote);
		return "lotes/fragments :: view";
	}

	@GetMapping(value = "/findLotesPorEstado/{estado}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Respuesta findLotesPorEstado(@PathVariable String estado) {
		return lotesServiceImpl.findLotesPorEstado(estado);
	}


	@GetMapping("/lotes/exportarExcel/{estado}")
	public ResponseEntity<byte[]> exportarLotesPorEstadoExcel(@PathVariable String estado) throws IOException {
		ByteArrayInputStream stream = lotesServiceImpl.exportarPorEstadoAExcel(estado);


		org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=lotes_" + estado + ".xlsx");

		return ResponseEntity
				.ok()
				.headers(headers)
				.body(stream.readAllBytes());
	}



}
