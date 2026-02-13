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
@Table(name="porcentajeMes")
public class PorcentajeMes {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // CORREGIDO: Solo @NotNull, sin @NotBlank
    @NotNull(message = "El porcentaje_aumento no puede ser nulo")
    @Column(name="porcentaje_aumento")
    private Double porcentaje_aumento;  // Cambiado a Double

    @NotNull
    @OneToOne
    @JoinColumn(name = "id_mes", nullable = false)
    private Mes mes;
    
    @NotNull
    @Column(name="anio")
    private int ano;

    public PorcentajeMes() {
    }

    public PorcentajeMes(long id,
            @NotNull Double porcentaje_aumento,  // Cambiado a Double
            @NotNull Mes mes, @NotNull int ano) {
        super();
        this.id = id;
        this.porcentaje_aumento = porcentaje_aumento;
        this.mes = mes;
        this.ano = ano;
    } 
}