package com.fenoreste.atms.modelo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"ObjetoInformacion","EstatusProceso","IdTransaccion","Mensaje"})
public class ModeloObjetoInformacion {
    
	@JsonProperty("ObjetoInformacion")
	private List<ModeloDatosCuenta> ObjetoInformacion;
	@JsonProperty("EstatusProceso")
	private Integer EstatusProceso;
	@JsonProperty("IdTransaccion")
	private String IdTransaccion;
	@JsonProperty("Mensaje")
	private String Mensaje;
}
