package com.fenoreste.atms.service;

import com.fenoreste.atms.entity.Amortizacion;
import com.fenoreste.atms.entity.AuxiliarPK;
import com.fenoreste.atms.entity.Auxiliar_d;
import com.fenoreste.atms.repository.AuxiliarDDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;

@Service
public class AuxiliarDServiceImpl implements IAuxiliarDService {

    @Autowired
    private AuxiliarDDao auxiliarDDao;

    @Override
    public double montoDiario(AuxiliarPK auxiliarPK, Date fecha,Integer idusuario) {
        return auxiliarDDao.montoDiario(auxiliarPK.getIdorigenp(),auxiliarPK.getIdproducto(),auxiliarPK.getIdauxiliar(),fecha,idusuario);
    }

    @Override
    public double montoMensual(AuxiliarPK auxiliarPK, String fecha, Integer idusuario) {
        return auxiliarDDao.montoMensual(auxiliarPK.getIdorigenp(),auxiliarPK.getIdproducto(),auxiliarPK.getIdauxiliar(),fecha,idusuario);
    }

    @Override
    public Auxiliar_d ultimoMovimiento(AuxiliarPK auxiliarPK) {
        return auxiliarDDao.ultimoMovimiento(auxiliarPK.getIdorigenp(),auxiliarPK.getIdproducto(),auxiliarPK.getIdauxiliar());
    }


}
