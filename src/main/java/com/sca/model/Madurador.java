package com.sca.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
// import javax.persistence.Lob;
import javax.persistence.Table;
// import javax.persistence.ManyToMany;
// import javax.persistence.OneToMany;
// import javax.persistence.OneToOne;
import javax.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name = "madurador")
public class Madurador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "litros", nullable = false)
    private Integer litros;
    
    @Column(name = "estado")
    private String estado;
    
    @Column(name = "notas")
    private String notas;

    @Column(name = "codigo_qr", unique = true)
    private String codigoQR;

    // Relación ManyToOne con Lote
    @ManyToOne
    @JoinColumn(name = "lote_id")  // Crea la columna lote_id en la tabla Madurador para asociar maduradores con un lote
    private Lote lote;

    public Madurador() {
    }

    public Madurador(Integer litros, String estado, String notas) {
        this.litros = litros;
        this.estado = estado;
        this.notas = notas;
    }

    public Madurador(Long id, Integer litros, String estado, String notas, Lote lote) {
        this.id = id;
        this.litros = litros;
        this.estado = estado;
        this.notas = notas;
        this.lote = lote;
    }
}
