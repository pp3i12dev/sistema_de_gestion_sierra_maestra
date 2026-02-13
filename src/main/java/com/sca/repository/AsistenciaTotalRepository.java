package com.sca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sca.model.AsistenciaTotal;

@Repository
public interface AsistenciaTotalRepository  extends JpaRepository <AsistenciaTotal, Long>{

}
