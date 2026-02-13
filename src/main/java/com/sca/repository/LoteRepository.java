package com.sca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sca.model.Lote;

@Repository
public interface LoteRepository  extends JpaRepository <Lote, Long>{
    List<Lote> findByEstado(String estado);
}
