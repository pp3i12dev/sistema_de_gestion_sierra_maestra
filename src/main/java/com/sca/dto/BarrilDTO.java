package com.sca.dto;

import com.sca.model.Barril;

public class BarrilDTO {
    private Long id;
    private String estado;
    private Integer litros;
    private String notas;
    private String cervezaNombre;

    public BarrilDTO(Barril barril) {
        this.id = barril.getId();
        this.estado = barril.getEstado();
        this.litros = barril.getLitros();
        this.notas = barril.getNotas();
        this.cervezaNombre = barril.getLote() != null && barril.getLote().getCerveza() != null
                ? barril.getLote().getCerveza().getNombreCerveza()
                : "-";
    }

    // getters
    public Long getId() { return id; }
    public String getEstado() { return estado; }
    public Integer getLitros() { return litros; }
    public String getNotas() { return notas; }
    public String getCervezaNombre() { return cervezaNombre; }
}
