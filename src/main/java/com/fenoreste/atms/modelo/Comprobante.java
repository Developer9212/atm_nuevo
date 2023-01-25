package com.fenoreste.atms.modelo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comprobante {
    
	private String descripcionCuenta;
	private Integer monto;
	private Integer montoNoDepositado;
	private String fechaHora;
	private List<Columns> detalleComprobante; 
	private String noAutorizacion;
}
