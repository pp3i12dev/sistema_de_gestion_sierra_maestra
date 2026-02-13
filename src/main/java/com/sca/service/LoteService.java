package com.sca.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

import com.sca.model.Lote;
import com.sca.model.Respuesta;

public interface LoteService {

	public ResponseEntity<Object> save(Lote lote, BindingResult bindingResult) throws BindException;
	
	public ResponseEntity<Object> update(Lote lote, BindingResult bindingResult) throws BindException;
	
	public Respuesta delete(Long id);
	
	public Respuesta findAll();
	
	public Respuesta findById(Long id);

	ByteArrayInputStream exportarPorEstadoAExcel(String estado) throws IOException;

}