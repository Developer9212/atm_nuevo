
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

