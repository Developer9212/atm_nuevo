package com.fenoreste.atms.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.fenoreste.atms.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fenoreste.atms.modelo.Columns;
import com.fenoreste.atms.modelo.Comprobante;
import com.fenoreste.atms.modelo.DatosCuenta;
import com.fenoreste.atms.modelo.ModeloDatosCuenta;
import com.fenoreste.atms.modelo.ModeloObjetoInformacionDetallesCuenta;
import com.fenoreste.atms.modelo.TxConsultaCta;
import com.fenoreste.atms.modelo.TxDeposito;
import com.fenoreste.atms.modelo.column;
import com.fenoreste.atms.modelo.opaDTO;
import com.fenoreste.atms.util.HerramientasUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServiceGeneralSpring {

	@Autowired
	private IAuxiliarService auxiliarService;
	
	@Autowired
	private IProductoService productoService;
	
	@Autowired
	private ISaiFuncionesService funcionesService;
	
	@Autowired
	private IRegistraMovimientoService registraMovimientoService;

	@Autowired
	private ITablaService tablasService;
	
	@Autowired
	private IOtrosService otrosService;
	
	@Autowired
	HerramientasUtil util;
	
	private String idTabla = "cajero_receptor";
	
    public ModeloObjetoInformacionDetallesCuenta DetallesCuenta(TxConsultaCta request) {
    	ModeloObjetoInformacionDetallesCuenta informacion = new ModeloObjetoInformacionDetallesCuenta();
    	ModeloDatosCuenta modelo = new ModeloDatosCuenta();
    	try {
    		List<ModeloDatosCuenta>lista = new ArrayList<>();
			DatosCuenta DatosCuenta = new DatosCuenta();
			//Buscamos el opa
			opaDTO opa= util.opa(request.getCuenta());
			TablaPK tbUserAtmPk = new TablaPK(idTabla,request.getIdCajero());
			Tabla tbAtm = tablasService.buscarPorId(tbUserAtmPk);
			
			if(tbAtm != null) {
				AuxiliarPK pk = new AuxiliarPK(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar());
				Auxiliar cuenta = auxiliarService.buscarPorOpa(pk);
				if(cuenta != null) {
	                Producto producto = productoService.buscarPorId(cuenta.getAuxiliarPK().getIdproducto());				
					DatosCuenta.setCuenta(String.format("%06d",cuenta.getAuxiliarPK().getIdorigenp())+String.format("%05d",cuenta.getAuxiliarPK().getIdproducto())+String.format("%08d", cuenta.getAuxiliarPK().getIdauxiliar()));
					
					String cadena = cuenta.getAuxiliarPK().getIdauxiliar().toString();
					log.info("Cadena original:"+cadena);
					cadena = obtenerUltimosCuatroDigitos(cadena);
					log.info("Cadena formateada:"+cadena);
					DatosCuenta.setDescripcionCuenta("******"+cadena);
					
					if(producto.getTipoproducto() == 0 || producto.getTipoproducto() == 2) {
						log.info("Entro 1");
						if(producto.getTipoproducto() == request.getTipoCuenta()) {
							// Si el tipoproducto es igual a 2(prestamo) buscamos datos que solo estan en para el tipo de producto
							if(producto.getTipoproducto() == 2) {	
							    //Corremos sai_auxiliar
								String sai_auxiliar = funcionesService.sai_auxiliar(cuenta.getAuxiliarPK().getIdorigenp(),cuenta.getAuxiliarPK().getIdproducto(),cuenta.getAuxiliarPK().getIdauxiliar());
								String[] parametros_sai = sai_auxiliar.split("\\|");
								List lista_sai = Arrays.asList(parametros_sai);						
								//Obtenemos informacion a cerca del proximo pago
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
								Date fecha_sai = sdf.parse(lista_sai.get(10).toString()); 
								log.info("Sai auxiliar:"+sai_auxiliar);
								log.info("datos prestamo cuento:"+cuenta.getAuxiliarPK().getIdorigenp()+","+cuenta.getAuxiliarPK().getIdproducto()
										+","+cuenta.getAuxiliarPK().getIdauxiliar()+","+fecha_sai+",tipo amortizacion:"+cuenta.getTipoamortizacion().intValue()+","+ sai_auxiliar);
								String proximo_pago = funcionesService.sai_prestamo_cuanto(cuenta.getAuxiliarPK().getIdorigenp(),cuenta.getAuxiliarPK().getIdproducto(), cuenta.getAuxiliarPK().getIdauxiliar(),fecha_sai,cuenta.getTipoamortizacion().intValue(), sai_auxiliar);
							  	
								String[] parametros_cuanto = proximo_pago.split("\\|");
								List<String> lista_prestamo_cuanto = Arrays.asList(parametros_cuanto);
								
								DatosCuenta.setDetalleCuenta("FechaProximoPago:"+lista_sai.get(10)+",MontoProximoPago:"+lista_prestamo_cuanto.get(7));
							}
							int producto_default = 0;
							Tabla tbProductoDefault = tablasService.buscarPorId(new TablaPK("cajero_receptor","producto_default"));
							if(tbProductoDefault != null) {
								if(tbProductoDefault.getDato1()!= null || !tbProductoDefault.getDato1().equals("")) {
									producto_default = Integer.parseInt(tbProductoDefault.getDato1());
									
								}else if(tbProductoDefault.getDato2()!= null || !tbProductoDefault.getDato2().equals("")) {
									producto_default = Integer.parseInt(tbProductoDefault.getDato2());
									
								}else {
									informacion.setCodigo(409);
									informacion.setMensaje("No existe configuracion para producto default");
									log.info("...........Error al obtener configuracion producto default..........");
								}
							}
							System.out.println("Pasoooooooooo");
							Auxiliar productoDefault = auxiliarService.buscarPorOgsProducto(new PersonasPK(cuenta.getIdorigen(),cuenta.getIdgrupo(),cuenta.getIdsocio()),producto_default);
			                 System.out.println("Pasooooooooooooooooooo1:"+productoDefault);
							DatosCuenta.setDefaultCuenta(String.format("%06d",productoDefault.getAuxiliarPK().getIdorigenp())+String.format("%05d",productoDefault.getAuxiliarPK().getIdproducto())+String.format("%08d",productoDefault.getAuxiliarPK().getIdauxiliar()));
							System.out.println("Pasooooooooooooooooooo2:"+producto_default);
							DatosCuenta.setDescripcionCuentaDefault("******"+String.valueOf(productoDefault.getAuxiliarPK().getIdauxiliar())+"-"+"ahorro");
							System.out.println("Pasooooooooooooooooooo3:"+producto_default);
							DatosCuenta.setCuentaCambio("******"+cadena+"-"+producto.getNombre());
							System.out.println("Pasooooooooooooooooooo:"+producto_default);
							DatosCuenta.setDescripcionCuentaCambio("******"+cadena+"-"+producto.getNombre());
							modelo.setDatosCuenta(DatosCuenta);
							lista.add(modelo);
							
							int numero = (int)Math.floor(Math.random()*(99999-10000)+10000);
							if(lista.size() > 0) {
								informacion.setEstatusProceso(0);
								informacion.setMensaje("Transaccion exitosa");
								informacion.setIdTransaccion(String.valueOf(numero));
							}else {
								informacion.setEstatusProceso(1);	
								informacion.setMensaje("Transaccion fallida");
								informacion.setCodigo(409);				
							}			
							informacion.setObjetoInformacion(lista);
						}else {
							informacion.setCodigo(409);
							informacion.setMensaje("Tipo de cuenta no identificada para producto:"+request.getCuenta());
							log.info(".............Tipo de cuenta no identificada para producto:"+request.getCuenta());
						}
					}else {
						informacion.setCodigo(409);
						informacion.setMensaje("Tipo de cuenta no es operable");
						log.info(".............Tipo de cuenta no es operable............");
					}
				}else {
					informacion.setCodigo(409);
					informacion.setMensaje("Cuenta no existe");
					log.info(".............Cuenta no existe en la base de datos............");
				}
			}else {
				informacion.setCodigo(409);
				informacion.setMensaje("Cajero no identificado");
				log.info(".............Cajero no identificado............");
			}
			
			
			
			
		} catch (Exception e) {
			informacion.setCodigo(500);
			System.out.println("Error al llenar modelo detalles cuenta:"+e.getMessage());
		}
    	return informacion;
    }
    
    
    public Comprobante depositoTX(TxDeposito objeto) {
    	Comprobante comprobante = new Comprobante();
    	
    	List<Columns> listaCol = new ArrayList<>();
    	List<column>listaColumns = new ArrayList<>();
    	
    	Columns columns = new Columns();
    	column col = new column();
    	col.setAligment("L");
		col.setLen(8);
		col.setText("Deposito en cajero");
		
		
		listaColumns.add(col);
		columns.setColumns(listaColumns);
		columns.setColumnsLen(8);
		listaCol.add(columns);
		
    	comprobante.setCodigo(500);   	
    	
    	boolean bandera_cambio = false;
    	Producto producto = null;
    	try {
    		opaDTO opa= util.opa(objeto.getCuenta());
    		log.info(""+opa.getIdorigenp()+","+opa.getIdproducto()+","+opa.getIdauxiliar());
    		AuxiliarPK auxPk = null;
			Auxiliar cuenta_depositar = null;
    		TablaPK tbPk = null;
    		
    		TablaPK tbUserAtmPk = new TablaPK(idTabla,objeto.getIdCajero());
			Tabla tbAtm = tablasService.buscarPorId(tbUserAtmPk);
			
			if(tbAtm != null) {
				//operaciones cambio
	    		if(objeto.getTipoTX() == 0 || objeto.getTipoTX() == 1) {
					auxPk = new AuxiliarPK();
	           	 	auxPk.setIdorigenp(opa.getIdorigenp());
	           	 	auxPk.setIdproducto(opa.getIdproducto());
	           	 	auxPk.setIdauxiliar(opa.getIdauxiliar());
	           	 	producto = productoService.buscarPorId(auxPk.getIdproducto());
	           	 	if (producto != null) {
	           	 	    if(objeto.getTipoTX() == 0 && producto.getTipoproducto() == 0) {
	           	 	       cuenta_depositar = auxiliarService.buscarPorOpa(auxPk);	
	           	 	    } else if(objeto.getTipoTX() == 1 && producto.getTipoproducto() == 2) {
	           	 	       cuenta_depositar = auxiliarService.buscarPorOpa(auxPk);	
	           	 	    }else {
	           	 	    	comprobante.setMensajeUsuario("Cuenta no admite el tipo de operacion solicitada");
	           	 	    }
					}
				}else if(objeto.getTipoTX() == 3 || objeto.getTipoTX() == 4) {
					bandera_cambio= true;
					switch(objeto.getTipoTX()) {
					case 3:
		                 tbPk =new TablaPK(idTabla,"producto_default");	
		                 Tabla tb_producto_default = tablasService.buscarPorId(tbPk);
		                 if(tb_producto_default != null) {
		                	 int producto_default = 0;
		                	 boolean bandera_producto_default = false;
		                	 if(tb_producto_default.getDato1() != null || !tb_producto_default.getDato2().equals("")) {
		                		bandera_producto_default = true;
		                		producto_default = Integer.parseInt(tb_producto_default.getDato1());
		                	 }else if(tb_producto_default.getDato2() != null || !tb_producto_default.getDato2().equals("")) {
		                		bandera_producto_default = true; 
		                		producto_default = Integer.parseInt(tb_producto_default.getDato2());
		                	 }
		                	 
		                	 if(bandera_producto_default) {
		                		 if(opa.getIdproducto() == producto_default) {
				                	 auxPk = new AuxiliarPK();
				                	 auxPk.setIdorigenp(opa.getIdorigenp());
				                	 auxPk.setIdproducto(opa.getIdproducto());
				                	 auxPk.setIdauxiliar(opa.getIdauxiliar());
				                	 cuenta_depositar = auxiliarService.buscarPorOpa(auxPk);
				                 }else {
				                	 comprobante.setCodigo(409);
				                	 comprobante.setMensajeUsuario("Producto no configurado para cambio");
				                	 log.info("............Producto no configurado para cambio.........");
				                 } 
		                	 }
		                	 
		                 }else {
		                	 comprobante.setCodigo(409);
		                	 comprobante.setMensajeUsuario("Producto no configurado default para cambio");
		                	 log.info(".........No existe configuracion para producto default cambio...........");
		                 }
						break;
					case 4:
						tbPk =new TablaPK(idTabla,"producto_cambio");	
		                Tabla tb_producto_cambio = tablasService.buscarPorId(tbPk);
		                 if(opa.getIdproducto() == Integer.parseInt(tb_producto_cambio.getDato1())) {
		                	 auxPk = new AuxiliarPK();
		                	 auxPk.setIdorigenp(opa.getIdorigenp());
		                	 auxPk.setIdproducto(opa.getIdproducto());
		                	 auxPk.setIdauxiliar(opa.getIdauxiliar());
		                	 cuenta_depositar = auxiliarService.buscarPorOpa(auxPk);
		                 }else {
		                	 comprobante.setCodigo(409);
		                	 comprobante.setMensajeUsuario("Producto no configurado para cambio EF");
		                	 log.info("............Producto no configurado para cambio EF.........");
		                 }
						break;
					}
				}
				
				if(cuenta_depositar != null) {
					producto = productoService.buscarPorId(cuenta_depositar.getAuxiliarPK().getIdproducto());
					if(cuenta_depositar.getEstatus() == 2) {
						Timestamp timestamp = new Timestamp(new Date().getTime());
						String sesion = otrosService.sesion();	
						log.info("Usuario encontradooooooooooooooooooooooo:"+tbAtm.getDato1());
						if(tbAtm != null) {
							RegistraMovimiento _movimiento = new RegistraMovimiento();
							//llenaremos pk de movimiento(Abono)
							MovimientoPK _pkMovimiento = new MovimientoPK();
							_pkMovimiento.setSesion(sesion);
							_pkMovimiento.setIdusuario(Integer.parseInt(tbAtm.getDato1()));
							_pkMovimiento.setReferencia(objeto.getReferencia());
							
							_movimiento.setPk(_pkMovimiento);
							_movimiento.setIdorigenp(cuenta_depositar.getAuxiliarPK().getIdorigenp());
							_movimiento.setIdproducto(cuenta_depositar.getAuxiliarPK().getIdproducto());
							_movimiento.setIdauxiliar(cuenta_depositar.getAuxiliarPK().getIdauxiliar());
							_movimiento.setFecha(timestamp);
							if(bandera_cambio && objeto.getTipoTX() == 4) {
								//Aqui buscamos la cuenta concentradora (Retiro en sucursal)
								_movimiento.setIdcuenta(null);
							}
							_movimiento.setIdcuenta(null);
							_movimiento.setIdorigen(cuenta_depositar.getIdorigen());
							_movimiento.setIdgrupo(cuenta_depositar.getIdgrupo());
							_movimiento.setIdsocio(cuenta_depositar.getIdsocio());
							_movimiento.setCargoabono(1);
							_movimiento.setMonto(objeto.getMontoIngresado() - objeto.getMontoCambio());
			                _movimiento.setSai_aux("");
			                _movimiento.setTipo_amort(cuenta_depositar.getTipoamortizacion().intValue());
			                                
			                RegistraMovimiento mov_registrados = registraMovimientoService.insertarMovimiento(_movimiento);	                
			                String tot_movs = funcionesService.sai_aplica_transaccion(_movimiento.getPk().getIdusuario(),_movimiento.getPk().getSesion(),objeto.getReferencia());
			                log.info(".............................."+tot_movs);
			                if(Integer.parseInt(tot_movs) > 0) {
			                	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			                	SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss.S");
			                	String fecha=sdf.format(new Date());
			                	String hora = sdf1.format(new Date());
			                	funcionesService.sai_termina_transaccion(new Date(),_movimiento.getPk().getIdusuario() ,sesion, objeto.getReferencia());
			                	comprobante.setCodigo(200);		                	
			                	comprobante.setDescripcionCuenta(producto.getNombre());
			                	if(!bandera_cambio) {
			                		comprobante.setMonto(objeto.getMontoDeposito());
			                		comprobante.setMontoNoDepositado(0.0);
			                	}
			                	comprobante.setFechaHora(fecha+"T"+hora+"Z");
			                	comprobante.setDetalleComprobante(listaCol);
			                	
			                }else {
			                	comprobante.setCodigo(409);
			                	comprobante.setMensajeUsuario("Error al procesar movimientos,contacte a proveedor de servicios");
			                	log.info("..........Falla al procesar movimientos en SAICoop...........");
			                }
			                
			          	}else {
			          	   comprobante.setCodigo(409);
			          	   comprobante.setMensajeUsuario("Cajero no identificado");
			          	   log.info(".............Cajero:"+objeto.getIdCajero()+" no identificado..............");
			          	}
					}else {
					  comprobante.setCodigo(409);
					  comprobante.setMensajeUsuario("Cuenta Inactiva");
					  log.info("............Verifique estatus de cuenta:"+cuenta_depositar.getEstatus());
					}
				}else {
					if(comprobante.getMensajeUsuario() == null) {
						comprobante.setMensajeUsuario("Cuenta a depositar no existe");
						log.info("..................Cuenta a depositar no existe.................");
					}
				}				
			}else {
				comprobante.setCodigo(409);
				comprobante.setMensajeUsuario("Cajero no identificado");
				log.info("......Cajero no identificado.......");
			}
    		

			
			
			//funcionesService.sai_aplica_transaccion(movimiento.getFecha(),movimiento.getIdusuario(),movimiento.getSesion(),movimiento.getReferencia());
		} catch (Exception e) {
			System.out.println("Error al realizar deposito:"+e.getMessage());
		}    	
    	return comprobante;
    }
    
    
    
    private static String obtenerUltimosCuatroDigitos(String cadena) {
        // Verifica si la cadena tiene menos de 4 dígitos
        int longitud = cadena.length();
        if (longitud < 4) {
            // Completa con ceros a la izquierda
            int cerosNecesarios = 4 - longitud;
            StringBuilder cadenaConCeros = new StringBuilder();
            for (int i = 0; i < cerosNecesarios; i++) {
                cadenaConCeros.append('0');
            }
            return cadenaConCeros.append(cadena).toString();
        } else {
            // Toma los últimos 4 dígitos
            return cadena.substring(longitud - 4);
        }
    }
	
	
}
