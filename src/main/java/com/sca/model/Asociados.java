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

// Importaciones adicionales para manejar JSON
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    
    // === NUEVO CAMPO PARA PERMISOS ===
    @Column(name = "permisos", columnDefinition = "JSON")
    private String permisos;
    // =================================
    
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
    
    // === MÉTODOS HELPER PARA MANEJAR PERMISOS ===
    
    /**
     * Convierte el JSON de permisos a una Lista de Strings
     */
    public List<String> getPermisosList() {
        if (this.permisos == null || this.permisos.trim().isEmpty()) {
            return new ArrayList<>();
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(this.permisos, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    /**
     * Convierte una Lista de Strings a JSON y la guarda en permisos
     */
    public void setPermisosList(List<String> permisosList) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.permisos = mapper.writeValueAsString(permisosList);
        } catch (Exception e) {
            this.permisos = "[]";
        }
    }
    
    /**
     * Verifica si el asociado tiene un permiso específico
     */
    public boolean tienePermiso(String permiso) {
        return getPermisosList().contains(permiso);
    }
    
    /**
     * Agrega un permiso a la lista (sin duplicados)
     */
    public void agregarPermiso(String permiso) {
        List<String> permisosActuales = getPermisosList();
        if (!permisosActuales.contains(permiso)) {
            permisosActuales.add(permiso);
            setPermisosList(permisosActuales);
        }
    }
    
    /**
     * Remueve un permiso de la lista
     */
    public void removerPermiso(String permiso) {
        List<String> permisosActuales = getPermisosList();
        permisosActuales.remove(permiso);
        setPermisosList(permisosActuales);
    }
}