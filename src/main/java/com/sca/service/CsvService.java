package com.sca.service;

import java.util.List;
import java.util.Map;

public interface CsvService {
    /**
     * Toma una lista de mapas (cada mapa es una fila con clave=columna) 
     * y devuelve el CSV en bytes UTF-8.
     */
    byte[] generateCsv(List<Map<String, Object>> data);
}
