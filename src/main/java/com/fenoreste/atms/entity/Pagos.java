package com.fenoreste.atms.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "amortizaciones_cubiertas_abonos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Pagos implements Serializable {   

	@Id
    @Column(name="idamortizacion")
    private Integer idamortizacion;
    @Column(name="idorigenp")
    private Integer idorigenp;
    @Column(name="idproducto")
    private Integer idproducto;
    @Column(name="idauxiliar")
    private Integer idauxiliar; 
	@Column(name = "vence")
    @Temporal(TemporalType.DATE)
    private Date vence;
	@Column(name = "todopag")
    private Boolean todopag;
    @Column(name = "atiempo")
    private Boolean atiempo;
	@Column(name = "abono")
    private BigDecimal abono;
	@Column(name = "abonopag")
    private BigDecimal abonopag;
	@Column(name = "fecha_pago")
    @Temporal(TemporalType.DATE)
    private Date fecha_pago;
	@Column(name="monto_abonado")
    private BigDecimal monto_abonado;
	@Column(name="diasvencidos")
    private Integer diasvencidos;
	@Column(name="io")
    private BigDecimal io;
	@Column(name="im")
    private BigDecimal im;
    
	private static final long serialVersionUID = 1L;
		
}
