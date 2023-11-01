package com.fenoreste.atms.service;

import com.fenoreste.atms.entity.Tabla;
import com.fenoreste.atms.entity.TablaPK;
import com.fenoreste.atms.repository.TablaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TablasServiceImpl implements ITablaService{

    @Autowired
    TablaDao tablaRepository;

    @Override
    public Tabla buscarPorId(TablaPK pk) {
        return tablaRepository.findById(pk).orElse(null);
    }
}
