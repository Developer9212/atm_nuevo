
DELETE FROM tablas WHERE idtabla ='cajero_receptor' AND idelemento='horario_actividad';
INSERT INTO tablas(idtabla,idelemento,dato1,dato2,dato3)VALUES('cajero_receptor','horario_actividad','06:00','17:00','1|2|3|4|5');

/*Producto default para depositar cambio en caso de que el ATM no pueda entregar efectivo 1.-Producto base , 2.- Opcional*/
DELETE FROM tablas WHERE idtabla='cajero_receptor' AND idelemento='producto_default';
INSERT INTO tablas(idtabla,idelemento,dato1,dato2)VALUES('cajero_receptor','producto_default','110','133');

/*Cajero: Idelemennto = idCajero en peticion, dato1= idusuario en saicoop*/
DELETE FROM tablas WHERE idtabla='cajero_receptor' AND idelemento='sss001';
INSERT INTO tablas(idtabla,idelemento,dato1)VALUES('cajero_receptor','sss001','999');


DELETE FROM tablas WHERE idtabla='cajero_receptor' AND idelemento ='user-ws';
INSERT INTO tablas(idtabla,idelemento,dato1,dato2)VALUES('cajero_receptor','user-ws','csn001','csn001');

DELETE FROM tablas WHERE idtabla='cajero_receptor' AND idelemento ='secret';
INSERT INTO tablas(idtabla,idelemento,dato1)VALUES('cajero_receptor','secret','csn2023');

/*Productos que aceptan abonos*/
DELETE FROM tablas WHERE idtabla='cajero_receptor' AND idelemento='productos_abono';
INSERT INTO tablas(idtabla,idelemento,dato2)VALUES('cajero_receptor','productos_abono','110|111|133|32644');

/*Datos para obtener el certificado para generar firma*/
DELETE FROM tablas WHERE idtabla='cajero_receptor' AND idelemento='datos_certificado';
INSERT INTO tablas(idtabla,idelemento,dato1,dato2,dato3)VALUES('cajero_receptor','datos_certificado','fenoreste','fenoreste','fenoreste2023');

/*Datos calculo de comision por operacion dato1:montoComision,dato2:cuenta,dato3:ProductoParaCargoComision,dato4:onfirmaSiActivaComision 1 si 0 no*/
DELETE FROM tablas WHERE idtabla='cajero_receptor' AND idelemento='comision';
INSERT INTO tablas(idtabla,idelemento,dato1,dato2,dato3,dato4)VALUES('cajero_receptor','comision','5.00','453534543','110','1');

/*Iva de la comision dato1: cuenta dato2:valor del iva*/
DELETE FROM tablas WHERE idtabla='cajero_receptor' AND idelemento='iva_comision';
INSERT INTO tablas(idtabla,idelemento,dato1,dato2)VALUES('cajero_receptor','iva_comision','453534543','16.00');


/*Maximo operacion*/
DELETE FROM tablas WHERE idtabla='cajero_receptor' AND idelemento='maximo_operacion';
INSERT INTO tablas(idtabla,idelemento,dato1)VALUES('cajero_receptor','maximo_operacion','100');

/*Minimo operacion*/
DELETE FROM tablas WHERE idtabla='cajero_receptor' AND idelemento='minimo_operacion';
INSERT INTO tablas(idtabla,idelemento,dato1)VALUES('cajero_receptor','minimo_operacion','50');

/*Maximo cambio*/
DELETE FROM tablas WHERE idtabla='cajero_receptor' AND idelemento='maximo_cambio';
INSERT INTO tablas(idtabla,idelemento,dato1)VALUES('cajero_receptor','maximo_cambio','500');


/*SMS Deposito*/
DELETE FROM tablas WHERE  idtabla='cajero_receptor' AND idelemento='mensaje_deposito';
INSERT INTO tablas(idtabla,idelemento,dato2)VALUES('cajero_receptor','mensaje_deposito','Deposito al @nombreproducto@ por la cantidad de @monto@ con folio @folio@ el @fecha@');


/*Maximo Diario*/
DELETE FROM tablas WHERE  idtabla='cajero_receptor' AND idelemento='maximo_deposito_diario';
INSERT INTO tablas(idtabla,idelemento,dato1)VALUES('cajero_receptor','maximo_deposito_diario','299999');

/*Maximo Mes*/
DELETE FROM tablas WHERE  idtabla='cajero_receptor' AND idelemento='maximo_deposito_mes';
INSERT INTO tablas(idtabla,idelemento,dato1)VALUES('cajero_receptor','maximo_deposito_mes','599999');

/*Ahorro patrimonial*/
DELETE FROM tablas WHERE  idtabla='cajero_receptor' AND idelemento='ahorro_patrimonial';
INSERT INTO tablas(idtabla,idelemento,dato1,dato2)VALUES('cajero_receptor','ahorro_patrimonial','110','Producto solo opera sobre multiplos de 500');
