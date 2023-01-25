package com.fenoreste.atms.repository;

import com.fenoreste.atms.entity.Auxiliar;
import com.fenoreste.atms.entity.PersonasPK;
import com.fenoreste.atms.entity.Tabla;
import com.fenoreste.atms.entity.TablasPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TablaRepository extends JpaRepository<Tabla, TablasPK> {



}
