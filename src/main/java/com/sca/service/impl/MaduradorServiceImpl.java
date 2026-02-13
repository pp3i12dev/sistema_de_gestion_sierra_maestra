package com.sca.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.sca.model.Madurador;
import com.sca.model.Respuesta;
import com.sca.repository.MaduradorRepository;
import com.sca.service.MaduradorService;

@Service
public class MaduradorServiceImpl extends ResponseEntityExceptionHandler implements MaduradorService{

Logger log = LoggerFactory.getLogger(String.class);
	
	@Autowired
	MaduradorRepository maduradorRepository;

	Respuesta respuesta;

	String resp = "";

	private static final Set<String> ALLOWED_STATES = new HashSet<>(Arrays.asList(
			"Activo","Cargado","Sucio","Mantenimiento","Inactivo"
	));

	//El ExceptionHandler me sirve para recuperar o en viar el status del error del pedido
	@ExceptionHandler(BindException.class)
	@Override
	public ResponseEntity<Object> save(Madurador madurador, BindingResult bindingResult) throws BindException {
		respuesta = new Respuesta();
		try {
			// Set default estado if not provided
			if (madurador.getEstado() == null || madurador.getEstado().trim().isEmpty()) {
				madurador.setEstado("Activo");
			} else if (!ALLOWED_STATES.contains(madurador.getEstado())) {
				respuesta.setCodigo(String.valueOf(HttpStatus.BAD_REQUEST.value()));
				respuesta.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
				respuesta.setDescripcion("Estado no válido. Valores permitidos: " + ALLOWED_STATES);
				respuesta.setData(madurador.getEstado());
				return handleExceptionInternal(new IllegalArgumentException("Estado no válido"), respuesta, new HttpHeaders(), HttpStatus.BAD_REQUEST, null);
			}
			respuesta.setCodigo("200");
			respuesta.setStatus("Ok");
			respuesta.setDescripcion("Se agrego una Madurador");
			respuesta.setData(maduradorRepository.save(madurador));
		} catch (Exception e) {
			respuesta.setCodigo(String.valueOf(HttpStatus.BAD_REQUEST.value()));
			respuesta.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
			respuesta.setDescripcion("No se pudo agregar el Madurador");
			if (bindingResult.hasErrors()) {
				bindingResult.getAllErrors().forEach(r -> {
					resp = resp + r.getDefaultMessage() + ";";
				});
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
		System.out.println(id);
		try {
			Madurador madurador = maduradorRepository.findById(id).orElse(null);
			if (madurador == null) {
				respuesta.setCodigo("404");
				respuesta.setStatus("Error");
				respuesta.setDescripcion("Madurador no encontrado");
				respuesta.setData(null);
				return respuesta;
			}
			madurador.setEstado("Inactivo");
			maduradorRepository.save(madurador);
			respuesta.setCodigo("200");
			respuesta.setStatus("Ok");
			respuesta.setDescripcion("Se inactivó el Madurador");
			respuesta.setData(madurador);
		} catch (Exception e) {
			respuesta.setCodigo("400");
			respuesta.setStatus("Error");
			respuesta.setDescripcion("No se pudo eliminar el Madurador");
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
			respuesta.setDescripcion("Se muestran todos los Maduradores");
			respuesta.setData(maduradorRepository.findAll());
		} catch (Exception e) {
			respuesta.setCodigo("400");
			respuesta.setStatus("Error");
			respuesta.setDescripcion("No se pudieron mostrar los Maduradores");
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
			respuesta.setDescripcion("Datos del Madurador");
			// Return the entity or null so Thymeleaf expressions like ${madurador.id} work
			respuesta.setData(maduradorRepository.findById(id).orElse(null));
		} catch (Exception e) {
			respuesta.setCodigo("400");
			respuesta.setStatus("Error");
			respuesta.setDescripcion("No se pudieron mostrar los datos del Madurador");
			respuesta.setData(e.getMessage());
		}
		return respuesta;
	}

	@Override
	public ResponseEntity<Object> update(Madurador madurador, BindingResult bindingResult) throws BindException {
		respuesta = new Respuesta();
		try {
			// Valida el estado si lo obtiene
			if (madurador.getEstado() != null && !madurador.getEstado().trim().isEmpty() && !ALLOWED_STATES.contains(madurador.getEstado())) {
				respuesta.setCodigo(String.valueOf(HttpStatus.BAD_REQUEST.value()));
				respuesta.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
				respuesta.setDescripcion("Estado no válido. Valores permitidos: " + ALLOWED_STATES);
				respuesta.setData(madurador.getEstado());
				return handleExceptionInternal(new IllegalArgumentException("Estado no válido"), respuesta, new HttpHeaders(), HttpStatus.BAD_REQUEST, null);
			}

			// Mergea con la entidad existente para evitar campos nulos
			Madurador toSave = madurador;
			if (madurador.getId() != null) {
				Madurador existing = maduradorRepository.findById(madurador.getId()).orElse(null);
				if (existing != null) {
					if (madurador.getLitros() == null) madurador.setLitros(existing.getLitros());
					if (madurador.getEstado() == null) madurador.setEstado(existing.getEstado());
					if (madurador.getNotas() == null) madurador.setNotas(existing.getNotas());
					if (madurador.getLote() == null) madurador.setLote(existing.getLote());
					toSave = madurador;
				}
			}

			respuesta.setCodigo("200");
			respuesta.setStatus("Ok");
			respuesta.setDescripcion("Se modificaron los datos del Madurador");
			respuesta.setData(maduradorRepository.save(toSave));
		} catch (Exception e) {
			respuesta.setCodigo(String.valueOf(HttpStatus.BAD_REQUEST.value()));
			respuesta.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
			respuesta.setDescripcion("No se pudo modificar el Madurador");
			if (bindingResult.hasErrors()) {
				bindingResult.getAllErrors().forEach(r -> {
					resp = resp + r.getDefaultMessage() + ";";
				});
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
	public Respuesta finByEstado(String estado) {
		respuesta = new Respuesta();
		try {
			respuesta.setCodigo("200");
			respuesta.setStatus("Ok");
			respuesta.setDescripcion("Se muestran todos los Maduradores con el estado "+estado);
			List<Madurador> filtered = maduradorRepository.findAll().stream()
				.filter(a -> a.getEstado() != null && a.getEstado().equalsIgnoreCase(estado))
				.collect(Collectors.toList());
			respuesta.setData(filtered);
		} catch (Exception e) {
			respuesta.setCodigo("400");
			respuesta.setStatus("Error");
			respuesta.setDescripcion("No se pudieron filtrar los Maduradores");
			respuesta.setData(e.getMessage());
		}
		return respuesta;
	}
}
