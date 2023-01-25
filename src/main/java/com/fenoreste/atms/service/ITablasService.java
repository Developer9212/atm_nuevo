package com.fenoreste.atms.service;

import com.fenoreste.atms.entity.Tabla;
import com.fenoreste.atms.entity.TablasPK;

public interface ITablasService {

    public Tabla findById(TablasPK pk);

}
