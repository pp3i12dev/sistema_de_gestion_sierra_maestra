package com.sca.service;

import com.sca.model.Respuesta;

public interface QRCodeService {
    
    /**
     * Genera un código QR único para un barril
     * @param barrilId ID del barril
     * @return Respuesta con el código QR generado y la imagen en Base64
     */
    Respuesta generarQRBarril(Long barrilId);
    
    /**
     * Genera un código QR único para un madurador
     * @param maduradorId ID del madurador
     * @return Respuesta con el código QR generado y la imagen en Base64
     */
    Respuesta generarQRMadurador(Long maduradorId);
    
    /**
     * Obtiene la imagen QR de un barril existente
     * @param barrilId ID del barril
     * @return Respuesta con la imagen QR en Base64
     */
    Respuesta obtenerQRBarril(Long barrilId);
    
    /**
     * Obtiene la imagen QR de un madurador existente
     * @param maduradorId ID del madurador
     * @return Respuesta con la imagen QR en Base64
     */
    Respuesta obtenerQRMadurador(Long maduradorId);
    
    /**
     * Genera imagen QR a partir de un texto
     * @param texto Texto a codificar
     * @param ancho Ancho de la imagen
     * @param alto Alto de la imagen
     * @return Array de bytes de la imagen PNG
     */
    byte[] generarImagenQR(String texto, int ancho, int alto) throws Exception;
}
