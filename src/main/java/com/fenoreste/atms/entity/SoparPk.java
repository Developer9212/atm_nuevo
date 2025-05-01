package com.fenoreste.atms.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoparPk implements Serializable {

    private Integer idorigen;
    private Integer idgrupo;
    private Integer idsocio;
    private String tipo;


    private static final long serialVersionUID = 1L;

}

