package com.fenoreste.atms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fenoreste.atms.entity.Origen;
import com.fenoreste.atms.repository.OrigenDao;


@Service
public class OrigenServiceImpl implements IOrigenService{
    
	@Autowired
	private OrigenDao origenDao;
	
	@Override
	public Origen buscarPorId(Integer idorigen) {
		return origenDao.findById(idorigen).orElse(null);
	}

	@Override
	public Origen origenMatriz() {
		return origenDao.buscarMatriz();
	}

	
}
