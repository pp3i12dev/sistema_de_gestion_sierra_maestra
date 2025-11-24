package com.sca.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sca.constantes.ExpresionRegular;
import com.sca.validator.ValidarExpresionesRegulares;
import javax.validation.constraints.Email;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"categorias","condiciones"})
// ESTA ES LA CLASE USUARIO, NO SE LE CAMBIO EL NOMBRE PARA NO ROMPER EL SISTEMA
@Entity
@Table(name="asociado")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
    )
public class Asociados {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;
    
    @Column(name="id_firma")
    private Integer id_firma;

    @ValidarExpresionesRegulares(customMessage="El legajo debe tener 3 digitos", expresionRegular = ExpresionRegular.LEGAJO)
    @NotBlank(message = "El legajo no puede estar en blanco")
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
    
    @ValidarExpresionesRegulares(customMessage = "El documento no es válido", expresionRegular = ExpresionRegular.DOCUMENTO)
    @Column(name="documento")
    private String documento;

    @Email(message = "El correo tiene formato inválido")
    @NotBlank(message = "El correo es obligatorio")
    @Column(name = "email")
    private String email;
    
    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "^(administrador|asociado|cliente)$", 
             message = "El rol debe ser: administrador, asociado o cliente")
    @Column(name="rol")
    private String rol;

    @Column(name="contrasenia")
    private String contrasenia;

    @Column(name="activo")
    @NotNull
    private int activo;

    @ManyToMany
    @JoinTable(
        name = "asociado_categoria", // Nombre de la tabla intermedia
        joinColumns = @JoinColumn(name = "asociado_id"), // Llave foránea de la tabla 'asociado'
        inverseJoinColumns = @JoinColumn(name = "categoria_id") // Llave foránea de la tabla 'categoria'
    )
    private Set<Categoria> categorias;
    
    @Column(name="telefono")
    @NotNull
    private String telefono;
    
    @OneToMany(mappedBy = "asociado", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<AsociadosCondicion> condiciones = new ArrayList<>();
    
    public Asociados() {
    }

    public Asociados(long id, 
                    @NotBlank(message = "El nombre no puede estar en blanco") @NotNull String nombre,
                    @NotBlank(message = "El apellido no puede estar en blanco") String apellido,
                    @NotBlank(message = "El legajo no puede estar en blanco") String legajo, 
                    Integer id_firma, 
                    String documento,
                    @Email @NotBlank String email,
                    @NotBlank String rol,
                    String contrasenia,
                    Set<Categoria> categorias,
                    @NotNull int activo, 
                    @NotNull String telefono) {
        super();
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.legajo = legajo;
        this.id_firma = id_firma;
        this.documento = documento;
        this.email = email;
        this.rol = rol;
        this.contrasenia = contrasenia;
        this.categorias = categorias;
        this.activo = activo;
        this.telefono = telefono;
    }
}