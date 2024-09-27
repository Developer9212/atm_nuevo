package com.fenoreste.atms.service;

import com.fenoreste.atms.entity.Origen;

public interface IOrigenService {
  
	public Origen buscarPorId(Integer idorigen); 
	public Origen origenMatriz();
}
