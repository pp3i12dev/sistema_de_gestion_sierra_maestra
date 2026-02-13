package com.sca.service.impl;

import com.sca.service.CsvService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CsvServiceImpl extends ResponseEntityExceptionHandler implements CsvService {

    @Override
    public byte[] generateCsv(List<Map<String, Object>> data) {
        // 1. Validación básica
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("La lista 'data' está vacía o nula");
        }

        // 2. Sacamos los encabezados del primer elemento
        Map<String, Object> primeraFila = data.get(0);
        List<String> headers = new ArrayList<>();
        for (String key : primeraFila.keySet()) {
            headers.add(key);
        }

        // 3. Armamos la línea de encabezado
        String csv = "";
        for (int i = 0; i < headers.size(); i++) {
            csv += headers.get(i);
            if (i < headers.size() - 1) {
                csv += ",";          // separador de columnas
            }
        }
        csv += "\n";                  // salto de línea al final de la cabecera

        // 4. Armamos cada fila de datos
        for (Map<String, Object> fila : data) { //bucle for en data que recorre la lista
            for (int i = 0; i < headers.size(); i++) {  //bucle for para obtener los datos de los atributos
                Object valor = fila.get(headers.get(i));
                // si es null, ponemos cadena vacía
                csv += (valor != null ? valor.toString() : "");
                if (i < headers.size() - 1) {
                    csv += ",";
                }
            }
            csv += "\n";              // salto de línea al final de cada fila
        }

        // 5. Convertimos la cadena completa a bytes UTF-8
        return csv.getBytes(StandardCharsets.UTF_8);
    }
}
