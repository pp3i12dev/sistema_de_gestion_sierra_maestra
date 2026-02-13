package com.sca.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name="asistenciaTotal")
public class AsistenciaTotal {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
	@OneToOne
	@JoinColumn(name = "id_mes", nullable = false)
	private Mes id_mes;
    
	@Column(name="anio")
	private int ano;
	
	@ManyToOne
	@JoinColumn(name = "id_asistencia", nullable = false)
	private Asociados id_asistencia;
	
	@Column(name="tiempoHoraAnual")
    private String tiempo_hora_anual;
	
	@Column(name="tiempoHoraMensual")
    private String tiempo_hora_mensual;
	
	@Column(name="TiempoHoraPrimeraQuincena")
    private String tiempo_hora_primera_quincena;
	
	@Column(name="TiempoSegundaQuincena")
    private String tiempo_hora_segunda_quincena;

	@Column(name="descripcion")
	private String descripcion;
	
    public AsistenciaTotal() {
    }

	public AsistenciaTotal(long id, Mes id_mes, int ano, Asociados id_asistencia, String tiempo_hora_anual,
			String tiempo_hora_mensual, String tiempo_hora_primera_quincena, String tiempo_hora_segunda_quincena) {
		super();
		this.id = id;
		this.id_mes = id_mes;
		this.ano = ano;
		this.id_asistencia = id_asistencia;
		this.tiempo_hora_anual = tiempo_hora_anual;
		this.tiempo_hora_mensual = tiempo_hora_mensual;
		this.tiempo_hora_primera_quincena = tiempo_hora_primera_quincena;
		this.tiempo_hora_segunda_quincena = tiempo_hora_segunda_quincena;
	}
}
