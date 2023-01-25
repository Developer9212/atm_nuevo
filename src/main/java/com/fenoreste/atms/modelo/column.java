package com.fenoreste.atms.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class column {

	private String aligment;
	private String decoration;
	private String text;
	private Integer len;	 
}
