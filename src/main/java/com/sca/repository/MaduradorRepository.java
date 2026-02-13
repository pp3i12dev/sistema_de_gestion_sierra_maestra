package com.sca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sca.model.Madurador;

@Repository
public interface MaduradorRepository  extends JpaRepository <Madurador, Long>{
    // Este método nos permitirá obtener todos los Maduradores de un estado específico
    List<Madurador> findByEstado(String estado);
}
