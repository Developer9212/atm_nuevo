package com.fenoreste.atms.service;

import com.fenoreste.atms.entity.Amortizacion;
import com.fenoreste.atms.entity.AuxiliarPK;
import com.fenoreste.atms.entity.Auxiliar_d;

import java.util.Date;

public interface IAuxiliarDService {

    public double montoDiario(AuxiliarPK auxiliarPK, Date fecha,Integer idusuario);

    public double montoMensual(AuxiliarPK auxiliarPK, String fecha,Integer idusuario);

    public Auxiliar_d ultimoMovimiento(AuxiliarPK auxiliarPK);
}
