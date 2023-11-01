package com.fenoreste.atms.controller;

import java.util.ArrayList;
import java.util.List;

import com.fenoreste.atms.service.ISaiFuncionesService;
import com.github.cliftonlabs.json_simple.JsonObject;

import lombok.extern.slf4j.Slf4j;

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
import com.fenoreste.atms.modelo.TxConsultaCta;
import com.fenoreste.atms.modelo.TxDeposito;
import com.fenoreste.atms.modelo.column;
import com.fenoreste.atms.modelo.Comprobante;
import com.fenoreste.atms.modelo.ModeloObjetoInformacionDeposito;
import com.fenoreste.atms.modelo.ModeloObjetoInformacionDetallesCuenta;
import com.fenoreste.atms.modelo.ObjetoFirma;
import com.fenoreste.atms.service.ServiceGeneralSpring;
import com.fenoreste.atms.util.FirmarCadenaPeticion;

@RestController
@CrossOrigin("*")
@RequestMapping("api")
@Slf4j
public class ATMSController {
	
	@Autowired
	private ServiceGeneralSpring service;
	
	@Autowired
	private FirmarCadenaPeticion firmarCadena;

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
		JsonObject json = new JsonObject();
		ObjetoFirma objetoFirma = firmarCadena.firma(request);
		String firma  = firmarCadena.firma(objetoFirma);
		log.info(firma);
		if(request.getFirma().equals(firma)) {		
		if(saiFuncionesService.servicios_activos()){
			Comprobante comprobante = service.depositoTX(request);
			ModeloObjetoInformacionDeposito informacion = new ModeloObjetoInformacionDeposito();
			log.info("...."+comprobante);
			if(comprobante.getCodigo() == 200) {
				informacion.setResponse(comprobante);
				informacion.setEstatusProceso(0);
				informacion.setMensaje("Transaccion Exitosa");
				informacion.setNumeroAutorizacion(comprobante.getNoAutorizacion());
			}else {
				informacion.setEstatusProceso(1);
				informacion.setMensaje("Error al procesar la transaccion:"+comprobante.getMensajeUsuario());				
			}
			return new ResponseEntity<>(informacion,HttpStatus.OK);
		}else{
			json.put("Descripcion de error","Operacion fuera de horario");
			return new ResponseEntity<>(json,HttpStatus.CONFLICT);
		}
		
	}else {
		log.info("..........Firmas no coinciden,favor de validar su certificado..............");
		json.put("Descripcion de error","Firmas no coinciden");
		return new ResponseEntity<>(json,HttpStatus.CONFLICT);
	}
	}

	
	
}
