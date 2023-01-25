package com.fenoreste.atms.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.fenoreste.atms.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fenoreste.atms.modelo.Comprobante;
import com.fenoreste.atms.modelo.DatosCuenta;
import com.fenoreste.atms.modelo.ModeloDatosCuenta;
import com.fenoreste.atms.modelo.ModeloObjetoInformacion;
import com.fenoreste.atms.modelo.TxConsultaCta;
import com.fenoreste.atms.modelo.TxDeposito;
import com.fenoreste.atms.modelo.opaDTO;
import com.fenoreste.atms.util.HerramientasUtil;

@Service
public class ServiceGeneralSpring {

	@Autowired
	IAuxiliarService auxiliarService;
	
	@Autowired
	IProductoService productoService;
	
	@Autowired
	ISaiFuncionesService funcionesService;
	
	@Autowired
	IProcesarMovimientoService procesarMovimientosService;

	@Autowired
	ITablasService tablasService;
	
	@Autowired
	HerramientasUtil util;
	
    public ModeloObjetoInformacion DetallesCuenta(TxConsultaCta request) {
    	ModeloObjetoInformacion informacion = new ModeloObjetoInformacion();
    	ModeloDatosCuenta modelo = new ModeloDatosCuenta();
    	try {
    		List<ModeloDatosCuenta>lista = new ArrayList<>();
			DatosCuenta DatosCuenta = new DatosCuenta();
			//Buscamos el opa
			opaDTO opa= util.opa(request.getCuenta());
			AuxiliarPK pk = new AuxiliarPK(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar());
			Auxiliar cuenta = auxiliarService.buscarPorOpa(pk);
			if(cuenta != null) {
				Tabla tbProductoDefault = tablasService.findById(new TablasPK("atm","producto_default"));
				Auxiliar productoDefault = auxiliarService.buscarPorOgsProducto(new PersonasPK(cuenta.getIdorigen(),cuenta.getIdgrupo(),cuenta.getIdsocio()),new Integer(tbProductoDefault.getDato1()));

				Producto producto = productoService.buscarPorId(cuenta.getAuxiliarPK().getIdproducto());				
				DatosCuenta.setCuenta(String.format("%06d",cuenta.getAuxiliarPK().getIdorigenp())+String.format("%05d",cuenta.getAuxiliarPK().getIdproducto())+String.format("%08d", cuenta.getAuxiliarPK().getIdauxiliar()));
				DatosCuenta.setDescripcionCuenta("******"+String.valueOf(cuenta.getAuxiliarPK().getIdauxiliar()).substring(String.valueOf(cuenta.getAuxiliarPK().getIdauxiliar()).length() - 4)+"-"+producto.getNombre());
				
				// Si el tipoproducto es igual a 2(prestamo) buscamos datos que solo estan en para el tipo de producto
				if(producto.getTipoproducto() == 2) {
				    //Corremos sai_auxiliar
					String sai_auxiliar = funcionesService.sai_auxiliar(cuenta.getAuxiliarPK().getIdorigenp(),cuenta.getAuxiliarPK().getIdproducto(),cuenta.getAuxiliarPK().getIdauxiliar());
					String[] parametros_sai = sai_auxiliar.split("\\|");
					List lista_sai = Arrays.asList(parametros_sai);
					
					//Obtenemos informacion a cerca del proximo pago
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date fecha_sai = sdf.parse(lista_sai.get(10).toString()); 
					String proximo_pago = funcionesService.sai_prestamo_cuanto(cuenta.getAuxiliarPK().getIdorigenp(),cuenta.getAuxiliarPK().getIdproducto(), cuenta.getAuxiliarPK().getIdauxiliar(),fecha_sai,cuenta.getTipoamortizacion().intValue(), sai_auxiliar);
				  	
					String[] parametros_cuanto = proximo_pago.split("\\|");
					List lista_prestamo_cuanto = Arrays.asList(parametros_cuanto);
					
					DatosCuenta.setDetalleCuenta("FechaProximoPago:"+lista_sai.get(10)+",MontoProximoPago:"+lista_prestamo_cuanto.get(7));
				}
				DatosCuenta.setDefaultCuenta(String.format("%06d",productoDefault.getAuxiliarPK().getIdorigenp())+String.format("%05d",productoDefault.getAuxiliarPK().getIdproducto())+String.format("%08d",productoDefault.getAuxiliarPK().getIdauxiliar()));
				String descripcionCuentaDefault = "";

				DatosCuenta.setDescripcionCuentaDefault("******"+String.valueOf(productoDefault.getAuxiliarPK().getIdauxiliar()).substring(String.valueOf(productoDefault.getAuxiliarPK().getIdauxiliar()).length() - 4)+"-"+"ahorro");
				DatosCuenta.setCuentaCambio("******"+String.valueOf(cuenta.getAuxiliarPK().getIdauxiliar()).substring(String.valueOf(cuenta.getAuxiliarPK().getIdauxiliar()).length() - 4)+"-"+producto.getNombre());
				DatosCuenta.setDescripcionCuentaCambio("******"+String.valueOf(cuenta.getAuxiliarPK().getIdauxiliar()).substring(String.valueOf(cuenta.getAuxiliarPK().getIdauxiliar()).length() - 4)+"-"+producto.getNombre());
				modelo.setDatosCuenta(DatosCuenta);
				lista.add(modelo);
			}				
			
			int numero = (int)Math.floor(Math.random()*(99999-10000)+10000);
			if(lista.size() > 0) {
				informacion.setEstatusProceso(0);
				informacion.setMensaje("Transaccion exitosa");
				informacion.setIdTransaccion(String.valueOf(numero));
			}else {
				informacion.setEstatusProceso(1);	
				informacion.setMensaje("Transaccion fallida");
				
			}
			informacion.setObjetoInformacion(lista);
			
			
		} catch (Exception e) {
			System.out.println("Error al llenar modelo detalles cuenta."+e.getMessage());
		}
    	return informacion;
    }
    
    
    public Comprobante depositoTX(TxDeposito objeto) {
    	Comprobante comprobante = new Comprobante();
    	try {
    		opaDTO opa= util.opa(objeto.getCuenta());
			AuxiliarPK pk = new AuxiliarPK(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar());
			Auxiliar cuenta = auxiliarService.buscarPorOpa(pk);
			
			RegistraMovimiento movimiento = new RegistraMovimiento();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
			Date parsedDate = dateFormat.parse(objeto.getFechaHora());
			Timestamp timestamp = new Timestamp(new Date().getTime());

			TablasPK tbUsuarioPk = new TablasPK("atm","usuario");
			Tabla tbUsuario = tablasService.findById(tbUsuarioPk);

			movimiento.setAuxiliaresPK(pk);
			movimiento.setCargoabono(1);
			movimiento.setFecha(timestamp);
			movimiento.setIdorigen(cuenta.getIdorigen());
			movimiento.setIdgrupo(cuenta.getIdgrupo());
			movimiento.setIdsocio(cuenta.getIdsocio());
			movimiento.setIdusuario(new Integer(tbUsuario.getDato1()));
			movimiento.setSesion(funcionesService.sesion());
			movimiento.setReferencia(objeto.getReferencia());

			
			movimiento = procesarMovimientosService.insertarMovimiento(movimiento);
			funcionesService.sai_aplica_transaccion(movimiento.getFecha(),movimiento.getIdusuario(),movimiento.getSesion(),movimiento.getReferencia());
		} catch (Exception e) {
			System.out.println("Error al realizar deposito:"+e.getMessage());
		}    	
    	return comprobante;
    }
	
	
}
