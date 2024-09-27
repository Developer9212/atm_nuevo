package com.fenoreste.atms.service;

import java.math.BigDecimal;
import java.util.Date;

import com.fenoreste.atms.entity.AuxiliarPK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fenoreste.atms.repository.FuncionesDao;


@Service
public class FuncionesServiceImpl implements IFuncionesService{

	@Autowired
	private FuncionesDao saiFuncionesDao;
	
	@Override
	public String sai_auxiliar(Integer idorigenp, Integer idproducto, Integer idauxiliar) {
		return saiFuncionesDao.sai_auxiliar(idorigenp, idproducto, idauxiliar);
	}
	
	@Override
	public String sai_prestamo_cuanto(Integer idorigenp, Integer idproducto, Integer idauxiliar,Date fecha,Integer tipoamortizacion, String sai) {
		return saiFuncionesDao.sai_prestamo_cuanto(idorigenp, idproducto, idauxiliar,fecha,tipoamortizacion, sai);
	}

	@Override
	public String sesion() {
		return saiFuncionesDao.sesion();
	}

	@Override
	public boolean servicios_activos() {
		return saiFuncionesDao.servicios_activos();
	}

	@Override
	public String sai_aplica_transaccion(Integer idusuario, String sesion, String referencia) {
		return saiFuncionesDao.sai_procesa_transaccion(idusuario,sesion,referencia);
	}

	@Override
	public String sai_detalle_transaccion_aplicada(Date fecha, Integer idusuario, String sesion, String referencia) {
		return saiFuncionesDao.sai_detalle_transaccion_aplicada(fecha,idusuario,sesion,referencia);
	}

	@Override
	public String sai_termina_transaccion(Date fecha, Integer idusuario, String sesion, String referencia) {
		return saiFuncionesDao.sai_termina_transaccion(fecha,idusuario,sesion,referencia);
	}

	@Override
	public Double monto_liquidar_prestamo(AuxiliarPK pk) {
		return Double.parseDouble(saiFuncionesDao.monto_liquidar_prestamos(pk.getIdorigenp(),pk.getIdproducto(),pk.getIdauxiliar(),new Date()));
	}

	@Override
	public String sai_limite_adelanto(Integer idorigenp, Integer idproducto, Integer idauxiliar,Double monto) {		
		return saiFuncionesDao.sai_limite_adelanto(idorigenp, idproducto, idauxiliar, new BigDecimal(monto));
	}

}
