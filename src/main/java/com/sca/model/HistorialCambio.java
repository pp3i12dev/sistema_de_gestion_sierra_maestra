package com.sca.model;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HistorialCambio {
    private Date fecha;
    private String usuario;
    private String accion;
    private String detalles;
    private String cambios;
    
    // Constructor simplificado
    public HistorialCambio(String usuario, String accion, String detalles) {
        this.fecha = new Date();
        this.usuario = usuario;
        this.accion = accion;
        this.detalles = detalles;
        this.cambios = "";
    }
    
    // Constructor con cambios específicos
    public HistorialCambio(String usuario, String accion, String detalles, String cambios) {
        this.fecha = new Date();
        this.usuario = usuario;
        this.accion = accion;
        this.detalles = detalles;
        this.cambios = cambios;
    }
}