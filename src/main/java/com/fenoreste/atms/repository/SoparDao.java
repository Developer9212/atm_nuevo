package com.fenoreste.atms.repository;

import com.fenoreste.atms.entity.Sopar;
import com.fenoreste.atms.entity.SoparPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoparDao extends JpaRepository<Sopar, SoparPk> {
}
