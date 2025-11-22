package com.sca.service;

import com.sca.model.HistorialCambio;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HistorialService {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public String agregarEntradaHistorial(String historialExistente, HistorialCambio nuevaEntrada) {
        try {
            List<HistorialCambio> historial = new ArrayList<>();
            
            // Si ya existe historial, cargarlo
            if (historialExistente != null && !historialExistente.trim().isEmpty()) {
                try {
                    historial = objectMapper.readValue(historialExistente, 
                        new TypeReference<List<HistorialCambio>>() {});
                } catch (Exception e) {
                    // Si hay error al parsear, empezar con lista vacía
                    historial = new ArrayList<>();
                }
            }
            
            // Agregar nueva entrada al inicio (más reciente primero)
            historial.add(0, nuevaEntrada);
            
            // Limitar a últimas 50 entradas
            if (historial.size() > 50) {
                historial = historial.subList(0, 50);
            }
            
            return objectMapper.writeValueAsString(historial);
            
        } catch (Exception e) {
            // En caso de error, crear historial nuevo
            try {
                List<HistorialCambio> nuevoHistorial = new ArrayList<>();
                nuevoHistorial.add(nuevaEntrada);
                return objectMapper.writeValueAsString(nuevoHistorial);
            } catch (Exception ex) {
                return "[]";
            }
        }
    }
    
    public List<HistorialCambio> obtenerHistorial(String historialJson) {
        try {
            if (historialJson == null || historialJson.trim().isEmpty()) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(historialJson, 
                new TypeReference<List<HistorialCambio>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}