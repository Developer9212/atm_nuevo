package com.fenoreste.atms.modelo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"ObjetoInformacion","EstatusProceso","IdTransaccion","Mensaje"})
public class ModeloObjetoInformacionDetallesCuenta {
    
	@JsonProperty("ObjetoInformacion")
	private List<ModeloDatosCuenta> ObjetoInformacion;
	@JsonProperty("EstatusProceso")
	private Integer EstatusProceso;
	@JsonProperty("IdTransaccion")
	private String IdTransaccion;
	@JsonProperty("Mensaje")
	private String Mensaje;
	@JsonProperty(access = Access.WRITE_ONLY)	
	private Integer Codigo;
	
}
