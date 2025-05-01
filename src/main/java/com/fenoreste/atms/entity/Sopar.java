package com.fenoreste.atms.entity;

import java.io.Serializable;

import javax.persistence.*;

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

	@EmbeddedId
	private SoparPk soparPk;
	private Integer idusuario;
	private String departamento;
	private String puesto;
	
    private static final long serialVersionUID = 1L;	
}
