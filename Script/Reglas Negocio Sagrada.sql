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
INSERT INTO tablas(idtabla,idelemento,dato2)VALUES('cajero_receptor','productos_abono','110|130|30102|30103|30104|30105|30106|30107|30108|30109|30110|30112|30113|30115|30117|30119|30120|30122|30123|30129|30130|30203|30213|30223|30302|30312|30322|30402|30412|30422|30502|30512|30522|30602|30702|30703|30704|30712|30713|30714|30722|30724|30802|30812|30822|31012|31022|31102|31112|31117|31122|31203|31213|31223|31303|31304|31305|31306|31313|31314|31315|31317|31323|31324|31325|31326|31403|31413|31423|31503|31513|31523|31603|31613|31623|31703|31713|31723|31803|31813|31823|31903|31913|31923|32202|32302|32402|32502|32512|32602|32702|32802|32902|33002|33012|33102|33112|33202|33212|33302|33312|33402|33412|33502|33512|33602|33612|33702|33712|33802|33812|33822|33902|33912|34002|34012|34102|34112|34122|34302|34312|34322|34402|34412|34422|34902|34912|34922|35002|35102|35202|35402|35502|35512|35522|35602|35603|35612|35613|35622|35623|35702|35902');

/*Maximo operacion para ahorros para prestamos se usa los valores de sai auxiliar*/
DELETE FROM tablas WHERE idtabla='cajero_receptor' AND idelemento='maximo_operacion';
INSERT INTO tablas(idtabla,idelemento,dato1)VALUES('cajero_receptor','maximo_operacion','15000');

/*Minimo operacion para ahorros para prestamos se usa los valores de sai auxiliar*/
DELETE FROM tablas WHERE idtabla='cajero_receptor' AND idelemento='minimo_operacion';
INSERT INTO tablas(idtabla,idelemento,dato1)VALUES('cajero_receptor','minimo_operacion','50');

/*Maximo cambio*/
DELETE FROM tablas WHERE idtabla='cajero_receptor' AND idelemento='maximo_cambio';
INSERT INTO tablas(idtabla,idelemento,dato1)VALUES('cajero_receptor','maximo_cambio','500');

/*Datos para obtener el certificado para generar firma*/
/*Dato1: Nombre JKS,Dato2:Alias de la JKS,Dato3:contraseña jks*/
DELETE FROM tablas WHERE idtabla='cajero_receptor' AND idelemento='datos_certificado';
INSERT INTO tablas(idtabla,idelemento,dato1,dato2,dato3)VALUES('cajero_receptor','datos_certificado','fenoreste','fenoreste','fenoreste2023');








/*Maximo diario*/
DELETE FROM tablas WHERE  idtabla='cajero_receptor' AND idelemento='maximo_deposito_diario';
INSERT INTO tablas(idtabla,idelemento,dato1)VALUES('cajero_receptor','maximo_deposito_diario','70000');

/*Maximo Mes*/
DELETE FROM tablas WHERE  idtabla='cajero_receptor' AND idelemento='maximo_deposito_mes';
INSERT INTO tablas(idtabla,idelemento,dato1)VALUES('cajero_receptor','maximo_deposito_mes','70000');