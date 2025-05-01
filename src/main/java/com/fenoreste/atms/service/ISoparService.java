package com.fenoreste.atms.service;

import com.fenoreste.atms.entity.PersonaPK;
import com.fenoreste.atms.entity.Sopar;
import com.fenoreste.atms.entity.SoparPk;

public interface ISoparService {

    public Sopar bloqueoListaNegra(SoparPk soparPk);
}
