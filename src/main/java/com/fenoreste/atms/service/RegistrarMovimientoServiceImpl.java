package com.fenoreste.atms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fenoreste.atms.entity.RegistraMovimiento;
import com.fenoreste.atms.repository.RegistrarMovimientoDao;

@Service
public class RegistrarMovimientoServiceImpl implements IRegistraMovimientoService {

	@Autowired
	RegistrarMovimientoDao procesarDao;
	
	@Override
	public RegistraMovimiento insertarMovimiento(RegistraMovimiento mov) {	
		System.out.println("Ya llego");
		return procesarDao.save(mov);
	}
	
	@Override
	public boolean eliminaMovimiento(RegistraMovimiento mov) { 
		return false;
	}

}
