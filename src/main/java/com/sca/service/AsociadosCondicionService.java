package com.sca.service;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

import com.sca.model.AsociadosCondicion;
import com.sca.model.Respuesta;

public interface AsociadosCondicionService {
	
	public ResponseEntity<Object> save(AsociadosCondicion asociadosCondicion, BindingResult bindingResult) throws BindException;
	
	public ResponseEntity<Object> update(AsociadosCondicion asociadosCondicion, BindingResult bindingResult) throws BindException;
	
	public Respuesta delete(Long id);
	
	public Respuesta findAll();
	
	public Respuesta findById(Long id);
	
	
}
