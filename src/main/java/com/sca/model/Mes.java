package com.sca.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.sca.constantes.ExpresionRegular;
import com.sca.validator.ValidarExpresionesRegulares;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name="mes")
public class Mes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	
	@NotBlank(message = "El nombre no puede estar en blanco")
	@Column(name="nombre")
	@NotNull
	@ValidarExpresionesRegulares(customMessage = "El nombre no es válido", expresionRegular = ExpresionRegular.NOMBREAPELLIDO)
    private String nombre;

    public Mes() {
    }

    public Mes(long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
}
