package com.fenoreste.atms.service;

import com.fenoreste.atms.entity.RegistraMovimiento;

public interface IProcesarMovimientoService {
	
	public RegistraMovimiento insertarMovimiento(RegistraMovimiento mov);
	public boolean eliminaMovimiento(RegistraMovimiento mov);
	
}
