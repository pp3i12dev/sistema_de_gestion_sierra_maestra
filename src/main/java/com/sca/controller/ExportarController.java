package com.sca.controller;

import com.sca.service.CsvService;
import com.sca.service.PdfService;

import io.swagger.annotations.Api;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "Exportar")
public class ExportarController {

    private final CsvService csvService;
    private final PdfService pdfService;

    public ExportarController(CsvService csvService, PdfService pdfService) {
        this.csvService = csvService;
        this.pdfService = pdfService;
    }

    /**
     * Recibe un wrapper JSON con clave "data" que debe ser un array de objetos.
     * Ejemplo:
     * {
     *   "status": 200,
     *   "data": [
     *     { "id": 1, "nombre": "Lunes" },
     *     { "id": 2, "nombre": "Martes" }
     *   ]
     * }
     */
    private List<Map<String, Object>> extractData(Map<String,Object> wrapper) {
        Object obj = wrapper.get("data");
        @SuppressWarnings("unchecked")
        List<Map<String,Object>> data = (List<Map<String,Object>>) obj;
        return data;
    }

    @PostMapping(
        value = "/csv",
        produces = "text/csv; charset=UTF-8"
    )
    public ResponseEntity<ByteArrayResource> exportCsv(@RequestBody Map<String, Object> wrapper) {
        List<Map<String, Object>> data;
        try {
            data = extractData(wrapper);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        byte[] csvBytes = csvService.generateCsv(data);
        ByteArrayResource resource = new ByteArrayResource(csvBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=datos.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(csvBytes.length)
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(resource);
    }

    @PostMapping(
        value = "/pdf",
        produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<ByteArrayResource> exportPdf(@RequestBody Map<String, Object> wrapper) {
        List<Map<String, Object>> data;
        try {
            data = extractData(wrapper);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        byte[] pdfBytes;
        try {
            pdfBytes = pdfService.generatePdf(data);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        }

        ByteArrayResource resource = new ByteArrayResource(pdfBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=datos.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfBytes.length)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
}
