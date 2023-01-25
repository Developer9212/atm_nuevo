package com.fenoreste.atms.util;

import org.springframework.stereotype.Service;

import com.fenoreste.atms.modelo.ogsDTO;
import com.fenoreste.atms.modelo.opaDTO;

@Service
public class HerramientasUtil {

	public ogsDTO ogs(String customerId) {
		ogsDTO ogs = new ogsDTO();
		ogs.setIdorigen(Integer.parseInt(customerId.substring(0, 6)));
		ogs.setIdgrupo(Integer.parseInt(customerId.substring(6, 8)));
		ogs.setIdsocio(Integer.parseInt(customerId.substring(8, 14)));
		return ogs;
	}

	public opaDTO opa(String productBankIdentifier) {
		opaDTO opa = new opaDTO();
		int count = 0;
		for (int i = 0; i < productBankIdentifier.length(); i++) {
			count = count + 1;
		}
		if (count == 18) {
			opa.setIdorigenp(Integer.parseInt(productBankIdentifier.substring(0, 5)));
			opa.setIdproducto(Integer.parseInt(productBankIdentifier.substring(5, 10)));
			opa.setIdauxiliar(Integer.parseInt(productBankIdentifier.substring(10, 18)));
		} else {
			opa.setIdorigenp(Integer.parseInt(productBankIdentifier.substring(0, 6)));
			opa.setIdproducto(Integer.parseInt(productBankIdentifier.substring(6, 11)));
			opa.setIdauxiliar(Integer.parseInt(productBankIdentifier.substring(11, 19)));
		}

		return opa;
	}

}
