package com.fenoreste.atms.entity;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author wilmer
 */
@Entity
@Table(name = "cajero_receptor_movimientos_ca")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistraMovimiento implements Serializable {
    @EmbeddedId
    private MovimientoPK pk;
	@Column(name="idorigenp")
	private Integer idorigenp = 0;    
	@Column(name="idproducto")
	private Integer idproducto = 0;
	@Column(name = "idauxiliar")
	private Integer idauxiliar= 0;
    @Column(name = "fecha")
    private Timestamp fecha;
    @Column(name = "idorigen")
    private Integer idorigen;
    @Column(name = "idgrupo")
    private Integer idgrupo;
    @Column(name = "idsocio")
    private Integer idsocio;
    @Column(name = "cargoabono")
    private Integer cargoabono;
    @Column(name = "monto")
    private Double monto;
    @Column(name = "idcuenta")
    private String idcuenta;
    @Column(name = "iva")
    private Double iva;
    @Column(name = "tipo_amort")
    private Integer tipo_amort;   
    @Column(name = "sai_aux")
    private String sai_aux;

	private static final long serialVersionUID = 1L;

    
}
