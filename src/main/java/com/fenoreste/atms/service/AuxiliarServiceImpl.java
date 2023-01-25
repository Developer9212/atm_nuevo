package com.fenoreste.atms.service;

import com.fenoreste.atms.entity.PersonasPK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fenoreste.atms.entity.Auxiliar;
import com.fenoreste.atms.entity.AuxiliarPK;
import com.fenoreste.atms.repository.AuxiliarDao;


@Service
public class AuxiliarServiceImpl implements IAuxiliarService{

	@Autowired
	AuxiliarDao auxiliarDao;
	
	@Override
	public Auxiliar buscarPorOpa(AuxiliarPK auxPk) {
		return auxiliarDao.findById(auxPk).orElse(null);
	}

	@Override
	public Auxiliar buscarPorOgsProducto(PersonasPK pk, Integer idProducto) {
		return auxiliarDao.buscarPorIdProducto(pk.getIdorigen(),pk.getIdgrupo(),pk.getIdsocio(),idProducto);
	}


}
