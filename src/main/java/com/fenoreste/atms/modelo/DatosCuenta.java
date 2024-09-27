package com.fenoreste.atms.modelo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatosCuenta {
   
	@JsonProperty("Cuenta")
	private String Cuenta;
	@JsonProperty("DescripcionCuenta")
	private String DescripcionCuenta;
	@JsonProperty("DetalleCuenta")
	private String DetalleCuenta;
	private BigDecimal montoProximoPago;
	private BigDecimal minDeposito;
	private BigDecimal maxDeposito;
	@JsonProperty("CuentaDefault")
	private String DefaultCuenta;
	@JsonProperty("DescripcionCuentaDefault")
	private String DescripcionCuentaDefault;
	@JsonProperty("CuentaCambio")
	private String CuentaCambio;
	@JsonProperty("DescripcionCuentaCambio")
	private String DescripcionCuentaCambio;	
	private BigDecimal maxCambio;
	
}
