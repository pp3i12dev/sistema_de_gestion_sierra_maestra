package com.sca.dto;

public class CervezaReportDTO {
    private String nombreCerveza;
    private long totalLitros;

    public CervezaReportDTO() {}

    public CervezaReportDTO(String nombreCerveza, long totalLitros) {
        this.nombreCerveza = nombreCerveza;
        this.totalLitros = totalLitros;
    }

    public String getNombreCerveza() { return nombreCerveza; }
    public void setNombreCerveza(String nombreCerveza) { this.nombreCerveza = nombreCerveza; }

    public long getTotalLitros() { return totalLitros; }
    public void setTotalLitros(long totalLitros) { this.totalLitros = totalLitros; }
}