package com.fenoreste.atms.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fenoreste.atms.repository.SaiFuncionesRepository;


@Service
public class SaiFuncionesServiceImpl implements ISaiFuncionesService{

	@Autowired
	SaiFuncionesRepository saiFuncionesDao;
	
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
	public String sai_aplica_transaccion(Date fecha, Integer idusuario, String sesion, String referencia) {
		return saiFuncionesDao.sai_procesa_transaccion(fecha,idusuario,sesion,referencia);
	}

	@Override
	public String sai_detalle_transaccion_aplicada(Date fecha, Integer idusuario, String sesion, String referencia) {
		return saiFuncionesDao.sai_detalle_transaccion_aplicada(fecha,idusuario,sesion,referencia);
	}

	@Override
	public String sai_termina_transaccion(Date fecha, Integer idusuario, String sesion, String referencia) {
		return saiFuncionesDao.sai_termina_transaccion(fecha,idusuario,sesion,referencia);
	}

}
