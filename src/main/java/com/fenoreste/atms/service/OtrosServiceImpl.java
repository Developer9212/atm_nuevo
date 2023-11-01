package com.fenoreste.atms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fenoreste.atms.repository.ExtraRepository;


@Service
public class OtrosServiceImpl implements IOtrosService {

	@Autowired
	private ExtraRepository otrosDao;

	@Override
	public String sesion() {
		return otrosDao.sesion();
	}
	
	
	

}
