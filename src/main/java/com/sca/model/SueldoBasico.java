package com.sca.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name="sueldoBasico")
public class SueldoBasico {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "El sueldo_basico no puede ser nulo")
    @Column(name="sueldo_basico")
    private Double sueldo_basico;

    @NotNull(message = "La categoría no puede ser nula")
    @OneToOne
    @JoinColumn(name = "id_Categoria", nullable = false)
    private Categoria categoria;

    @NotNull(message = "El porcentaje mes no puede ser nulo")
    @OneToOne
    @JoinColumn(name = "id_Porcentaje_Mes", nullable = false)
    private PorcentajeMes porcentajeMes;

    public SueldoBasico() {
    }

    public SueldoBasico(long id, Double sueldo_basico) {
        this.id = id;
        this.sueldo_basico = sueldo_basico;
    }
}