package com.fenoreste.atms.repository;

import com.fenoreste.atms.entity.PersonaPK;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fenoreste.atms.entity.Auxiliar;
import com.fenoreste.atms.entity.AuxiliarPK;
import org.springframework.data.jpa.repository.Query;

public interface AuxiliarDao extends JpaRepository<Auxiliar,AuxiliarPK> {
    @Query(value = "SELECT * FROM auxiliares WHERE idorigen=?1 AND idgrupo=?2 AND idsocio=?3 AND idproducto=?4 AND estatus=2",nativeQuery = true)
    Auxiliar buscarPorIdProducto(Integer idorigen,Integer idgrupo,Integer idsocio, Integer idProducto);
}
