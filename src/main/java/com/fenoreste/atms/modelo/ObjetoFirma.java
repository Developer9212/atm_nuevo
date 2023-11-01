package com.fenoreste.atms.modelo;

import java.io.Serializable;

import lombok.Data;

@Data
public class ObjetoFirma implements Serializable{
	
	private String idCajero;
	private String fechaHora;
	private Integer tipoTX;
	private String referencia;
	private String cuenta;
	private Integer secuencia;
	private Double montoIngresado;
	private Double montoDepositado;
	private Double montoCambio;
	private String secret;
	
	
	private static final long serialVersionUID = 1L;

}
