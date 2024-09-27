package com.fenoreste.atms.repository;

import com.fenoreste.atms.entity.Auxiliar;
import com.fenoreste.atms.entity.PersonaPK;
import com.fenoreste.atms.entity.Tabla;
import com.fenoreste.atms.entity.TablaPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TablaDao extends JpaRepository<Tabla, TablaPK> {



}
