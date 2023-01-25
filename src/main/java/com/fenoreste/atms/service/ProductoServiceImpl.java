package com.fenoreste.atms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fenoreste.atms.entity.Producto;
import com.fenoreste.atms.repository.ProductoDao;

@Service
public class ProductoServiceImpl implements IProductoService{

	@Autowired
	ProductoDao productoDao;
	
	@Override
	public Producto buscarPorId(Integer id) {		
		return productoDao.findById(id).orElse(null);
	}

}
