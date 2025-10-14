package com.sca.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

import com.sca.model.Accesorio;
import com.sca.model.Respuesta;

public interface AccesorioService {

    public ResponseEntity<Object> save(Accesorio accesorio, BindingResult bindingResult) throws BindException;

    public ResponseEntity<Object> update(Accesorio accesorio, BindingResult bindingResult) throws BindException;

    public Respuesta delete(Long id);

    public Respuesta findAll();

    public Respuesta findById(Long id);

    public Respuesta findAccesoriosPorEstado(String estado);

	Respuesta findDisponiblesByNombre(String nombre);

    void marcarComoAlquilados(List<Long> accesorioIds);

    // 🔹 Nuevo método
    void marcarComoDisponibles(List<Long> accesorioIds);

    Respuesta findAccesorioPorEstado(String estado);

    // 🔹 Nuevo método para exportar a Excel
    ResponseEntity<byte[]> exportarAccesoriosExcel();

}
