package com.sca.model;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
// import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "asociados")
@Entity
@Table(name="categoria")
public class Categoria {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;

    @NotBlank(message = "El nombre no puede estar en blanco")
	@Column(name="nombre")
	@NotNull
    private String nombre;
    
    @ManyToMany(mappedBy = "categorias")
    @JsonIgnore
    private Set<Asociados> asociados;

    public Categoria() {
    }

    public Categoria(long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
}
