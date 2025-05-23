/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.atms.entity;

import java.io.Serializable;
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
 * @author wilmer
 */

@Entity
@Table(name = "estados")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Estado implements Serializable {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idestado;
    private String nombre;
    private String ef;
    private Integer idpais;

    private static final long serialVersionUID = 1L;
        
    
}
