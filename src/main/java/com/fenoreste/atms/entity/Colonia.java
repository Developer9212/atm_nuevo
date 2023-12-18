package com.fenoreste.atms.entity;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author root
 */

@Entity
@Table(name = "colonias")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Colonia implements Serializable {

    @Id
    @Column(name = "idcolonia")
    private Integer idcolonia;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "idmunicipio")
    private Integer idmunicipio;
    @Column(name = "codigopostal")
    private String codigopostal;

    private static final long serialVersionUID = 1L;
    
}


