package com.fenoreste.atms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fenoreste.atms.entity.Auxiliar;



public interface ExtraDao extends CrudRepository<Auxiliar, Long> {

	@Query(value = "SELECT text(pg_backend_pid())||'-'||trim(to_char(now(),'ddmmyy'))" , nativeQuery = true)
	String sesion();
	
	@Query(value ="SELECT sai_bankingly_servicio_activo_inactivo()" , nativeQuery = true)
	boolean servicios_activos();
	
}
