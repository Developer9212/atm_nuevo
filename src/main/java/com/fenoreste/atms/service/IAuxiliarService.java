package com.fenoreste.atms.service;

import java.util.List;

import com.fenoreste.atms.entity.Auxiliar;
import com.fenoreste.atms.entity.AuxiliarPK;
import com.fenoreste.atms.entity.PersonaPK;

public interface IAuxiliarService {

	public Auxiliar buscarPorOpa(AuxiliarPK auxPk);
	public Auxiliar buscarPorOgsProducto(PersonaPK pk,Integer idProducto);
}
