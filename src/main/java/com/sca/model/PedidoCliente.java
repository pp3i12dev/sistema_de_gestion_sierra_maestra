package com.sca.model;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="pedido_cliente")
public class PedidoCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La fecha de pedido es obligatoria")
    @Column(name = "fecha_pedido", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPedido;

    @NotNull(message = "La fecha de entrega es obligatoria")
    @Column(name = "fecha_entrega", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntrega;
    
    @NotNull(message = "El campo envío es obligatorio")
    @Column(name="envio", nullable = false)
    private boolean envio;

    // 👇 dirección de entrega solo se valida en el servicio
    @Size(max = 100, message = "La dirección de entrega no puede tener más de 100 caracteres")
    @Column(name="direccionEntrega", length = 100)
    private String direccionEntrega;

    @NotEmpty(message = "El estado es obligatorio")
    @Size(max = 100, message = "El estado no puede tener más de 100 caracteres")
    @Column(name="estado", nullable = false, length = 100)
    private String estado;
    
    // Información del cliente externo
    @NotEmpty(message = "El nombre del cliente es obligatorio")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$", 
             message = "El nombre solo puede contener letras y espacios")
    @Column(name="nombre_cliente", nullable = false, length = 100)
    private String nombreCliente;
    
    @NotEmpty(message = "El email es obligatorio")
    @Size(max = 100, message = "El email no puede tener más de 100 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@(gmail\\.com|hotmail\\.com)$", 
             message = "El email debe terminar en @gmail.com o @hotmail.com")
    @Column(name="email_cliente", nullable = false, length = 100)
    private String emailCliente;
    
    @NotEmpty(message = "El teléfono es obligatorio")
    @Size(max = 100, message = "El teléfono no puede tener más de 100 caracteres")
    @Pattern(regexp = "^[0-9]+$", 
             message = "El teléfono solo puede contener números")
    @Column(name="telefono_cliente", nullable = false, length = 100)
    private String telefonoCliente;
    
    @Size(max = 100, message = "El lugar de entrega no puede tener más de 100 caracteres")
    @Column(name="lugar_entrega", length = 100)
    private String lugarEntrega;
    
    @ManyToMany(cascade = {})
    @JoinTable(
        name = "pedido_cliente_accesorio",
        joinColumns = @JoinColumn(name = "pedido_cliente_id"),
        inverseJoinColumns = @JoinColumn(name = "accesorio_id")
    )
    private Set<Accesorio> accesorios = new HashSet<>();

    @ManyToMany(cascade = {})
    @JoinTable(
        name = "pedido_cliente_cerveza",
        joinColumns = @JoinColumn(name = "pedido_cliente_id"),
        inverseJoinColumns = @JoinColumn(name = "cerveza_id")
    )
    private Set<Cerveza> cervezas = new HashSet<>();
    
    @OneToMany(cascade = {}, fetch = javax.persistence.FetchType.LAZY)
    @JoinColumn(name = "pedido_cliente_id")
    private Set<Barril> barriles = new HashSet<>();
    
    @NotNull(message = "El total general es obligatorio")
    @PositiveOrZero(message = "El total general no puede ser negativo")
    @Column(name="totalGral", nullable = false)
    private Double totalGral;

    @NotNull(message = "El estado de pago es obligatorio")
    @Size(max = 100, message = "El estado de pago no puede tener más de 100 caracteres")
    @Column(name = "estado_pago", nullable = false, length = 100)
    private String estadoPago = "Pendiente"; // valor por defecto
    
    @Size(max = 100, message = "Las notas no pueden tener más de 100 caracteres")
    @Column(name="nota", length = 100)
    private String nota;
}
