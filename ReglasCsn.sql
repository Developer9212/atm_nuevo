
DELETE FROM tablas WHERE idtabla ='atm' AND idelemento='atm_horario_actividad';
INSERT INTO tablas(idtabla,idelemento,dato1,dato2,dato3)VALUES('atm','atm_horario_actividad','06:00','17:00','1|2|3|4|5');

DELETE FROM tablas WHERE idtabla='atm' AND idelemento='producto_default';
INSERT INTO tablas(idtabla,idelemento,dato1)VALUES('atm','producto_default','110');


DELETE FROM tablas WHERE idtabla='atm' AND idelemento='usuario';
INSERT INTO tablas(idtabla,idelemento,dato1)VALUES('atm','usuario','999');