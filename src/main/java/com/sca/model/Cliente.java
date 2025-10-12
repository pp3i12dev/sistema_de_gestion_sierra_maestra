package com.sca.model;
// import java.util.Objects;
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
@Table(name="cliente")
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name="legajo")
	private String legajo;

	@NotBlank(message = "El nombre no puede estar en blanco")
	@Column(name="nombre")
	@NotNull
	@ValidarExpresionesRegulares(customMessage = "El nombre no es válido", expresionRegular = ExpresionRegular.NOMBREAPELLIDO)
	private String nombre;
	
	@ValidarExpresionesRegulares(customMessage = "El apellido no es válido", expresionRegular = ExpresionRegular.NOMBREAPELLIDO)
	@NotBlank(message = "El apellido no puede estar en blanco")
	@Column(name="apellido")
	private String apellido;
	
	@Pattern(regexp = "^\\d+$", message = "El documento debe contener solo números")
	@NotBlank(message = "El documento no puede estar en blanco")
	@Column(name="documento")
	private String documento;

	@NotBlank(message = "El mail no puede estar en blanco")
	@Column(name="mail")
	@Email(message = "Error en el formato del mail")
	private String mail;

	@Column(name="tipo")
	private String tipo;

	@Column(name="activo")
	private Boolean activo;

	@Column(name="contrasenia")
	private String contrasenia;

	@Column(name="direccion")
	private String direccion;

	@ValidarExpresionesRegulares(customMessage = "El teléfono no es válido", expresionRegular = ExpresionRegular.TELEFONO)
	@Column(name="telefono")
	private String telefono;

	@ValidarExpresionesRegulares(customMessage = "El documento no es válido", expresionRegular = ExpresionRegular.FECHA)
	@Column(name="fecha_nacimiento")
	@ApiModelProperty(value = "Fecha de nacimiento en formato dd/mm/aaaa")
	private String fecha_nacimiento;
	
	@ValidarExpresionesRegulares(customMessage = "El cuit no es válido", expresionRegular = ExpresionRegular.CUIT)
	@Column(name="cuit")
	private String cuit;
	
	public Cliente() {
		super();
	}

	public Cliente(long id, String nombre, String apellido, String documento, String fecha_nacimiento, String direccion,
			@Email String mail, String cuit, String telefono) {
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
