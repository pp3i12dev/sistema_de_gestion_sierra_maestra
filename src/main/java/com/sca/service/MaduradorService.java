package com.sca.service;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

import com.sca.model.Madurador;
import com.sca.model.Respuesta;

public interface MaduradorService {

	public ResponseEntity<Object> save(Madurador madurador, BindingResult bindingResult) throws BindException;
	
	public ResponseEntity<Object> update(Madurador madurador, BindingResult bindingResult) throws BindException;
	
	public Respuesta delete(Long id);
	
	public Respuesta findAll();
	
	public Respuesta findById(Long id);

	public Respuesta finByEstado(String estado);
}