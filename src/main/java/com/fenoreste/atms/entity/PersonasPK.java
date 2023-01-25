package com.fenoreste.atms.entity;

import java.io.Serializable;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonasPK implements Serializable{
	
	private Integer idorigen;
	private Integer idgrupo;
	private Integer idsocio;
	
	
	private static final long serialVersionUID = 1L;

}
