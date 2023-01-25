package com.fenoreste.atms.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="sopar")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Sopar implements Serializable {

	@Id
	@Column(name="idusuario")
	private Integer idusuario;	
	@Column(name="idorigen")
	private Integer idorigen;
	@Column(name="idgrupo")
	private Integer idgrupo;
	@Column(name="idsocio")
	private Integer idsocio;
	@Column(name="tipo")
	private String tipo;
	@Column(name="departamento")
	private String departamento;
	@Column(name="puesto")
	private String puesto;
	
    private static final long serialVersionUID = 1L;	
}
