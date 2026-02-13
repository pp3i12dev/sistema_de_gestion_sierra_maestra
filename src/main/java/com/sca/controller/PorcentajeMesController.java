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

import com.sca.model.PorcentajeMes;
import com.sca.model.Respuesta;
import com.sca.service.impl.PorcentajeMesServiceImpl;
import com.sca.service.impl.MesServiceImpl;
import com.sca.model.Mes;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(tags = "PorcentajeMes")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class PorcentajeMesController {
    
	// Logger log = LoggerFactory.getLogger(String.class);
	
	@Autowired
	PorcentajeMesServiceImpl porcentajeMesServiceImpl;

	@Autowired
	MesServiceImpl mesServiceImpl;
	
	@PostMapping(value = "/addPorcentajeMes", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Agrega una PorcentajeMes", notes = "Esta operación agrega una Porcentaje Mes a la base de datos")
	public ResponseEntity<Object> addPorcentajeMese(@RequestBody @Validated PorcentajeMes porcentajeMes, BindingResult bindingResult) throws BindException{
		
		System.out.println("=== DEBUG addPorcentajeMes ===");
		System.out.println("PorcentajeMes recibido: " + porcentajeMes);
		System.out.println("Porcentaje aumento: " + porcentajeMes.getPorcentaje_aumento());
		System.out.println("Año: " + porcentajeMes.getAno());
		
		if (porcentajeMes.getMes() != null) {
			System.out.println("Mes ID: " + porcentajeMes.getMes().getId());
			System.out.println("Mes objeto: " + porcentajeMes.getMes());
		} else {
			System.out.println("Mes es NULL");
		}
		
		// Si el mes solo tiene ID, cargar el objeto completo desde la base de datos
		if (porcentajeMes.getMes() != null && porcentajeMes.getMes().getId() > 0) {
			try {
				System.out.println("Buscando mes con ID: " + porcentajeMes.getMes().getId());
				Respuesta respuestaMes = mesServiceImpl.findById(porcentajeMes.getMes().getId());
				if (respuestaMes.getData() != null) {
					Object mesData = respuestaMes.getData();
					if (mesData instanceof Mes) {
						porcentajeMes.setMes((Mes) mesData);
						System.out.println("Mes cargado correctamente: " + porcentajeMes.getMes().getNombre());
					} else if (mesData instanceof java.util.Optional) {
						java.util.Optional<Mes> mesOptional = (java.util.Optional<Mes>) mesData;
						if (mesOptional.isPresent()) {
							porcentajeMes.setMes(mesOptional.get());
							System.out.println("Mes cargado desde Optional: " + porcentajeMes.getMes().getNombre());
						}
					}
				} else {
					System.out.println("No se encontró el mes con ID: " + porcentajeMes.getMes().getId());
				}
			} catch (Exception e) {
				System.out.println("Error al cargar el mes: " + e.getMessage());
				e.printStackTrace();
			}
		}
		
		if (bindingResult.hasErrors()) {
			System.out.println("Errores de validación: " + bindingResult.getAllErrors());
			bindingResult.getAllErrors().forEach(error -> {
				System.out.println("Error: " + error.getDefaultMessage());
			});
		}
		
		System.out.println("Llamando al servicio save...");
		return porcentajeMesServiceImpl.save(porcentajeMes, bindingResult);
	}
	
	@GetMapping(value = "/getAllPorcentajeMess", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Consultar PorcentajeMess", notes = "Esta operación devuelve todos los Porcentaje Mes a la base de datos")
	public Respuesta getAllPorcentajeMess() {
		return porcentajeMesServiceImpl.findAll();
	}
	
	@GetMapping(value = "/getByIdPorcentajeMes/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Consultar PorcentajeMes por id", notes = "Esta operación consulta un Porcentaje Mes por su identificador personal")
	public Respuesta getByIdPorcentajeMes(@PathParam("id") @PathVariable Long id) {
		return porcentajeMesServiceImpl.findById(id);
	}
	
	@DeleteMapping(value = "/deletePorcentajeMes/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Eliminar un PorcentajeMes", notes = "Esta operación elimina un Porcentaje Mes de la base de datos")
	public Respuesta deletePorcentajeMes(@PathParam("id") @PathVariable Long id) { 
		return porcentajeMesServiceImpl.delete(id);
	}
	
	@PutMapping(value = "/updatePorcentajeMes", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Actualizar un PorcentajeMes", notes = "Esta operación actualiza un Porcentaje Mes a la base de datos")
	public ResponseEntity<Object> updatePorcentajeMes(@RequestBody PorcentajeMes porcentajeMes, BindingResult bindingResult) throws BindException {
		
		System.out.println("=== DEBUG updatePorcentajeMes ===");
		System.out.println("PorcentajeMes recibido para actualizar: " + porcentajeMes);
		System.out.println("ID: " + porcentajeMes.getId());
		System.out.println("Porcentaje aumento: " + porcentajeMes.getPorcentaje_aumento());
		System.out.println("Año: " + porcentajeMes.getAno());
		
		if (porcentajeMes.getMes() != null) {
			System.out.println("Mes ID: " + porcentajeMes.getMes().getId());
		}
		
		// Si el mes solo tiene ID, cargar el objeto completo desde la base de datos
		if (porcentajeMes.getMes() != null && porcentajeMes.getMes().getId() > 0) {
			try {
				System.out.println("Buscando mes con ID: " + porcentajeMes.getMes().getId());
				Respuesta respuestaMes = mesServiceImpl.findById(porcentajeMes.getMes().getId());
				if (respuestaMes.getData() != null) {
					Object mesData = respuestaMes.getData();
					if (mesData instanceof Mes) {
						porcentajeMes.setMes((Mes) mesData);
						System.out.println("Mes cargado correctamente: " + porcentajeMes.getMes().getNombre());
					} else if (mesData instanceof java.util.Optional) {
						java.util.Optional<Mes> mesOptional = (java.util.Optional<Mes>) mesData;
						if (mesOptional.isPresent()) {
							porcentajeMes.setMes(mesOptional.get());
							System.out.println("Mes cargado desde Optional: " + porcentajeMes.getMes().getNombre());
						}
					}
				} else {
					System.out.println("No se encontró el mes con ID: " + porcentajeMes.getMes().getId());
				}
			} catch (Exception e) {
				System.out.println("Error al cargar el mes: " + e.getMessage());
				e.printStackTrace();
			}
		}
		
		if (bindingResult.hasErrors()) {
			System.out.println("Errores de validación: " + bindingResult.getAllErrors());
		}
		
		System.out.println("Llamando al servicio update...");
		return porcentajeMesServiceImpl.update(porcentajeMes, bindingResult);
	}
}