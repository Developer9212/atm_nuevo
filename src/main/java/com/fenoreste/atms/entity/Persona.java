/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.atms.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
/**
 *
 * @author Elliot
 */

@Entity
@Table(name = "personas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Persona implements Serializable{
    
	@EmbeddedId
    private PersonasPK personasPK;    
    private String calle;
    private String numeroext;
    private String numeroint;
    private String entrecalles;
    @Temporal(TemporalType.DATE)
    private Date fechanacimiento;
    private String lugarnacimiento;
    private Integer efnacimiento;
    private Short sexo;
    private String telefono;
    private String telefonorecados;
    private Boolean listanegra;
    private Short estadocivil;
    private String idcoop;
    private Integer idsector;
    private Boolean estatus;
    private Boolean aceptado;
    @Temporal(TemporalType.DATE)
    private Date fechaingreso;
    @Temporal(TemporalType.DATE)
    private Date fecharetiro;
    @Temporal(TemporalType.DATE)
    private Date fechaciudad;
    private Short regimenMat;
    private String nombre;
    private Short medioInf;
    private Integer requisitos;
    private String appaterno;
    private String apmaterno;
    private Short nacionalidad;
    private Short gradoEstudios;
    private Short categoria;
    private String rfc;
    private String curp;
    private String email;
    private String razonSocial;
    private Integer causaBaja;
    private Short nivelRiesgo;
    private String celular;
    private Boolean rfcValido;
    private Boolean curpValido;
    private Integer idcolonia;
     
    private static final long serialVersionUID = 1L;
    
}
