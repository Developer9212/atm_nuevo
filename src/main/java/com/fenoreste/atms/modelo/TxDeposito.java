package com.fenoreste.atms.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TxDeposito {

	private String idCajero;
	private String fechaHora;
	private Integer tipoTX;
	private String referencia;
	private String cuenta;
	private Integer secuencia;
	private Double montoIngresado;
	private Double montoDepositado;
	private Double montoCambio;
	private String firma;
}
