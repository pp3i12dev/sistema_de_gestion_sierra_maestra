package com.sca.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

import com.sca.model.Cerveza;
import com.sca.model.Respuesta;

public interface CervezaService {

	public ResponseEntity<Object> save(Cerveza cerveza, BindingResult bindingResult) throws BindException;
	
	public ResponseEntity<Object> update(Cerveza cerveza, BindingResult bindingResult) throws BindException;
	
	public Respuesta delete(Long id);
	
	public Respuesta findAll();
	
	public Respuesta findById(Long id);

	public Respuesta findByEstado(String estado);

	ByteArrayInputStream exportarPorEstadoAExcel(String estado) throws IOException;

}