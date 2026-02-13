package com.sca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sca.model.Accesorio;

@Repository
public interface AccesorioRepository  extends JpaRepository <Accesorio, Long>{

}
