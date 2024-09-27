package com.fenoreste.atms.entity;

import lombok.*;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario implements Serializable {

    @Id
    private Integer idusuario;
    private String nombre;
    private boolean activo;

    private static final long serialVersionUID = 1L;
}
