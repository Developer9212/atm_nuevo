package com.fenoreste.atms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fenoreste.atms.entity.AuxiliarPK;
import com.fenoreste.atms.entity.RegistraMovimiento;

public interface ProcesarMovsDao extends JpaRepository<RegistraMovimiento,AuxiliarPK> {
    
	  
}
