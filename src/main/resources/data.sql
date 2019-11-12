/* Populate table empleados */
INSERT INTO empleados (DNI, apellido1, apellido2, direccion, fecha_nacim, foto, localidad, nombre, pais, provincia) VALUES('12345678A', 'Hernández', 'Centenero', 'Plaza Juan Pablo II', '1992-12-13', '', 'Valladolid', 'Carlos', 'España', 'Valladolid');
INSERT INTO empleados (DNI, apellido1, apellido2, direccion, fecha_nacim, foto, localidad, nombre, pais, provincia) VALUES('87654321B', 'Ruiz', 'Fernández', 'Plaza Juan Pablo II', '1993-09-29', '', 'Valladolid', 'Laura', 'España', 'Valladolid');
INSERT INTO empleados (DNI, apellido1, apellido2, direccion, fecha_nacim, foto, localidad, nombre, pais, provincia) VALUES('14714714C', 'Estebanez', 'Fernández', 'Calle la Nave', '1992-01-03', '',  'Villanueva del Campo', 'Juan', 'España', 'Zamora');

/* Populate table Fichaje */
INSERT INTO fichajes (empleado_cod_empl, fecha, hora_entrada, hora_salida, tipo_fichaje) VALUES (1, '2019-11-04', '08:00:00', '', 'ENTRADA');