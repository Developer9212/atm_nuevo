package com.fenoreste.atms.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TxConsultaCta {
	
	private String idCajero;
	private String fechaHora;
	private String cuenta;
	private Integer tipoCuenta;

}
