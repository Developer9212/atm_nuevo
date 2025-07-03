package com.fenoreste.atms.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
    private IFuncionesService funcionesService;

    @Autowired
    private IRegistraMovimientoService registraMovimientoService;

    @Autowired
    private ITablaService tablasService;

    @Autowired
    private IOtrosService otrosService;

    @Autowired
    private IOrigenService origenService;

    @Autowired
    private HerramientasUtil util;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IAuxiliarDService auxiliarDService;

    @Autowired
    private IPersonaService personaService;

    @Autowired
    private ISoparService soparService;


    private String idTabla = "cajero_receptor";

    public ModeloObjetoInformacionDetallesCuenta DetallesCuenta(TxConsultaCta request) {
        ModeloObjetoInformacionDetallesCuenta informacion = new ModeloObjetoInformacionDetallesCuenta();
        ModeloDatosCuenta modelo = new ModeloDatosCuenta();
        try {
            List<ModeloDatosCuenta> lista = new ArrayList<>();
            DatosCuenta DatosCuenta = new DatosCuenta();
            //Buscamos el opa
            opaDTO opa = util.opa(request.getCuenta());
            TablaPK tbPk = new TablaPK(idTabla, request.getIdCajero());
            Tabla tabla = tablasService.buscarPorId(tbPk);

            if (tabla != null) {
                AuxiliarPK pk = new AuxiliarPK(opa.getIdorigenp(), opa.getIdproducto(), opa.getIdauxiliar());
                Auxiliar cuenta = auxiliarService.buscarPorOpa(pk);
                if (cuenta != null) {
                    BigDecimal bd = null;
                    Producto producto = productoService.buscarPorId(cuenta.getAuxiliarPK().getIdproducto());
                    DatosCuenta.setCuenta(String.format("%06d", cuenta.getAuxiliarPK().getIdorigenp()) + String.format("%05d", cuenta.getAuxiliarPK().getIdproducto()) + String.format("%08d", cuenta.getAuxiliarPK().getIdauxiliar()));
                    String cadena = cuenta.getAuxiliarPK().getIdauxiliar().toString();
                    cadena = obtenerUltimosCuatroDigitos(cadena);

                    PersonaPK personaPK = new PersonaPK(cuenta.getIdorigen(), cuenta.getIdgrupo(), cuenta.getIdsocio());
                    Persona persona = personaService.getPersona(personaPK);

                    boolean soparBandera = true;

                    //Validaciones para sopar entro el 01/05/2025 CSN
                    if (origenService.origenMatriz().getIdorigen() == 30200) {
                        tbPk = new TablaPK(idTabla, "lista_negra");
                        tabla = tablasService.buscarPorId(tbPk);

                        SoparPk soparPk = new SoparPk();
                        soparPk.setIdorigen(persona.getPersonasPK().getIdorigen());
                        soparPk.setIdgrupo(persona.getPersonasPK().getIdgrupo());
                        soparPk.setIdsocio(persona.getPersonasPK().getIdsocio());
                        soparPk.setTipo(tabla.getDato2());
                        Sopar sopar = soparService.bloqueoListaNegra(soparPk);
                        if (sopar != null) {
                            soparBandera = false;
                        }
                    } else {
                        soparBandera = false;
                    }


                    if (!soparBandera) {
                        DatosCuenta.setDescripcionCuenta("******" + cadena + " - \nBeneficiario:\n" + formateaCadena(persona.getNombre() + " " + persona.getAppaterno() + " " + persona.getApmaterno()));
                        DatosCuenta.setMaxDeposito(new BigDecimal(0.00));
                        DatosCuenta.setMinDeposito(new BigDecimal(0.00));
                        if (producto.getTipoproducto() == 0 || producto.getTipoproducto() == 2) {
                            //Min y Max depositos
                            TablaPK tbParamPK = new TablaPK(idTabla, "maximo_operacion");
                            Tabla tbParam = tablasService.buscarPorId(tbParamPK);

                            bd = new BigDecimal(new Double(tbParam.getDato1()));
                            bd = bd.setScale(2, RoundingMode.HALF_UP);
                            BigDecimal bdmo = bd;

                            tbParamPK = new TablaPK(idTabla, "minimo_operacion");
                            tbParam = tablasService.buscarPorId(tbParamPK);

                            bd = new BigDecimal(new Double(tbParam.getDato1()));
                            bd = bd.setScale(2, RoundingMode.HALF_UP);
                            BigDecimal bdmio = bd;


                            /*Solo para los ahorros*/
                            DatosCuenta.setMaxDeposito(bdmo);//Double.parseDouble(lista_prestamo_cuanto.get(0)));
                            DatosCuenta.setMinDeposito(bdmio);//Double.parseDouble(lista_prestamo_cuanto.get(7)));


                            boolean bandera = false;
                            if (request.getTipoCuenta() == 1 || request.getTipoCuenta() == 0) {/**************Solo Abono a credito o ahorro**************/
                                // Si el tipoproducto es igual a 2(prestamo) buscamos datos que solo estan en para el tipo de producto
                                if (producto.getTipoproducto() == 2 && request.getTipoCuenta() == 1) {
                                    bandera = true;
                                } else if (producto.getTipoproducto() == 0 && request.getTipoCuenta() == 0) {
                                    bandera = true;
                                }

                                if (bandera) {
                                    if (producto.getTipoproducto() == 2 && request.getTipoCuenta() == 1) {
                                        //Corremos sai_auxiliar
                                        String sai_auxiliar = funcionesService.sai_auxiliar(cuenta.getAuxiliarPK().getIdorigenp(), cuenta.getAuxiliarPK().getIdproducto(), cuenta.getAuxiliarPK().getIdauxiliar());
                                        String[] parametros_sai = sai_auxiliar.split("\\|");
                                        List lista_sai = Arrays.asList(parametros_sai);
                                        //Obtenemos informacion a cerca del proximo pago
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                        Date fecha_sai = sdf.parse(lista_sai.get(10).toString());
                                        log.info("datos prestamo cuanto:" + cuenta.getAuxiliarPK().getIdorigenp() + "," + cuenta.getAuxiliarPK().getIdproducto()
                                                + "," + cuenta.getAuxiliarPK().getIdauxiliar() + "," + fecha_sai + ",tipo amortizacion:" + cuenta.getTipoamortizacion().intValue() + "," + sai_auxiliar);
                                        String proximo_pago = funcionesService.sai_prestamo_cuanto(cuenta.getAuxiliarPK().getIdorigenp(), cuenta.getAuxiliarPK().getIdproducto(), cuenta.getAuxiliarPK().getIdauxiliar(), fecha_sai, cuenta.getTipoamortizacion().intValue(), sai_auxiliar);

                                        log.info("Tu proximo pago es:" + proximo_pago);

                                        String[] parametros_cuanto = proximo_pago.split("\\|");
                                        List<String> lista_prestamo_cuanto = Arrays.asList(parametros_cuanto);
                                        DatosCuenta.setDetalleCuenta("FechaProximoPago:" + lista_sai.get(10) + ",MontoProximoPago:" + lista_prestamo_cuanto.get(0));
                                        Double monto_liquidacion = funcionesService.monto_liquidar_prestamo(pk);
                                        log.info("Monto liquidacion:" + monto_liquidacion);
                                        bd = new BigDecimal(new Double(lista_prestamo_cuanto.get(0)));
                                        bd = bd.setScale(2, RoundingMode.HALF_UP);
                                        BigDecimal bdmpg = bd;

                                        DatosCuenta.setMontoProximoPago(bdmpg);

                                        bd = new BigDecimal(monto_liquidacion);//ew Double(lista_prestamo_cuanto.get(0)));
                                        bd = bd.setScale(2, RoundingMode.HALF_UP);
                                        bdmpg = bd;
                                        DatosCuenta.setMaxDeposito(bdmpg);

                                        bd = new BigDecimal(new Double(lista_prestamo_cuanto.get(7)));
                                        bd = bd.setScale(2, RoundingMode.HALF_UP);
                                        BigDecimal bdmip = bd;
                                        DatosCuenta.setMinDeposito(bdmip);

                                    } else if (origenService.origenMatriz().getIdorigen() == 30200) {
                                        //Validacion para CSN
                                        tbParamPK = new TablaPK(idTabla, "ahorro_patrimonial");
                                        tbParam = tablasService.buscarPorId(tbParamPK);
                                        if (cuenta.getAuxiliarPK().getIdproducto() == Integer.parseInt(tbParam.getDato1())) {
                                            DatosCuenta.setDetalleCuenta(tbParam.getDato2());
                                        }
                                    }


                                    int producto_default = 0;
                                    int producto_defaultOp = 0;
                                    Tabla tbProductoDefault = tablasService.buscarPorId(new TablaPK("cajero_receptor", "producto_default"));
                                    if (tbProductoDefault != null) {
                                        if (tbProductoDefault.getDato1() != null || !tbProductoDefault.getDato1().equals("")) {
                                            producto_default = Integer.parseInt(tbProductoDefault.getDato1());

                                        }
                                        if (tbProductoDefault.getDato2() != null || !tbProductoDefault.getDato2().equals("")) {
                                            producto_defaultOp = Integer.parseInt(tbProductoDefault.getDato2());

                                        }
                                    }

                                    Auxiliar productoDefault = auxiliarService.buscarPorOgsProducto(new PersonaPK(cuenta.getIdorigen(), cuenta.getIdgrupo(), cuenta.getIdsocio()), producto_default);
                                    if (productoDefault == null) {
                                        productoDefault = auxiliarService.buscarPorOgsProducto(new PersonaPK(cuenta.getIdorigen(), cuenta.getIdgrupo(), cuenta.getIdsocio()), producto_defaultOp);
                                    }
                                    DatosCuenta.setDefaultCuenta(String.format("%06d", productoDefault.getAuxiliarPK().getIdorigenp()) + String.format("%05d", productoDefault.getAuxiliarPK().getIdproducto()) + String.format("%08d", productoDefault.getAuxiliarPK().getIdauxiliar()));
                                    Producto defaultP = productoService.buscarPorId(producto_default);
                                    DatosCuenta.setDescripcionCuentaDefault("******" + String.valueOf(productoDefault.getAuxiliarPK().getIdauxiliar()) + "-" + defaultP.getNombre() + "\nBeneficiario:\n" + formateaCadena(persona.getNombre() + " " + persona.getAppaterno() + " " + persona.getApmaterno()));
                                    DatosCuenta.setCuentaCambio("******" + cadena + "-" + producto.getNombre());
                                    DatosCuenta.setDescripcionCuentaCambio("******" + cadena + "-" + producto.getNombre());


                                    tbParamPK = new TablaPK(idTabla, "maximo_cambio");
                                    tbParam = tablasService.buscarPorId(tbParamPK);

                                    bd = new BigDecimal(tbParam.getDato1());
                                    bd = bd.setScale(2, RoundingMode.HALF_UP);

                                    DatosCuenta.setMaxCambio(bd);//df.format(new Double(tbParam.getDato1())));

                                    modelo.setDatosCuenta(DatosCuenta);
                                    lista.add(modelo);

                                    int numero = (int) Math.floor(Math.random() * (99999 - 10000) + 10000);
                                    if (lista.size() > 0) {
                                        informacion.setEstatusProceso(0);
                                        informacion.setMensaje("Transaccion exitosa");
                                        informacion.setIdTransaccion(String.valueOf(numero));
                                    } else {
                                        informacion.setEstatusProceso(1);
                                        informacion.setMensaje("Transaccion fallida");
                                        informacion.setCodigo(409);
                                    }
                                    informacion.setObjetoInformacion(lista);
                                } else {
                                    informacion.setCodigo(409);
                                    informacion.setMensaje("Tipo de cuenta no identificada para producto:" + request.getCuenta());
                                    log.error(".............Tipo de cuenta no identificada para producto:" + request.getCuenta());
                                }

                            } else {
                                informacion.setCodigo(409);
                                informacion.setMensaje("Tipo de cuenta no operable:" + request.getCuenta());
                                log.error(".............Tipo de cuenta no operable:" + request.getCuenta());
                            }
                        } else {
                            informacion.setCodigo(409);
                            informacion.setMensaje("Tipo de cuenta no es operable");
                            log.error(".............Tipo de cuenta no es operable............");
                        }
                    } else {
                        informacion.setCodigo(409);
                        informacion.setMensaje("Socio esta bloqueado");
                        log.error(".............socio esta bloqueado............");
                    }
                } else {
                    informacion.setCodigo(409);
                    informacion.setMensaje("Cuenta no existe");
                    log.error(".............Cuenta no existe en la base de datos............");
                }
            } else {
                informacion.setCodigo(409);
                informacion.setMensaje("Cajero no identificado");
                log.error(".............Cajero no identificado............");
            }


        } catch (Exception e) {
            informacion.setCodigo(500);
            log.error("Error al llenar modelo detalles cuenta:" + e.getMessage());
        }
        return informacion;
    }

    public Comprobante depositoTX(TxDeposito objeto) {
        Comprobante comprobante = new Comprobante();

        List<Columns> listaCol = new ArrayList<>();
        List<column> listaColumns = new ArrayList<>();

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

        Producto producto = null;
        try {
            opaDTO opa = util.opa(objeto.getCuenta());
            log.info(":" + opa.getIdorigenp() + "," + opa.getIdproducto() + "," + opa.getIdauxiliar());
            AuxiliarPK auxPk = null;
            Auxiliar cuenta_depositar = null;
            TablaPK tbPk = null;
            Tabla tabla = null;

            boolean soparBandera = false;

            //Validaciones para sopar entro el 01/05/2025 CSN
            if (origenService.origenMatriz().getIdorigen() == 30200) {
                auxPk = new AuxiliarPK(opa.getIdorigenp(), opa.getIdproducto(), opa.getIdauxiliar());
                cuenta_depositar = auxiliarService.buscarPorOpa(auxPk);
                PersonaPK personaPK = new PersonaPK(cuenta_depositar.getIdorigen(), cuenta_depositar.getIdgrupo(), cuenta_depositar.getIdsocio());
                Persona persona = personaService.getPersona(personaPK);

                tbPk = new TablaPK(idTabla, "lista_negra");
                tabla = tablasService.buscarPorId(tbPk);

                SoparPk soparPk = new SoparPk();
                soparPk.setIdorigen(persona.getPersonasPK().getIdorigen());
                soparPk.setIdgrupo(persona.getPersonasPK().getIdgrupo());
                soparPk.setIdsocio(persona.getPersonasPK().getIdsocio());
                soparPk.setTipo(tabla.getDato2());
                Sopar sopar = soparService.bloqueoListaNegra(soparPk);
                if (sopar != null) {
                    soparBandera = true;
                }
            }else{
                soparBandera = false;
            }


            if (!soparBandera) {
                tbPk = new TablaPK(idTabla, objeto.getIdCajero());
                tabla = tablasService.buscarPorId(tbPk);
                if (tabla != null) {
                    Integer idUsuario = Integer.parseInt(tabla.getDato1());
                    Usuario usuario = usuarioService.buscarPorID(idUsuario);
                    if (usuario != null) {
                        if (objeto.getTipoTX() == 0 || objeto.getTipoTX() == 1) {
                            auxPk = new AuxiliarPK();
                            auxPk.setIdorigenp(opa.getIdorigenp());
                            auxPk.setIdproducto(opa.getIdproducto());
                            auxPk.setIdauxiliar(opa.getIdauxiliar());
                            producto = productoService.buscarPorId(auxPk.getIdproducto());
                            if (producto != null) {
                                if (objeto.getTipoTX() == 0 && producto.getTipoproducto() == 0) {
                                    cuenta_depositar = auxiliarService.buscarPorOpa(auxPk);
                                } else if (objeto.getTipoTX() == 1 && producto.getTipoproducto() == 2) {
                                    cuenta_depositar = auxiliarService.buscarPorOpa(auxPk);
                                } else {
                                    comprobante.setMensajeUsuario("Cuenta no admite el tipo de operacion solicitada");
                                }
                            }
                        } else if (objeto.getTipoTX() == 3 || objeto.getTipoTX() == 4) {
                            switch (objeto.getTipoTX()) {
                                case 3:
                                    tbPk = new TablaPK(idTabla, "producto_default");
                                    Tabla tb_producto_default = tablasService.buscarPorId(tbPk);
                                    if (tb_producto_default != null) {
                                        int producto_default = 0;
                                        int producto_default1 = 0;
                                        boolean bandera_producto_default = false;
                                        if (tb_producto_default.getDato1() != null || !tb_producto_default.getDato1().equals("")) {
                                            producto_default = Integer.parseInt(tb_producto_default.getDato1());
                                            bandera_producto_default = true;

                                        } else if (tb_producto_default.getDato2() != null || !tb_producto_default.getDato2().equals("")) {
                                            bandera_producto_default = true;
                                            producto_default1 = Integer.parseInt(tb_producto_default.getDato2());
                                        }

                                        if (bandera_producto_default) {
                                            if (opa.getIdproducto() == producto_default  || opa.getIdproducto() == producto_default1) {
                                                auxPk = new AuxiliarPK();
                                                auxPk.setIdorigenp(opa.getIdorigenp());
                                                auxPk.setIdproducto(opa.getIdproducto());
                                                auxPk.setIdauxiliar(opa.getIdauxiliar());
                                                cuenta_depositar = auxiliarService.buscarPorOpa(auxPk);
                                                producto = productoService.buscarPorId(cuenta_depositar.getAuxiliarPK().getIdproducto());

                                            } else {
                                                comprobante.setCodigo(409);
                                                comprobante.setMensajeUsuario("Producto no configurado para cambio");
                                                log.warn("............Producto no configurado para cambio.........");
                                            }
                                        }

                                    } else {
                                        comprobante.setCodigo(409);
                                        comprobante.setMensajeUsuario("Producto no configurado default para cambio");
                                        log.warn(".........No existe configuracion para producto default cambio...........");
                                    }
                                    break;
                                case 4:
                                    tbPk = new TablaPK(idTabla, "producto_cambio");
                                    Tabla tb_producto_cambio = tablasService.buscarPorId(tbPk);
                                    if (opa.getIdproducto() == Integer.parseInt(tb_producto_cambio.getDato1())) {
                                        auxPk = new AuxiliarPK();
                                        auxPk.setIdorigenp(opa.getIdorigenp());
                                        auxPk.setIdproducto(opa.getIdproducto());
                                        auxPk.setIdauxiliar(opa.getIdauxiliar());
                                        cuenta_depositar = auxiliarService.buscarPorOpa(auxPk);
                                    } else {
                                        comprobante.setCodigo(409);
                                        comprobante.setMensajeUsuario("Producto no configurado para cambio EF");
                                        log.warn("............Producto no configurado para cambio EF.........");
                                    }
                                    break;
                            }
                        }

                        if (cuenta_depositar != null) {
                            //Una ves validada la cuenta a la que se intenta abonar vamos a validar reglas de negocio

                            String[] validacion = new String[2];
                            if (origenService.origenMatriz().getIdorigen() == 30200) {
                                validacion = validarReglasCsn(cuenta_depositar, objeto.getMontoDeposito(), objeto.getMontoCambio(), idUsuario);
                            } else if (origenService.origenMatriz().getIdorigen() == 20700) {
                                validacion = validarReglasSagrada(cuenta_depositar, objeto.getMontoDeposito(), objeto.getMontoCambio(), idUsuario);
                            } else {
                                validacion[0] = "OK";
                                validacion[1] = "200";
                            }

                            log.info("Validacion regreso:" + validacion[1]);
                            if (validacion[1] == "200") {
                                if (cuenta_depositar.getEstatus() == 2) {
                                    Timestamp timestamp = new Timestamp(new Date().getTime());
                                    String sesion = otrosService.sesion();
                                    RegistraMovimiento _movimiento = new RegistraMovimiento();
                                    //llenaremos pk de movimiento(Abono)
                                    MovimientoPK _pkMovimiento = new MovimientoPK();
                                    _pkMovimiento.setSesion(sesion);
                                    _pkMovimiento.setIdusuario(idUsuario);//Integer.parseInt(tbAtm.getDato1()));
                                    _pkMovimiento.setReferencia(objeto.getReferencia());
                                    _pkMovimiento.setN_mov(1);

                                    _movimiento.setPk(_pkMovimiento);
                                    _movimiento.setIdorigenp(cuenta_depositar.getAuxiliarPK().getIdorigenp());
                                    _movimiento.setIdproducto(cuenta_depositar.getAuxiliarPK().getIdproducto());
                                    _movimiento.setIdauxiliar(cuenta_depositar.getAuxiliarPK().getIdauxiliar());
                                    _movimiento.setFecha(timestamp);
								/*if(bandera_cambio && objeto.getTipoTX() == 4) {
									//Aqui buscamos la cuenta concentradora (Retiro en sucursal)
									_movimiento.setIdcuenta(null);
								}*/
                                    _movimiento.setIdcuenta(null);
                                    _movimiento.setIdorigen(cuenta_depositar.getIdorigen());
                                    _movimiento.setIdgrupo(cuenta_depositar.getIdgrupo());
                                    _movimiento.setIdsocio(cuenta_depositar.getIdsocio());
                                    _movimiento.setCargoabono(1);
                                    _movimiento.setMonto(objeto.getMontoDeposito());
                                    _movimiento.setSai_aux("");
                                    _movimiento.setTipo_amort(cuenta_depositar.getTipoamortizacion().intValue());

                                    RegistraMovimiento mov_registrados = registraMovimientoService.insertarMovimiento(_movimiento);
                                    String tot_movs = funcionesService.sai_aplica_transaccion(_movimiento.getPk().getIdusuario(), _movimiento.getPk().getSesion(), objeto.getReferencia());
                                    log.info("Movimiento op normal......................" + tot_movs);
                                    if (Integer.parseInt(tot_movs) > 0) {

                                        //Buscamos la tabla configurada con la cuenta para comision
                                        TablaPK tbPkComision = new TablaPK(idTabla, "comision");
                                        Tabla tbComision = tablasService.buscarPorId(tbPkComision);

                                        if (tbComision.getDato4().equals("1")) {
                                            //Si la poliza normal se genero vamos a cobrar la comision
                                            _movimiento = new RegistraMovimiento();
                                            //llenaremos pk de movimiento(Abono)/*************************************
                                            _pkMovimiento = new MovimientoPK();
                                            _pkMovimiento.setSesion(sesion);
                                            _pkMovimiento.setIdusuario(idUsuario);//Integer.parseInt(tbAtm.getDato1()));
                                            _pkMovimiento.setReferencia(objeto.getReferencia());
                                            _pkMovimiento.setN_mov(2);

                                            _movimiento.setPk(_pkMovimiento);

                                            _movimiento.setIdcuenta(tbComision.getDato2());
                                            _movimiento.setFecha(timestamp);

                                            //_movimiento.setIdorigen(cuenta_depositar.getIdorigen());
                                            //_movimiento.setIdgrupo(cuenta_depositar.getIdgrupo());
                                            //_movimiento.setIdsocio(cuenta_depositar.getIdsocio());
                                            _movimiento.setCargoabono(1);
                                            _movimiento.setMonto(Double.parseDouble(tbComision.getDato1()));
                                            _movimiento.setSai_aux("");

                                            mov_registrados = registraMovimientoService.insertarMovimiento(_movimiento);

                                            //Generamos el cargo al producto aux ¨************************************************
                                            _movimiento = new RegistraMovimiento();
                                            _pkMovimiento = new MovimientoPK();
                                            _pkMovimiento.setSesion(sesion);
                                            _pkMovimiento.setIdusuario(idUsuario);//Integer.parseInt(tbAtm.getDato1()));
                                            _pkMovimiento.setReferencia(objeto.getReferencia());
                                            _pkMovimiento.setN_mov(3);
                                            _movimiento.setPk(_pkMovimiento);

                                            PersonaPK personaPk = new PersonaPK(cuenta_depositar.getIdorigen(), cuenta_depositar.getIdgrupo(), cuenta_depositar.getIdsocio());
                                            cuenta_depositar = auxiliarService.buscarPorOgsProducto(personaPk, Integer.parseInt(tbComision.getDato3()));

                                            _movimiento.setIdorigenp(cuenta_depositar.getAuxiliarPK().getIdorigenp());
                                            _movimiento.setIdproducto(cuenta_depositar.getAuxiliarPK().getIdproducto());
                                            _movimiento.setIdauxiliar(cuenta_depositar.getAuxiliarPK().getIdauxiliar());
                                            _movimiento.setFecha(timestamp);
										/*if(bandera_cambio && objeto.getTipoTX() == 4) {
											//Aqui buscamos la cuenta concentradora (Retiro en sucursal)
											_movimiento.setIdcuenta(null);
										}*/
                                            _movimiento.setIdcuenta(null);
                                            _movimiento.setIdorigen(cuenta_depositar.getIdorigen());
                                            _movimiento.setIdgrupo(cuenta_depositar.getIdgrupo());
                                            _movimiento.setIdsocio(cuenta_depositar.getIdsocio());
                                            _movimiento.setCargoabono(0);
                                            Double montoComision = Double.parseDouble(tbComision.getDato1());
                                            tbPkComision = new TablaPK(idTabla, "iva_comision");
                                            tbComision = tablasService.buscarPorId(tbPkComision);

                                            _movimiento.setMonto((Double.parseDouble(tbComision.getDato2()) * montoComision) + montoComision);
                                            _movimiento.setSai_aux("");
                                            _movimiento.setTipo_amort(cuenta_depositar.getTipoamortizacion().intValue());

                                            mov_registrados = registraMovimientoService.insertarMovimiento(_movimiento);

                                            //Generamos el abono de iva comision a la cuenta correspondiente*************************************************************
                                            _movimiento = new RegistraMovimiento();
                                            //llenaremos pk de movimiento(Abono)
                                            _pkMovimiento = new MovimientoPK();
                                            _pkMovimiento.setSesion(sesion);
                                            _pkMovimiento.setIdusuario(idUsuario);//Integer.parseInt(tbAtm.getDato1()));
                                            _pkMovimiento.setReferencia(objeto.getReferencia());

                                            _movimiento.setPk(_pkMovimiento);
                                            //Buscamos la tabla configurada con la cuenta para comision


                                            _movimiento.setIdcuenta(tbComision.getDato1());
                                            _movimiento.setFecha(timestamp);

                                            //_movimiento.setIdorigen(cuenta_depositar.getIdorigen());
                                            //_movimiento.setIdgrupo(cuenta_depositar.getIdgrupo());
                                            //_movimiento.setIdsocio(cuenta_depositar.getIdsocio());
                                            _movimiento.setCargoabono(1);
                                            _movimiento.setMonto(montoComision * Double.parseDouble(tbComision.getDato2())); /*5.00 * 16.00*/
                                            _movimiento.setSai_aux("");

                                            mov_registrados = registraMovimientoService.insertarMovimiento(_movimiento);

                                            tot_movs = funcionesService.sai_aplica_transaccion(_movimiento.getPk().getIdusuario(), _movimiento.getPk().getSesion(), objeto.getReferencia());
                                            log.info("..Comision............................" + tot_movs);
                                        }


                                        String detalle = funcionesService.sai_detalle_transaccion_aplicada(new Date(), _movimiento.getPk().getIdusuario(), sesion, objeto.getReferencia());
                                        log.info("Detalle Transaccion aplicada:" + detalle);
                                        comprobante.setCodigo(200);
                                        comprobante.setDescripcionCuenta(producto.getNombre());
                                        //Entra solo si fue un deposito de cambio
                                        comprobante.setMonto(objeto.getMontoDeposito());
                                        comprobante.setMontoNoDepositado(0.00);

                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss.S");
                                        String fecha = sdf.format(new Date());
                                        String hora = sdf1.format(new Date());
                                        comprobante.setFechaHora(fecha + "T" + hora + "Z");
                                        comprobante.setDetalleComprobante(listaCol);


                                        String[] partes = detalle.split("\\|");
                                        List<String> lista = Arrays.asList(partes);
                                        //eliminamos los movimientos generados
                                        funcionesService.sai_termina_transaccion(new Date(), _movimiento.getPk().getIdusuario(), sesion, objeto.getReferencia());

                                        String mensageTx = "";
                                        if (producto.getTipoproducto() == 2) {
                                            log.info("El abono a seguro hipotecario fue de:" + lista.get(1));
                                            mensageTx = "Monto de la transaccion:" + objeto.getMontoDeposito()
                                                    + ",Fecha hora:" + new Date()
                                                    + ",Seguro hipotecario:" + lista.get(1)
                                                    + ",Comision cobranza:" + lista.get(2)
                                                    + ",Iva interes moratorio:" + lista.get(3)
                                                    + ",Interes ordinario:" + lista.get(4)
                                                    + ",Iva interes ordinario:" + lista.get(5)
                                                    + ",Capital:" + lista.get(6)
                                                    + ",Adelanto de interes:" + lista.get(7)
                                                    + ",Evita atrasos, en tu prestamo el pago de interes debe ser mensual";
                                        } else {
                                            mensageTx = "Monto de la transaccion:" + objeto.getMontoDeposito() + ",Fecha hora:" + new Date()
                                                    + ",Capital:" + objeto.getMontoDeposito();
                                        }
                                        comprobante.setMensajeUsuario(mensageTx);

                                        Auxiliar_d ultimoMovimiento = auxiliarDService.ultimoMovimiento(cuenta_depositar.getAuxiliarPK());
                                        comprobante.setNoAutorizacion(String.format("%06d", ultimoMovimiento.getIdorigenc()) + ultimoMovimiento.getPeriodo() + String.format("%02d", ultimoMovimiento.getIdtipo()) + String.format("%08d", ultimoMovimiento.getIdpoliza()));

                                        //Vamos a enviar SMS
                                        tbPk = new TablaPK("bankingly_banca_movil", "liga_envio_mensajes");
                                        tabla = tablasService.buscarPorId(tbPk);

                                        String url = tabla.getDato2();

                                        tbPk = new TablaPK(idTabla, "mensaje_deposito");
                                        tabla = tablasService.buscarPorId(tbPk);

                                        sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                                        String fechaParse = sdf.format(new Date());
                                        String mensaje = tabla.getDato2().replace("@nombreproducto@", producto.getNombre()).replace("@monto@", String.valueOf(objeto.getMontoDeposito())).replace("@folio@", objeto.getReferencia()).replace("@fecha@", fechaParse);


                                    } else {
                                        comprobante.setCodigo(409);
                                        comprobante.setMensajeUsuario("Error al procesar movimientos,contacte a proveedor de servicios");
                                        log.error("..........Falla al procesar movimientos en SAICoop...........");
                                    }


                                } else {
                                    comprobante.setCodigo(409);
                                    comprobante.setMensajeUsuario("Cuenta Inactiva");
                                    log.warn("............Verifique estatus de cuenta:" + cuenta_depositar.getEstatus());
                                }
                            } else {
                                log.error("Sucedio un error en la validacion");
                                comprobante.setCodigo(409);
                                comprobante.setMensajeUsuario(validacion[0]);
                            }

                        } else {
                            if (comprobante.getMensajeUsuario() == null) {
                                comprobante.setMensajeUsuario("Cuenta a depositar no existe");
                                log.error("..................Cuenta a depositar no existe.................");
                            }
                        }
                    } else {
                        comprobante.setCodigo(409);
                        comprobante.setMensajeUsuario("Cajero no identificado");
                        log.error("......Cajero no identificado.......");
                    }

                }
            } else {
                comprobante.setCodigo(409);
                comprobante.setMensajeUsuario("Socio bloqueado");
                log.error("......Socio bloqueado.......");
            }
            //funcionesService.sai_aplica_transaccion(movimiento.getFecha(),movimiento.getIdusuario(),movimiento.getSesion(),movimiento.getReferencia());
        } catch (Exception e) {
            log.error("Error al realizar deposito:" + e.getMessage());
        }
        return comprobante;
    }

    private String[] validarReglasCsn(Auxiliar auxiliar, Double monto, Double cambio, int idusuario) {
        String[] respuestaValidacion = new String[2];
        try {
            //Validamos que el auxiliar sea capaz de reicbir abonos
            TablaPK tbPk = new TablaPK(idTabla, "productos_abono");
            Tabla productosAbono = tablasService.buscarPorId(tbPk);
            boolean productoValido = false;
            if (productosAbono != null) {
                String productos = productosAbono.getDato2();
                String[] partes = productos.split("\\|");
                List<String> lista = Arrays.asList(partes);
                for (int i = 0; i < lista.size(); i++) {
                    if (auxiliar.getAuxiliarPK().getIdproducto() == Integer.parseInt(lista.get(i))) {
                        productoValido = true;
                        break;
                    }
                }

                boolean banderaMultiplo = true;
                //Si es el producto 111 AHORRO PATRIMONIAL
                tbPk = new TablaPK(idTabla, "ahorro_patrimonial");
                Tabla ahorroPatrimonial = tablasService.buscarPorId(tbPk);
                if (auxiliar.getAuxiliarPK().getIdproducto() == Integer.parseInt(ahorroPatrimonial.getDato1())) {
                    Double multiplo = 500.00;
                    if (monto % multiplo != 0) {
                        banderaMultiplo = false;
                    }
                }

                if (productoValido & banderaMultiplo) {
                    tbPk = new TablaPK(idTabla, "maximo_operacion");
                    Tabla montos = tablasService.buscarPorId(tbPk);
                    if (monto <= Double.parseDouble(montos.getDato1())) {
                        tbPk = new TablaPK(idTabla, "maximo_cambio");
                        montos = tablasService.buscarPorId(tbPk);
                        if (cambio <= Double.parseDouble(montos.getDato1())) {
                            tbPk = new TablaPK(idTabla, "maximo_deposito_diario");
                            montos = tablasService.buscarPorId(tbPk);
                            double montoHoy = auxiliarDService.montoDiario(auxiliar.getAuxiliarPK(), new Date(), idusuario) + monto;
                            log.debug("::::::::::::::::::Monto hoy::::::::::::" + montoHoy);
                            if (montoHoy <= Double.parseDouble(montos.getDato1())) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
                                String fecha = sdf.format(new Date());
                                log.debug("Periodo hoy:" + fecha);
                                tbPk = new TablaPK(idTabla, "maximo_deposito_mes");
                                montos = tablasService.buscarPorId(tbPk);
                                if ((monto + auxiliarDService.montoMensual(auxiliar.getAuxiliarPK(), fecha, idusuario)) <= Double.parseDouble(montos.getDato1())) {
                                    //Validamos el tipo de amortizacion
                                    if (auxiliar.getTipoamortizacion() == 5) {//Hipotecario
                                        //Correoms sai_auxiliar
                                        String sai_auxiliar = funcionesService.sai_auxiliar(auxiliar.getAuxiliarPK().getIdorigenp(), auxiliar.getAuxiliarPK().getIdproducto(), auxiliar.getAuxiliarPK().getIdauxiliar());
                                        String[] parametros_sai = sai_auxiliar.split("\\|");
                                        List lista_sai = Arrays.asList(parametros_sai);
                                        //Obtenemos informacion a cerca del proximo pago
                                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                                        Date fecha_sai = sdf.parse(lista_sai.get(10).toString());
                                        Origen matriz = origenService.origenMatriz();
                                        String prestamoCuanto = funcionesService.sai_prestamo_cuanto(auxiliar.getAuxiliarPK().getIdorigenp(), auxiliar.getAuxiliarPK().getIdproducto(), auxiliar.getAuxiliarPK().getIdauxiliar(), fecha_sai, auxiliar.getTipoamortizacion().intValue(), sai_auxiliar);
                                        String[] parametros_cuanto = prestamoCuanto.split("\\|");
                                        List<String> lista_prestamo_cuanto = Arrays.asList(parametros_cuanto);
                                        if (matriz.getFechatrabajo().compareTo(fecha_sai) <= 0) { //Fecha de trabajo menor a la fecha de proximo pago
                                            String limite_adelanto = funcionesService.sai_limite_adelanto(auxiliar.getAuxiliarPK().getIdorigenp(), auxiliar.getAuxiliarPK().getIdproducto(), auxiliar.getAuxiliarPK().getIdauxiliar(), monto);
                                            //Fecha trabajo mayor o igual a la fecha del proximo pago
                                            if (monto == Double.parseDouble(limite_adelanto)) {
                                                //Se deposito lo que se podia adelantar
                                                respuestaValidacion[0] = "OK";
                                                respuestaValidacion[1] = "200";
                                            } else {
                                                respuestaValidacion[0] = "PUEDE ABONAR HASTA:" + limite_adelanto;
                                                respuestaValidacion[1] = "400";
                                            }
                                        }
                                    } else {
                                        respuestaValidacion[0] = "OK";
                                        respuestaValidacion[1] = "200";
                                    }
                                } else {
                                    log.warn("..................Monto deposito mensual excede al permitido en el core.................");
                                    respuestaValidacion[0] = "MONTO DEPOSITO MENSUAL EXCEDE AL PERMITIDO EN EL CORE";
                                    respuestaValidacion[1] = "400";
                                }

                            } else {
                                log.warn("..................Monto deposito diario excede al permitido en el core.................");
                                respuestaValidacion[0] = "MONTO DEPOSITO DIARIO EXCEDE AL PERMITIDO EN EL CORE";
                                respuestaValidacion[1] = "400";
                            }
                        } else {
                            log.warn("..................Monto cambio excede al permitido en el core.................");
                            respuestaValidacion[0] = "MONTO CAMBIO EXCEDE AL PERMITIDO EN EL CORE";
                            respuestaValidacion[1] = "400";
                        }
                    } else {
                        log.warn("..................Monto deposito excede al permitido en el core.................");
                        respuestaValidacion[0] = "MONTO DEPOSITO EXCEDE AL PERMITIDO EN EL CORE";
                        respuestaValidacion[1] = "400";
                    }
                } else {
                    if (banderaMultiplo == false) {
                        respuestaValidacion[0] = "ASEGURATE QUE EL MONTO DEPOSITADO SEA MULTIPLO DE 500";
                        respuestaValidacion[1] = "400";
                    } else {
                        respuestaValidacion[0] = "PRODUCTO NO PUEDE RECIBIR ABONOS";
                        respuestaValidacion[1] = "400";
                    }

                }
            } else {
                respuestaValidacion[0] = "NO EXISTE CONFIGURACION PARA ABONOS";
                respuestaValidacion[1] = "400";
            }
        } catch (Exception e) {
            log.error("Error al validar reglas CSN:" + e.getMessage());
            e.printStackTrace();
        }
        return respuestaValidacion;

    }


    private String[] validarReglasSagrada(Auxiliar auxiliar, Double monto, Double cambio, int idusuario) {
        String[] respuestaValidacion = new String[2];
        try {
            //Validamos que el auxiliar sea capaz de reicbir abonos
            TablaPK tbPk = new TablaPK(idTabla, "productos_abono");
            Tabla productosAbono = tablasService.buscarPorId(tbPk);
            boolean productoValido = false;
            if (productosAbono != null) {
                String productos = productosAbono.getDato2();
                String[] partes = productos.split("\\|");
                List<String> lista = Arrays.asList(partes);
                for (int i = 0; i < lista.size(); i++) {
                    if (auxiliar.getAuxiliarPK().getIdproducto() == Integer.parseInt(lista.get(i))) {
                        productoValido = true;
                        break;
                    }
                }

                if (productoValido) {
                    tbPk = new TablaPK(idTabla, "maximo_operacion");
                    Tabla montos = tablasService.buscarPorId(tbPk);
                    if (monto <= Double.parseDouble(montos.getDato1())) {
                        tbPk = new TablaPK(idTabla, "maximo_cambio");
                        montos = tablasService.buscarPorId(tbPk);
                        if (cambio <= Double.parseDouble(montos.getDato1())) {
                            tbPk = new TablaPK(idTabla, "maximo_deposito_diario");
                            montos = tablasService.buscarPorId(tbPk);
                            double montoHoy = auxiliarDService.montoDiario(auxiliar.getAuxiliarPK(), new Date(), idusuario) + monto;
                            log.debug("::::::::::::::::::Monto hoy::::::::::::" + montoHoy);
                            if (montoHoy <= Double.parseDouble(montos.getDato1())) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
                                String fecha = sdf.format(new Date());
                                log.debug("Periodo hoy:" + fecha);
                                tbPk = new TablaPK(idTabla, "maximo_deposito_mes");
                                montos = tablasService.buscarPorId(tbPk);
                                if ((monto + auxiliarDService.montoMensual(auxiliar.getAuxiliarPK(), fecha, idusuario)) <= Double.parseDouble(montos.getDato1())) {
                                    //Validamos el tipo de amortizacion
                                    if (auxiliar.getTipoamortizacion() == 5) {//Hipotecario
                                        //Correoms sai_auxiliar
                                        String sai_auxiliar = funcionesService.sai_auxiliar(auxiliar.getAuxiliarPK().getIdorigenp(), auxiliar.getAuxiliarPK().getIdproducto(), auxiliar.getAuxiliarPK().getIdauxiliar());
                                        String[] parametros_sai = sai_auxiliar.split("\\|");
                                        List lista_sai = Arrays.asList(parametros_sai);
                                        //Obtenemos informacion a cerca del proximo pago
                                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                                        Date fecha_sai = sdf.parse(lista_sai.get(10).toString());
                                        Origen matriz = origenService.origenMatriz();
                                        String prestamoCuanto = funcionesService.sai_prestamo_cuanto(auxiliar.getAuxiliarPK().getIdorigenp(), auxiliar.getAuxiliarPK().getIdproducto(), auxiliar.getAuxiliarPK().getIdauxiliar(), fecha_sai, auxiliar.getTipoamortizacion().intValue(), sai_auxiliar);
                                        String[] parametros_cuanto = prestamoCuanto.split("\\|");
                                        List<String> lista_prestamo_cuanto = Arrays.asList(parametros_cuanto);
                                        if (matriz.getFechatrabajo().compareTo(fecha_sai) <= 0) { //Fecha de trabajo menor a la fecha de proximo pago
                                            String limite_adelanto = funcionesService.sai_limite_adelanto(auxiliar.getAuxiliarPK().getIdorigenp(), auxiliar.getAuxiliarPK().getIdproducto(), auxiliar.getAuxiliarPK().getIdauxiliar(), monto);
                                            //Fecha trabajo mayor o igual a la fecha del proximo pago
                                            if (monto == Double.parseDouble(limite_adelanto)) {
                                                //Se deposito lo que se podia adelantar
                                                respuestaValidacion[0] = "OK";
                                                respuestaValidacion[1] = "200";
                                            } else {
                                                respuestaValidacion[0] = "PUEDE ABONAR HASTA:" + limite_adelanto;
                                                respuestaValidacion[1] = "400";
                                            }
                                        }
                                    } else {
                                        respuestaValidacion[0] = "OK";
                                        respuestaValidacion[1] = "200";
                                    }
                                } else {
                                    log.warn("..................Monto deposito mensual excede al permitido en el core.................");
                                    respuestaValidacion[0] = "MONTO DEPOSITO MENSUAL EXCEDE AL PERMITIDO EN EL CORE";
                                    respuestaValidacion[1] = "400";
                                }

                            } else {
                                log.warn("..................Monto deposito diario excede al permitido en el core.................");
                                respuestaValidacion[0] = "MONTO DEPOSITO DIARIO EXCEDE AL PERMITIDO EN EL CORE";
                                respuestaValidacion[1] = "400";
                            }
                        } else {
                            log.warn("..................Monto cambio excede al permitido en el core.................");
                            respuestaValidacion[0] = "MONTO CAMBIO EXCEDE AL PERMITIDO EN EL CORE";
                            respuestaValidacion[1] = "400";
                        }
                    } else {
                        log.warn("..................Monto deposito excede al permitido en el core.................");
                        respuestaValidacion[0] = "MONTO DEPOSITO EXCEDE AL PERMITIDO EN EL CORE";
                        respuestaValidacion[1] = "400";
                    }
                } else {
                    respuestaValidacion[0] = "PRODUCTO NO PUEDE RECIBIR ABONOS";
                    respuestaValidacion[1] = "400";

                }
            } else {
                respuestaValidacion[0] = "NO EXISTE CONFIGURACION PARA ABONOS";
                respuestaValidacion[1] = "400";
            }
        } catch (Exception e) {
            log.error("Error al validar reglas CSN:" + e.getMessage());
            e.printStackTrace();
        }
        return respuestaValidacion;

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


    private String formateaCadena(String cadena) {
        String[] words = cadena.split("\\s+"); // Divide la cadena en palabras usando espacios

        StringBuilder maskedString = new StringBuilder();
        for (String word : words) {
            if (word.length() > 0) {
                // Agrega el primer carácter de la palabra y enmascara el resto con asteriscos
                maskedString.append(word.charAt(0));
                for (int i = 1; i < word.length(); i++) {
                    maskedString.append('*');
                }
                maskedString.append(' '); // Agrega un espacio después de cada palabra enmascarada
            }
        }

        return maskedString.toString().trim(); // Elimina el espacio final adicional
    }


}
