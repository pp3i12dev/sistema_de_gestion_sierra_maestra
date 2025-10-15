package com.sca.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

import com.sca.constantes.ExpresionRegular;
import com.sca.validator.ValidarExpresionesRegulares;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name = "accesorio")
public class Accesorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @ValidarExpresionesRegulares(customMessage = "El estado no es válido", expresionRegular = ExpresionRegular.ACCESORIO_ESTADO)
    @Column(name = "estado")
    private String estado;
    
    @Column(name = "notas")
    private String notas;
    
    // ✅ NUEVO: Campo para la sesión de reserva
    @Column(name = "session_reserva")
    private String sessionReserva;

    // ✅ NUEVO: Campo para el timestamp de reserva  
    @Column(name = "timestamp_reserva")
    private LocalDateTime timestampReserva;
    
    // Constructores (actualizados)
    public Accesorio(Long id, String nombre, String estado, String notas, String sessionReserva, LocalDateTime timestampReserva) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
        this.notas = notas;
        this.sessionReserva = sessionReserva;
        this.timestampReserva = timestampReserva;
    }

    public Accesorio() {
    }

    public Accesorio(String nombre, String estado, String notas) {
        this.nombre = nombre;
        this.estado = estado;
        this.notas = notas;
    }
}