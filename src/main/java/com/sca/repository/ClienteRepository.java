package com.sca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sca.model.Cliente;

import java.util.Optional; // ✅ Corrección: importar Optional

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // 🔹 Método para login (documento + contraseña)
    Cliente findByDocumentoAndContrasenia(String documento, String contrasenia);
    
    // 🔹 Métodos para validación de unicidad en registro de clientes
    // Verifica si ya existe un cliente con el mismo documento (DNI)
    boolean existsByDocumento(String documento);
    
    // Verifica si ya existe un cliente con el mismo email
    boolean existsByMail(String mail);
    
    // Busca cliente por documento (para validaciones adicionales)
    Cliente findByDocumento(String documento);
    
    // ✅ Corrección: busca cliente por email y permite verificar si existe
    Optional<Cliente> findByMail(String mail);
    
    // ✅ Nuevo método: busca cliente por token de recuperación
    Optional<Cliente> findByTokenRecuperacion(String tokenRecuperacion);
}
