package com.fenoreste.atms.controller;

import java.util.ArrayList;
import java.util.List;

import com.fenoreste.atms.service.IFuncionesService;
import com.github.cliftonlabs.json_simple.JsonObject;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fenoreste.atms.modelo.TxConsultaCta;
import com.fenoreste.atms.modelo.TxDeposito;
import com.fenoreste.atms.modelo.Comprobante;
import com.fenoreste.atms.modelo.ModeloObjetoInformacionDeposito;
import com.fenoreste.atms.modelo.ObjetoFirma;
import com.fenoreste.atms.service.ServiceGeneralSpring;
import com.fenoreste.atms.util.FirmarCadenaPeticion;


@RestController
@CrossOrigin("*")
@RequestMapping("/atms/api")
@Slf4j
public class ATMSController {
	
	@Autowired
	private ServiceGeneralSpring service;
	
	@Autowired
	private FirmarCadenaPeticion firmarCadena;

	@Autowired
	private IFuncionesService saiFuncionesService;



	@RequestMapping(value = "/detalleCuenta", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> informacionCuenta(@RequestBody TxConsultaCta request) {
		log.info("Peticion detalle cuenta:"+request);
		if(saiFuncionesService.servicios_activos()){
			log.info("Servicios Activos Funcion:"+saiFuncionesService.servicios_activos());
			return new ResponseEntity<>(service.DetallesCuenta(request), HttpStatus.OK);
		}else{
			log.error("Servicios Activos Funcion:"+saiFuncionesService.servicios_activos());
			JsonObject json = new JsonObject();
			json.put("Descripcion de error","Operacion fuera de horario");
			return new ResponseEntity<>(json, HttpStatus.CONFLICT);
		}

	}
	
	@RequestMapping(value = "/deposito", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> depositoTX(@RequestBody TxDeposito request) {
		JsonObject json = new JsonObject();
		log.info("Peticion deposito ATM:"+request);
		ObjetoFirma objetoFirma = firmarCadena.objetoFirma(request);
		String firma  = firmarCadena.firma(objetoFirma);
		log.info("Firma Generada:::::"+firma);
		if(request.getFirma().equals(firma.trim())) {		
		if(saiFuncionesService.servicios_activos()){
			Comprobante comprobante = service.depositoTX(request);
			ModeloObjetoInformacionDeposito informacion = new ModeloObjetoInformacionDeposito();
			log.info("comprobante de operacion:"+comprobante);
			if(comprobante.getCodigo() == 200) {
				informacion.setResponse(comprobante);
				informacion.setEstatusProceso(0);
				informacion.setMensaje("Transaccion Exitosa");
				informacion.setNumeroAutorizacion(comprobante.getNoAutorizacion());
				log.info("Transacion exitosa");
				return new ResponseEntity<>(informacion,HttpStatus.OK);
			}else {
				informacion.setEstatusProceso(1);
				informacion.setMensaje("Error al procesar la transaccion:"+comprobante.getMensajeUsuario());
				log.error("Error al procesar la transaccion:"+comprobante.getCodigo());
				return new ResponseEntity<>(informacion,HttpStatus.CONFLICT);
			}
			//return new ResponseEntity<>(informacion,HttpStatus.OK);
		}else{
			json.put("Descripcion de error","Operacion fuera de horario");
			log.error("Operacion fuera de horario");
			return new ResponseEntity<>(json,HttpStatus.CONFLICT);
		}
		
	}else {
		log.error("..........Firmas no coinciden,favor de validar su certificado..............");
		json.put("Descripcion de error","Firmas no coinciden");
		return new ResponseEntity<>(json,HttpStatus.CONFLICT);
	}
	}

	
	
}
