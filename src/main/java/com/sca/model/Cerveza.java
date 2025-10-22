package com.sca.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

import com.sca.constantes.ExpresionRegular;
import com.sca.validator.ValidarExpresionesRegulares;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // ✅ Activamos comparación explícita
@ToString
@Entity
@Table(name = "cervezas")
public class Cerveza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // ✅ Esto permite que el Set<Cerveza> compare por ID
    private Long id;

    @Column(name = "nombre", nullable = false, length = 50)
    @NotEmpty(message = "El nombre no puede estar vacio")
    private String nombreCerveza;

    @Column(name = "tipo", nullable = false, length = 30)
    private String tipoCerveza;

    @Column(name = "grado_alcoholico", nullable = false)
    @Positive(message = "El grado alcohólico debe ser un número positivo")
    private Double gradoAlcoholico;

    @Column(name = "amargor_IBU", nullable = false)
    @Positive(message = "El amargor (IBU) debe ser un número positivo")
    private Double amargorIbu;

    @Column(name = "descripcion", nullable = true)
    private String descripcion;

    @Column(name = "precioLitro", nullable = true)
    @Positive(message = "El precio por litro debe ser un número positivo")
    private Double precioPorLitro;

    @NotEmpty(message = "El estado es obligatorio")
    @Column(name = "estado", nullable = false)
    @ValidarExpresionesRegulares(
        customMessage = "El estado no es válido",
        expresionRegular = ExpresionRegular.CERVEZA_ESTADO
    )
    private String estado;

    public Cerveza() {}

    public Cerveza(Long id, String nombreCerveza, String tipoCerveza, Double gradoAlcoholico, Double precioPorLitro) {
        this.id = id;
        this.nombreCerveza = nombreCerveza;
        this.tipoCerveza = tipoCerveza;
        this.gradoAlcoholico = gradoAlcoholico;
        this.precioPorLitro = precioPorLitro;
    }

    public Cerveza(String nombreCerveza, String tipoCerveza, Double gradoAlcoholico, Double precioPorLitro) {
        this.nombreCerveza = nombreCerveza;
        this.tipoCerveza = tipoCerveza;
        this.gradoAlcoholico = gradoAlcoholico;
        this.precioPorLitro = precioPorLitro;
    }

    public Cerveza(Long id, String nombreCerveza, String tipoCerveza, Double gradoAlcoholico, String descripcion,
                   Double precioPorLitro) {
        this.id = id;
        this.nombreCerveza = nombreCerveza;
        this.tipoCerveza = tipoCerveza;
        this.gradoAlcoholico = gradoAlcoholico;
        this.descripcion = descripcion;
        this.precioPorLitro = precioPorLitro;
    }

    public Cerveza(Long id, String nombreCerveza, String tipoCerveza, Double gradoAlcoholico, Double amargorIbu,
                   String descripcion, Double precioPorLitro, String estado) {
        this.id = id;
        this.nombreCerveza = nombreCerveza;
        this.tipoCerveza = tipoCerveza;
        this.gradoAlcoholico = gradoAlcoholico;
        this.amargorIbu = amargorIbu;
        this.descripcion = descripcion;
        this.precioPorLitro = precioPorLitro;
        this.estado = estado;
    }
}
