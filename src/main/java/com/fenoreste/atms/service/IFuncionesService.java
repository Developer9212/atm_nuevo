package com.fenoreste.atms.service;

import com.fenoreste.atms.entity.AuxiliarPK;

import java.util.Date;

public interface IFuncionesService {

	public String sai_auxiliar(Integer idorigenp,Integer idproducto,Integer idauxiliar);
	public String sai_prestamo_cuanto(Integer idorigenp,Integer idproducto,Integer idauxiliar,Date fecha,Integer tipoamortizacion,String sai);
	public String sai_limite_adelanto(Integer idorigenp,Integer idproducto,Integer idauxiliar,Double monto);
	public String sesion();
	public boolean servicios_activos();
	public String sai_aplica_transaccion(Integer idusuario,String sesion,String referencia);
	public String sai_detalle_transaccion_aplicada(Date fecha,Integer idusuario,String sesion,String referencia);
	public String sai_termina_transaccion(Date fecha,Integer idusuario,String sesion,String referencia);
    public Double monto_liquidar_prestamo(AuxiliarPK pk);
}
