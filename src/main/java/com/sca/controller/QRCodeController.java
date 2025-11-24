package com.sca.controller;

import com.sca.model.Respuesta;
import com.sca.service.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/qr")
public class QRCodeController {

    @Autowired
    private QRCodeService qrCodeService;

    /**
     * Vista principal de gestión de códigos QR
     */
    @GetMapping
    public String vistaGestionQR(Model model) {
        return "qr/gestion-qr";
    }

    /**
     * Generar código QR para un barril
     */
    @PostMapping("/barril/{id}/generar")
    @ResponseBody
    public ResponseEntity<Respuesta> generarQRBarril(@PathVariable Long id) {
        Respuesta respuesta = qrCodeService.generarQRBarril(id);
        
        if ("200".equals(respuesta.getCodigo())) {
            return ResponseEntity.ok(respuesta);
        } else if ("404".equals(respuesta.getCodigo())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        } else if ("400".equals(respuesta.getCodigo())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    /**
     * Generar código QR para un madurador
     */
    @PostMapping("/madurador/{id}/generar")
    @ResponseBody
    public ResponseEntity<Respuesta> generarQRMadurador(@PathVariable Long id) {
        Respuesta respuesta = qrCodeService.generarQRMadurador(id);
        
        if ("200".equals(respuesta.getCodigo())) {
            return ResponseEntity.ok(respuesta);
        } else if ("404".equals(respuesta.getCodigo())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        } else if ("400".equals(respuesta.getCodigo())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    /**
     * Obtener código QR de un barril existente
     */
    @GetMapping("/barril/{id}")
    @ResponseBody
    public ResponseEntity<Respuesta> obtenerQRBarril(@PathVariable Long id) {
        Respuesta respuesta = qrCodeService.obtenerQRBarril(id);
        
        if ("200".equals(respuesta.getCodigo())) {
            return ResponseEntity.ok(respuesta);
        } else if ("404".equals(respuesta.getCodigo())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    /**
     * Obtener código QR de un madurador existente
     */
    @GetMapping("/madurador/{id}")
    @ResponseBody
    public ResponseEntity<Respuesta> obtenerQRMadurador(@PathVariable Long id) {
        Respuesta respuesta = qrCodeService.obtenerQRMadurador(id);
        
        if ("200".equals(respuesta.getCodigo())) {
            return ResponseEntity.ok(respuesta);
        } else if ("404".equals(respuesta.getCodigo())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    /**
     * Descargar imagen QR como PNG
     */
    @GetMapping("/barril/{id}/imagen")
    public ResponseEntity<byte[]> descargarImagenQRBarril(@PathVariable Long id) {
        try {
            Respuesta respuesta = qrCodeService.obtenerQRBarril(id);
            
            if (!"200".equals(respuesta.getCodigo())) {
                return ResponseEntity.notFound().build();
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> datos = (Map<String, Object>) respuesta.getDato();
            String imagenBase64 = (String) datos.get("imagenBase64");
            String codigoQR = (String) datos.get("codigoQR");
            
            byte[] imagenBytes = java.util.Base64.getDecoder().decode(imagenBase64);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentDispositionFormData("attachment", "QR-Barril-" + id + ".png");
            
            return new ResponseEntity<>(imagenBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Descargar imagen QR como PNG
     */
    @GetMapping("/madurador/{id}/imagen")
    public ResponseEntity<byte[]> descargarImagenQRMadurador(@PathVariable Long id) {
        try {
            Respuesta respuesta = qrCodeService.obtenerQRMadurador(id);
            
            if (!"200".equals(respuesta.getCodigo())) {
                return ResponseEntity.notFound().build();
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> datos = (Map<String, Object>) respuesta.getDato();
            String imagenBase64 = (String) datos.get("imagenBase64");
            
            byte[] imagenBytes = java.util.Base64.getDecoder().decode(imagenBase64);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentDispositionFormData("attachment", "QR-Madurador-" + id + ".png");
            
            return new ResponseEntity<>(imagenBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
