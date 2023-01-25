package com.fenoreste.atms.service;

import com.fenoreste.atms.entity.Tabla;
import com.fenoreste.atms.entity.TablasPK;
import com.fenoreste.atms.repository.TablaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TablasServiceImpl implements ITablasService{

    @Autowired
    TablaRepository tablaRepository;

    @Override
    public Tabla findById(TablasPK pk) {
        return tablaRepository.findById(pk).orElse(null);
    }
}
