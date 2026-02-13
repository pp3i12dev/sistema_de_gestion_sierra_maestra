package com.sca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sca.model.Cerveza;

@Repository
public interface CervezaRepository  extends JpaRepository <Cerveza, Long>{
    List<Cerveza> findByEstado(String estado);
}
