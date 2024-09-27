package com.fenoreste.atms.repository;

import com.fenoreste.atms.entity.Amortizacion;
import com.fenoreste.atms.entity.AuxiliarPK;
import com.fenoreste.atms.entity.Auxiliar_d;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;


public interface AuxiliarDDao extends CrudRepository<Auxiliar_d, AuxiliarPK> {
    @Query(value = "SELECT case when sum(monto) > 0 then sum(ad.monto) else 0 end FROM auxiliares_d ad INNER JOIN polizas p USING(idorigenc,periodo,idtipo,idpoliza) WHERE " +
            " ad.idorigenp = ?1 AND ad.idproducto = ?2 AND ad.idauxiliar = ?3 AND p.idusuario = ?5 AND date(ad.fecha) = ?4 AND ad.cargoabono=1", nativeQuery = true)
    public double montoDiario(int idorigenp, int idproducto, int idauxiliar, Date fecha,int idusuario);


    @Query(value = "SELECT case when sum(monto) > 0 then sum(ad.monto) else 0 end FROM auxiliares_d ad INNER JOIN polizas p USING(idorigenc,periodo,idtipo,idpoliza) WHERE " +
            " ad.idorigenp = ?1 AND ad.idproducto = ?2 AND ad.idauxiliar = ?3 AND p.idusuario = ?5 AND to_char(date(ad.fecha),'yyyymm') = ?4 AND ad.cargoabono=1", nativeQuery = true)

    public double montoMensual(int idorigenp, int idproducto, int idauxiliar, String fecha,int idusuario);

    @Query(value = "SELECT * FROM auxiliares_d WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 AND cargoabono = 1 ORDER BY fecha DESC LIMIT 1",nativeQuery = true)
    public Auxiliar_d ultimoMovimiento(Integer idorigenp, Integer idproducto, Integer idauxiliar);

}
