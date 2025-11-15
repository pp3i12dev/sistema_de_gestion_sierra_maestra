package com.sca.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.sca.constantes.ExpresionRegular;
import com.sca.validator.ValidarExpresionesRegulares;

import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "legajo")
    private String legajo;

    @NotBlank(message = "El nombre no puede estar en blanco")
    @NotNull
    @ValidarExpresionesRegulares(customMessage = "El nombre no es válido", expresionRegular = ExpresionRegular.NOMBREAPELLIDO)
    @Column(name = "nombre")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar en blanco")
    @ValidarExpresionesRegulares(customMessage = "El apellido no es válido", expresionRegular = ExpresionRegular.NOMBREAPELLIDO)
    @Column(name = "apellido")
    private String apellido;

    @NotBlank(message = "El documento no puede estar en blanco")
    @Pattern(regexp = "^\\d+$", message = "El documento debe contener solo números")
    @Column(name = "documento")
    private String documento;

    @NotBlank(message = "El mail no puede estar en blanco")
    @Email(message = "Error en el formato del mail")
    @Column(name = "mail")
    private String mail;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "contrasenia")
    private String contrasenia;

    @Column(name = "direccion")
    private String direccion;

    @ValidarExpresionesRegulares(customMessage = "El teléfono no es válido", expresionRegular = ExpresionRegular.TELEFONO)
    @Column(name = "telefono")
    private String telefono;

    @ValidarExpresionesRegulares(customMessage = "La fecha no es válida", expresionRegular = ExpresionRegular.FECHA)
    @Column(name = "fecha_nacimiento")
    @ApiModelProperty(value = "Fecha de nacimiento en formato dd/mm/aaaa")
    private String fecha_nacimiento;

    @ValidarExpresionesRegulares(customMessage = "El cuit no es válido", expresionRegular = ExpresionRegular.CUIT)
    @Column(name = "cuit")
    private String cuit;

    // 🔐 Token para recuperación de contraseña
    @Column(name = "token_recuperacion")
    private String tokenRecuperacion;

    public Cliente() {
        super();
    }

    public Cliente(long id, String nombre, String apellido, String documento, String fecha_nacimiento, String direccion,
                   String mail, String cuit, String telefono) {
        super();
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
        this.fecha_nacimiento = fecha_nacimiento;
        this.direccion = direccion;
        this.mail = mail;
        this.cuit = cuit;
        this.telefono = telefono;
    }
}
