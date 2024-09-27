package com.fenoreste.atms.repository;


import java.math.BigDecimal;
import java.util.Date;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fenoreste.atms.entity.Auxiliar;


public interface FuncionesDao extends CrudRepository<Auxiliar ,Integer>  {
	
	@Query(value = "SELECT sai_auxiliar(?1,?2,?3,(SELECT date(fechatrabajo) FROM origenes LIMIT 1))" , nativeQuery = true)
	String sai_auxiliar(Integer idorigenp,Integer idproducto,Integer idauxiliar);
	
	@Query(value="SELECT sai_cajero_receptor_prestamo_cuanto(?1,?2,?3,?4,?5,?6)", nativeQuery=true)
	String sai_prestamo_cuanto(Integer idorigenp,Integer idproducto,Integer idauxiliar,Date fecha,Integer tipoamortizacion,String sai);

	@Query(value = "SELECT text(pg_backend_pid())||'-'||trim(to_char(now(),'ddmmyy'))" , nativeQuery = true)
	String sesion();

	@Query(value ="SELECT sai_cajero_receptor_servicio_activo_inactivo()" , nativeQuery = true)
	boolean servicios_activos();

	@Query(value ="SELECT sai_cajero_receptor_aplica_transaccion(?1,?2,?3)",nativeQuery = true)
	String sai_procesa_transaccion(Integer idusuario,String sesion,String referencia);

	@Query(value = "SELECT sai_cajero_receptor_limite_adelanto(?1,?2,?3,(SELECT date(fechatrabajo) FROM origenes limit 1),?4,null)", nativeQuery = true)
	String sai_limite_adelanto(Integer idorigenp,Integer idproducto,Integer idauxiliar,BigDecimal monto);

	@Query(value ="SELECT sai_cajero_receptor_detalle_transaccion_aplicada(?1,?2,?3,?4)", nativeQuery = true)
	String sai_detalle_transaccion_aplicada(Date fecha,Integer idusuario,String sesion,String referencia);

	@Query(value ="SELECT sai_cajero_receptor_termina_transaccion(?1,?2,?3,?4)",nativeQuery = true)
	String sai_termina_transaccion(Date fecha,Integer idusuario,String sesion,String referencia);

	@Query(value ="SELECT monto_para_liquidar_prestamo(?1,?2,?3,?4)",nativeQuery = true)
	String monto_liquidar_prestamos(Integer idorigenp,Integer idproducto,Integer idauxiliar,Date fecha);

}
