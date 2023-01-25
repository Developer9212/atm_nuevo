package com.fenoreste.atms.repository;


import java.util.Date;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fenoreste.atms.entity.Auxiliar;


public interface SaiFuncionesRepository extends CrudRepository<Auxiliar ,Integer>  {
	
	@Query(value = "SELECT sai_auxiliar(?,?,?,(SELECT date(fechatrabajo) FROM origenes LIMIT 1))" , nativeQuery = true)
	String sai_auxiliar(Integer idorigenp,Integer idproducto,Integer idauxiliar);
	
	@Query(value="SELECT sai_bankingly_prestamo_cuanto(?1,?2,?3,?4,?5,?6)", nativeQuery=true)
	String sai_prestamo_cuanto(Integer idorigenp,Integer idproducto,Integer idauxiliar,Date fecha,Integer tipoamortizacion,String sai);

	@Query(value = "SELECT text(pg_backend_pid())||'-'||trim(to_char(now(),'ddmmyy'))" , nativeQuery = true)
	String sesion();

	@Query(value ="SELECT sai_cajeros_atm_servicio_activo_inactivo()" , nativeQuery = true)
	boolean servicios_activos();

	@Query(value ="SELECT sai_bankingly_aplica_transaccion(?,?,?,?)",nativeQuery = true)
	String sai_procesa_transaccion(Date fecha,Integer idusuario,String sesion,String referencia);

	@Query(value = "SELECT sai_bankingly_limite_adelanto(?,?,?,(SELECT date(fechatrabajo) FROM origenes limit 1),NULL)", nativeQuery = true)
	String sai_limite_adelanto(Integer idorigenp,Integer idproducto,Integer idauxiliar,Double amount);

	@Query(value ="SELECT sai_bankingly_detalle_transaccion_aplicada(?,?,?,?)", nativeQuery = true)
	String sai_detalle_transaccion_aplicada(Date fecha,Integer idusuario,String sesion,String referencia);

	@Query(value ="SELECT sai_bankingly_termina_transaccion(?,?,?,?)",nativeQuery = true)
	String sai_termina_transaccion(Date fecha,Integer idusuario,String sesion,String referencia);
}
