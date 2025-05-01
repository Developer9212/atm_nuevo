package com.fenoreste.atms.service;

import com.fenoreste.atms.entity.PersonaPK;
import com.fenoreste.atms.entity.Sopar;
import com.fenoreste.atms.entity.SoparPk;
import com.fenoreste.atms.repository.SoparDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SoparServiceImpl implements ISoparService{

    @Autowired
    private SoparDao soparDao;

    @Override
    public Sopar bloqueoListaNegra(SoparPk soparPk) {
        return soparDao.findById(soparPk).orElse(null);
    }
}
