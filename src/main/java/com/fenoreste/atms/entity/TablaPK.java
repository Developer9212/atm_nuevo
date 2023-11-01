package com.fenoreste.atms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
public class TablaPK implements Serializable {
    @Column(name="idtabla")
    private String idtabla;
    @Column(name="idelemento")
    private String idelemento;

    final static long serialVersionUID = 1L;
}
