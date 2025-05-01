DELETE FROM tablas WHERE idtabla ='cajero_receptor' AND idelemento='horario_actividad';
INSERT INTO tablas(idtabla,idelemento,dato1,dato2,dato3)VALUES('cajero_receptor','horario_actividad','05:00','21:45','1|2|3|4|5');

/*Producto default para depositar cambio en caso de que el ATM no pueda entregar efectivo 1.-Producto base , 2.- Opcional*/
DELETE FROM tablas WHERE idtabla='cajero_receptor' AND idelemento='producto_default';
INSERT INTO tablas(idtabla,idelemento,dato1,dato2)VALUES('cajero_receptor','producto_default','130','110');

/*Cajero: Idelemennto = idCajero en peticion, dato1= idusuario en saicoop*/
DELETE FROM tablas WHERE idtabla='cajero_receptor' AND idelemento='WY000003';
INSERT INTO tablas(idtabla,idelemento,dato1)VALUES('cajero_receptor','WY000003','999');

/*Productos que aceptan abonos*/
DELETE FROM tablas WHERE idtabla='cajero_receptor' AND idelemento='productos_abono';
INSERT INTO tablas(idtabla,idelemento,dato2)VALUES('cajero_receptor','productos_abono','110|111|133|32644');

/*Maximo operacion para ahorros para prestamos se usa los valores de sai auxiliar*/
DELETE FROM tablas WHERE idtabla='cajero_receptor' AND idelemento='maximo_operacion';
INSERT INTO tablas(idtabla,idelemento,dato1)VALUES('cajero_receptor','maximo_operacion','100');

/*Minimo operacion para ahorros para prestamos se usa los valores de sai auxiliar*/
DELETE FROM tablas WHERE idtabla='cajero_receptor' AND idelemento='minimo_operacion';
INSERT INTO tablas(idtabla,idelemento,dato1)VALUES('cajero_receptor','minimo_operacion','50');

/*Maximo cambio*/
DELETE FROM tablas WHERE idtabla='cajero_receptor' AND idelemento='maximo_cambio';
INSERT INTO tablas(idtabla,idelemento,dato1)VALUES('cajero_receptor','maximo_cambio','500');








/*Maximo Diario*/
DELETE FROM tablas WHERE  idtabla='cajero_receptor' AND idelemento='maximo_deposito_diario';
INSERT INTO tablas(idtabla,idelemento,dato1)VALUES('cajero_receptor','maximo_deposito_diario','299999');

/*Maximo Mes*/
DELETE FROM tablas WHERE  idtabla='cajero_receptor' AND idelemento='maximo_deposito_mes';
INSERT INTO tablas(idtabla,idelemento,dato1)VALUES('cajero_receptor','maximo_deposito_mes','599999');