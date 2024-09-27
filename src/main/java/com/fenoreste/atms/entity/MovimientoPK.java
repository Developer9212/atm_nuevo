package com.fenoreste.atms.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoPK implements Serializable{
     
	 @Column(name = "idusuario")
	 private Integer idusuario;
	 @Column(name = "sesion")
	 private String sesion;
	 @Column(name = "referencia")
	 private String referencia;
	 @Column(name = "n_mov")
	 private Integer n_mov = 0;
	 
	 private static final long serialVersionUID = 1L;
}
