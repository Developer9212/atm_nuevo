package com.fenoreste.atms.controller;

import java.util.ArrayList;
import java.util.List;

import com.fenoreste.atms.service.ISaiFuncionesService;
import com.github.cliftonlabs.json_simple.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fenoreste.atms.modelo.Columns;
import com.fenoreste.atms.modelo.ModeloObjetoInformacion;
import com.fenoreste.atms.modelo.TxConsultaCta;
import com.fenoreste.atms.modelo.TxDeposito;
import com.fenoreste.atms.modelo.column;
import com.fenoreste.atms.modelo.Comprobante;
import com.fenoreste.atms.service.ServiceGeneralSpring;

@RestController
@CrossOrigin("*")
@RequestMapping("atms/api")
public class ATMSController {
	
	@Autowired
	private ServiceGeneralSpring service;

	@Autowired
	private ISaiFuncionesService saiFuncionesService;
	
	@RequestMapping(value = "detalleCuenta", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> informacionCuenta(@RequestBody TxConsultaCta request) {
		if(saiFuncionesService.servicios_activos()){
			return new ResponseEntity<>(service.DetallesCuenta(request), HttpStatus.OK);
		}else{
			JsonObject json = new JsonObject();
			json.put("Descripcion de error","Operacion fuera de horario");
			return new ResponseEntity<>(json, HttpStatus.CONFLICT);
		}

	}
	
	@RequestMapping(value = "deposito", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> depositoTX(@RequestBody TxDeposito request) {
		column co = new column();
		co.setAligment("f");
		co.setDecoration("d");
		co.setLen(8);
		co.setText("d");
		Columns colu = new Columns();

		List<column>list = new ArrayList<>();
		list.add(co);
		colu.setColumns(list);

		List<Columns> li = new ArrayList<>();
		li.add(colu);
		Comprobante c = new Comprobante();
		c.setDetalleComprobante(li);
		if(saiFuncionesService.servicios_activos()){
			return new ResponseEntity<>(service.depositoTX(request),HttpStatus.OK);
		}else{
			JsonObject json = new JsonObject();
			json.put("Descripcion de error","Operacion fuera de horario");
			return new ResponseEntity<>(json,HttpStatus.CONFLICT);
		}
	}

	
	
}
