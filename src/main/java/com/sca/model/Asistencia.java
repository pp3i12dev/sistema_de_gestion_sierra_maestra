package com.sca.model;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name="asistencia")
public class Asistencia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	
	@OneToOne
	@JoinColumn(name = "id_condicion", nullable = false)
    private Condicion id_condicion;
	
	@OneToOne
	@JoinColumn(name = "id_asociado", nullable = false)
	private Asociados id_asociado;
	
	@OneToOne
	@JoinColumn(name = "id_dia", nullable = false)
	private Dia id_dia;
	
	@Column(name="horaEntrada")
    private String horaEntrada;
	
	@Column(name="horaSalida")
    private String horaSalida;
	
	@Column(name="fecha")
    private LocalDate fecha;
	
	@Column(name="observacion")
    private String observacion;
	
	@Column(name="subTotal")
    private String subtotal;

    public Asistencia() {
    }

	public Asistencia(long id, Condicion id_condicion, Asociados id_asociado, Dia id_dia, String horaEntrada,
			String horaSalida, LocalDate fecha, String observacion, String subtotal) {
		super();
		this.id = id;
		this.id_condicion = id_condicion;
		this.id_asociado = id_asociado;
		this.id_dia = id_dia;
		this.horaEntrada = horaEntrada;
		this.horaSalida = horaSalida;
		this.fecha = fecha;
		this.observacion = observacion;
		this.subtotal = subtotal;
	}
}
