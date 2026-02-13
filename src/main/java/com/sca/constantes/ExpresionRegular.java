package com.sca.constantes;

public class ExpresionRegular {

	// Nombres y apellidos: letras (incluye acentos), espacios, guiones y apóstrofes
	public static final String NOMBREAPELLIDO = "^[A-Za-zÀ-ÖØ-öø-ÿ'\\-\\s]+$";
	// Documento: permitir entre 7 y 10 dígitos (varios formatos de documento regionales)
	public static final String DOCUMENTO = "^\\d{7,10}$";
	public static final String FECHA = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$"; // Valida una fecha en formato dd/mm/aaaa
	// CUIT: aceptar formato 11 dígitos o formato con guiones XX-XXXXXXXX-X
	public static final String CUIT = "^(\\d{2}-\\d{8}-\\d|\\d{11})$";
	// Teléfono: formatos variados, aceptar dígitos, espacios, paréntesis, + y guiones (6-20 caracteres)
	public static final String TELEFONO = "^[0-9\\s()+\\-]{6,20}$";
	public static final String LEGAJO = "^\\d{3}$"; // Valida que el legajo tenga 3 digitos	
	public static final String DIA_SEMANA_CAP = "^(Lunes|Martes|Miércoles|Miercoles|Jueves|Viernes|Sábado|Sabado|Domingo)$";
	public static final String LOTE_ESTADO = "^(Produccion|Madurando|Terminado|Mantenimiento)$";
	public static final String CERVEZA_ESTADO = "^(Disponible|Agotada|Produccion|Mantenimiento)$";
	public static final String ACCESORIO_ESTADO = "^(Disponible|Alquilado|Devuelto|Mantenimiento|Inactivo|Sucio)$";
	public static final String BARRIL_ESTADO = "^(Disponible|Cargado|Alquilado|Despacho|Devuelto|Sucio)$";
}
