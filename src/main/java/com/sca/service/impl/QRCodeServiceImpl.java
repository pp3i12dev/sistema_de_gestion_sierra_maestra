package com.sca.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sca.model.Barril;
import com.sca.model.Madurador;
import com.sca.model.Respuesta;
import com.sca.repository.BarrilRepository;
import com.sca.repository.MaduradorRepository;
import com.sca.service.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class QRCodeServiceImpl implements QRCodeService {

    @Autowired
    private BarrilRepository barrilRepository;

    @Autowired
    private MaduradorRepository maduradorRepository;

    private static final int QR_WIDTH = 300;
    private static final int QR_HEIGHT = 300;

    @Override
    @Transactional
    public Respuesta generarQRBarril(Long barrilId) {
        try {
            Optional<Barril> barrilOpt = barrilRepository.findById(barrilId);
            
            if (!barrilOpt.isPresent()) {
                return new Respuesta("404", "404", "Barril no encontrado", null);
            }

            Barril barril = barrilOpt.get();

            // Validar que no tenga ya un QR activo
            if (barril.getCodigoQR() != null && !barril.getCodigoQR().isEmpty()) {
                return new Respuesta("400", "400", "El barril ya tiene un código QR asignado: " + barril.getCodigoQR(), null);
            }

            // Generar código QR único
            String codigoQR = generarCodigoUnico("BARRIL", barrilId);
            
            // Generar imagen QR
            byte[] imagenQR = generarImagenQR(codigoQR, QR_WIDTH, QR_HEIGHT);
            String imagenBase64 = Base64.getEncoder().encodeToString(imagenQR);

            // Guardar código en el barril
            barril.setCodigoQR(codigoQR);
            barrilRepository.save(barril);

            Map<String, Object> datos = new HashMap<>();
            datos.put("codigoQR", codigoQR);
            datos.put("imagenBase64", imagenBase64);
            datos.put("tipo", "BARRIL");
            datos.put("id", barrilId);

            return new Respuesta("200", "200", "Código QR generado exitosamente", datos);

        } catch (Exception e) {
            return new Respuesta("500", "500", "Error al generar código QR: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional
    public Respuesta generarQRMadurador(Long maduradorId) {
        try {
            Optional<Madurador> maduradorOpt = maduradorRepository.findById(maduradorId);
            
            if (!maduradorOpt.isPresent()) {
                return new Respuesta("404", "404", "Madurador no encontrado", null);
            }

            Madurador madurador = maduradorOpt.get();

            // Validar que no tenga ya un QR activo
            if (madurador.getCodigoQR() != null && !madurador.getCodigoQR().isEmpty()) {
                return new Respuesta("400", "400", "El madurador ya tiene un código QR asignado: " + madurador.getCodigoQR(), null);
            }

            // Generar código QR único
            String codigoQR = generarCodigoUnico("MADURADOR", maduradorId);
            
            // Generar imagen QR
            byte[] imagenQR = generarImagenQR(codigoQR, QR_WIDTH, QR_HEIGHT);
            String imagenBase64 = Base64.getEncoder().encodeToString(imagenQR);

            // Guardar código en el madurador
            madurador.setCodigoQR(codigoQR);
            maduradorRepository.save(madurador);

            Map<String, Object> datos = new HashMap<>();
            datos.put("codigoQR", codigoQR);
            datos.put("imagenBase64", imagenBase64);
            datos.put("tipo", "MADURADOR");
            datos.put("id", maduradorId);

            return new Respuesta("200", "200", "Código QR generado exitosamente", datos);

        } catch (Exception e) {
            return new Respuesta("500", "500", "Error al generar código QR: " + e.getMessage(), null);
        }
    }

    @Override
    public Respuesta obtenerQRBarril(Long barrilId) {
        try {
            Optional<Barril> barrilOpt = barrilRepository.findById(barrilId);
            
            if (!barrilOpt.isPresent()) {
                return new Respuesta("404", "404", "Barril no encontrado", null);
            }

            Barril barril = barrilOpt.get();

            if (barril.getCodigoQR() == null || barril.getCodigoQR().isEmpty()) {
                return new Respuesta("404", "404", "El barril no tiene código QR asignado", null);
            }

            // Generar imagen del QR existente
            byte[] imagenQR = generarImagenQR(barril.getCodigoQR(), QR_WIDTH, QR_HEIGHT);
            String imagenBase64 = Base64.getEncoder().encodeToString(imagenQR);

            Map<String, Object> datos = new HashMap<>();
            datos.put("codigoQR", barril.getCodigoQR());
            datos.put("imagenBase64", imagenBase64);
            datos.put("tipo", "BARRIL");
            datos.put("id", barrilId);

            return new Respuesta("200", "200", "Código QR obtenido exitosamente", datos);

        } catch (Exception e) {
            return new Respuesta("500", "500", "Error al obtener código QR: " + e.getMessage(), null);
        }
    }

    @Override
    public Respuesta obtenerQRMadurador(Long maduradorId) {
        try {
            Optional<Madurador> maduradorOpt = maduradorRepository.findById(maduradorId);
            
            if (!maduradorOpt.isPresent()) {
                return new Respuesta("404", "404", "Madurador no encontrado", null);
            }

            Madurador madurador = maduradorOpt.get();

            if (madurador.getCodigoQR() == null || madurador.getCodigoQR().isEmpty()) {
                return new Respuesta("404", "404", "El madurador no tiene código QR asignado", null);
            }

            // Generar imagen del QR existente
            byte[] imagenQR = generarImagenQR(madurador.getCodigoQR(), QR_WIDTH, QR_HEIGHT);
            String imagenBase64 = Base64.getEncoder().encodeToString(imagenQR);

            Map<String, Object> datos = new HashMap<>();
            datos.put("codigoQR", madurador.getCodigoQR());
            datos.put("imagenBase64", imagenBase64);
            datos.put("tipo", "MADURADOR");
            datos.put("id", maduradorId);

            return new Respuesta("200", "200", "Código QR obtenido exitosamente", datos);

        } catch (Exception e) {
            return new Respuesta("500", "500", "Error al obtener código QR: " + e.getMessage(), null);
        }
    }

    @Override
    public byte[] generarImagenQR(String texto, int ancho, int alto) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);

        BitMatrix bitMatrix = qrCodeWriter.encode(texto, BarcodeFormat.QR_CODE, ancho, alto, hints);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        
        return outputStream.toByteArray();
    }

    /**
     * Genera un código único para el QR
     * Formato: TIPO-ID-TIMESTAMP-UUID
     */
    private String generarCodigoUnico(String tipo, Long id) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return String.format("SM-%s-%d-%s-%s", tipo, id, timestamp, uuid);
    }
}
