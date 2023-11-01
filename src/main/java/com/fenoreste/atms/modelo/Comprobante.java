package com.fenoreste.atms.modelo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comprobante {
    
	private String descripcionCuenta;
	private Double monto;
	private Double montoNoDepositado;
	private String fechaHora;
	private List<Columns> detalleComprobante; 
	private String noAutorizacion;
	//Solo se escribe para pasar informacion pero nunca se lee
	@JsonProperty(access = Access.WRITE_ONLY)
	private Integer codigo;
    @JsonInclude(value = Include.NON_NULL)
	private String mensajeUsuario;
}
