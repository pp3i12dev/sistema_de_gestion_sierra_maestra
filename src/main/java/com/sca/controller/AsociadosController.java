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

import com.sca.model.Asociados;
import com.sca.model.Respuesta;
import com.sca.service.impl.AsociadosServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

// IMPORTACIONES NUEVAS
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.ArrayList;

@RestController
@Controller // ← ANOTACIÓN NUEVA para soportar formularios HTML
@Api(tags = "Asociados")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class AsociadosController {
	
	// Logger log = LoggerFactory.getLogger(String.class);
	
	@Autowired
	AsociadosServiceImpl asociadosServiceImpl;
	
	// =============================================
	// ✅ MÉTODOS EXISTENTES - NO MODIFICADOS
	// =============================================
	
	@PostMapping(value = "/addAsociados", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Agrega un Asociados", notes = "Esta operación agrega un Asociados a la base de datos")
	public ResponseEntity<Object> addAsociados(@RequestBody @Validated Asociados asociados, BindingResult bindingResult) throws BindException{
		return asociadosServiceImpl.save(asociados,bindingResult);
	}
	
	@GetMapping(value = "/getAllAsociados", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Consultar Asociados", notes = "Esta operación devuelve todos los Asociadoss a la base de datos")
	public Respuesta getAllAsociadoss() {
		try {
			return asociadosServiceImpl.findAll();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	@GetMapping(value = "/getByIdAsociados/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Consultar Asociados por id", notes = "Esta operación consulta un Asociados por su identificador personal")
	public Respuesta getByIdAsociados(@PathParam("id") @PathVariable Long id) {
		return asociadosServiceImpl.findById(id);
	}
	
	@DeleteMapping(value = "/deleteAsociados/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Eliminar un Asociados", notes = "Esta operación elimina un Asociados de la base de datos")
	public Respuesta deleteAsociados(@PathParam("id") @PathVariable Long id) {
		return asociadosServiceImpl.delete(id);
	}
	
	@PutMapping(value = "/updateAsociados", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Actualizar un Asociados", notes = "Esta operación actualiza un Asociados a la base de datos")
	public ResponseEntity<Object> updateAsociados(@RequestBody Asociados asociados, BindingResult bindingResult) throws BindException {
		return asociadosServiceImpl.update(asociados, bindingResult);
	}

	@GetMapping(value = "/contarAsociados", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Contar Asociados", notes = "Esta operación cuenta todos los Asociadoss en la base de datos")
	public Respuesta contarAsociados() {
		try {
			return asociadosServiceImpl.contarAsociados();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	// =============================================
	// 🆕 MÉTODOS NUEVOS PARA FORMULARIOS HTML
	// NO AFECTAN LOS MÉTODOS EXISTENTES
	// =============================================
	
	/**
	 * Maneja el guardado desde formularios HTML (Thymeleaf)
	 * Este método es NUEVO y trabaja con application/x-www-form-urlencoded
	 */
	@PostMapping(value = "/asociados/save", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String saveAsociadoFromForm(
			@ModelAttribute Asociados asociado,
			@RequestParam(value = "permisos", required = false) List<String> permisos,
			@RequestParam(value = "categoriaIds", required = false) List<Long> categoriaIds,
			BindingResult bindingResult) {
		
		try {
			// Procesar permisos - esto es NUEVO
			if (permisos != null) {
				asociado.setPermisosList(permisos);
			} else {
				asociado.setPermisosList(new ArrayList<>());
			}
			
			// Usar el servicio existente para guardar
			ResponseEntity<Object> response = asociadosServiceImpl.save(asociado, bindingResult);
			
			if (response.getStatusCode().is2xxSuccessful()) {
				// Redirigir al listado (usando HTMX)
				return "redirect:/asociados?success=true";
			} else {
				return "redirect:/asociados?error=true";
			}
			
		} catch (Exception e) {
			log.error("Error al guardar asociado desde formulario", e);
			return "redirect:/asociados?error=true";
		}
	}
}