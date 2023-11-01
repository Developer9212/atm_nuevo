package com.fenoreste.atms.service;

import java.util.Date;

public interface ISaiFuncionesService {

	public String sai_auxiliar(Integer idorigenp,Integer idproducto,Integer idauxiliar);
	public String sai_prestamo_cuanto(Integer idorigenp,Integer idproducto,Integer idauxiliar,Date fecha,Integer tipoamortizacion,String sai);
	public String sesion();
	public boolean servicios_activos();
	public String sai_aplica_transaccion(Integer idusuario,String sesion,String referencia);
	public String sai_detalle_transaccion_aplicada(Date fecha,Integer idusuario,String sesion,String referencia);
	public String sai_termina_transaccion(Date fecha,Integer idusuario,String sesion,String referencia);
}
