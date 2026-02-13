package com.sca.model;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Respuesta {
	
	private String status;

	private String codigo;

	private String descripcion;

	private Object data;

	public Respuesta() {
		super();
	}

	public Respuesta(String status, String codigo, String descripcion) {
		super();
		this.status = status;
		this.codigo = codigo;
		this.descripcion = descripcion;
	}

	public Respuesta(String status, String codigo, String descripcion, Object data) {
		super();
		this.status = status;
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.data = data;
	}
}
