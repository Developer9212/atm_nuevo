package com.fenoreste.atms.service;

import com.fenoreste.atms.entity.Tabla;
import com.fenoreste.atms.entity.TablaPK;

public interface ITablaService {

    public Tabla buscarPorId(TablaPK pk);

}
