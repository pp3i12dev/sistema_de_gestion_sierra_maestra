package com.sca.controller;

import com.sca.service.InventarioService;

import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "Inventario")
public class InventarioController {

    @Autowired
    InventarioService inventarioService;

    @PostMapping(
        value = "/csv",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ByteArrayResource> exportCsv() {
        byte[] data = inventarioService.invetarioCsv();
        ByteArrayResource resource = new ByteArrayResource(data);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=datos.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(data.length)
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(resource);
    }

    
}
