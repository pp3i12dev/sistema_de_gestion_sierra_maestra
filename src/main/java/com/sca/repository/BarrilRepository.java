package com.sca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sca.model.Barril;

@Repository
public interface BarrilRepository extends JpaRepository<Barril, Long> {

    // 🔹 Nuevo: buscar barriles por id de la cerveza y estado
    List<Barril> findByLote_Cerveza_IdAndEstado(Long cervezaId, String estado);

    List<Barril> findByEstado(String estado);

}
