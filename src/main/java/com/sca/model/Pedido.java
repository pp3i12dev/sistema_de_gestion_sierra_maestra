// com.sca.model.Pedido.java
package com.sca.model;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La fecha de pedido es obligatoria")
    @Column(name = "fecha_pedido", nullable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date fechaPedido;

    @NotNull(message = "La fecha de entrega es obligatoria")
    @Column(name = "fecha_entrega", nullable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date fechaEntrega;

    @NotNull(message = "El campo envío es obligatorio")
    @Column(name = "envio", nullable = false)
    private boolean envio;

    @Column(name = "direccionEntrega")
    private String direccionEntrega;

    @NotEmpty(message = "El estado es obligatorio")
    @Column(name = "estado", nullable = false)
    private String estado;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "pedido_accesorio",
        joinColumns = @JoinColumn(name = "pedido_id"),
        inverseJoinColumns = @JoinColumn(name = "accesorio_id")
    )
    private Set<Accesorio> accesorios = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "pedido_cerveza",
        joinColumns = @JoinColumn(name = "pedido_id"),
        inverseJoinColumns = @JoinColumn(name = "cerveza_id")
    )
    private Set<Cerveza> cervezas = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "pedido_barril",
        joinColumns = @JoinColumn(name = "pedido_id"),
        inverseJoinColumns = @JoinColumn(name = "barril_id")
    )
    private Set<Barril> barriles = new HashSet<>();

    @NotNull(message = "El usuario es obligatorio")
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Asociados usuario;

    @NotNull(message = "El cliente es obligatorio")
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @NotNull(message = "El total general es obligatorio")
    @PositiveOrZero(message = "El total general no puede ser negativo")
    @Column(name = "totalGral", nullable = false)
    private Double totalGral;

    @NotNull(message = "El estado de pago es obligatorio")
    @Column(name = "estado_pago", nullable = false)
    private String estadoPago = "Pendiente";

    @Column(name = "nota", columnDefinition = "TEXT")
    private String nota;

    // ✅ ÚNICO CAMBIO NECESARIO:
    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata; // ← guarda precios personalizados de accesorios

}