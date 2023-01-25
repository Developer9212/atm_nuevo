package com.fenoreste.atms.modelo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Columns {
	
    private List<column>columns;
    private Integer columnsLen;

}
