package com.fenoreste.atms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ws_siscoop_folios_tarjetas")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class FolioTarjeta implements Serializable {

	@Id
	@Column(name = "idorigenp")
	private int idorigenp;
	@Column(name = "idproducto")
	private int idproducto;
	@Column(name = "idauxiliar")
	private int idauxiliar;
	@Column(name = "idtarjeta")
	private String idtarjeta;
	@Column(name = "asignada")
	private Boolean asignada;
	@Column(name = "activa")
	private Boolean activa;
	@Column(name = "bloqueada")
	private Boolean bloqueada;
	@Column(name = "fecha_hora")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaHora;

	private static final long serialVersionUID = 1L;

}
