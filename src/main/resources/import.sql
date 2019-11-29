/* Populate table empleados */
INSERT INTO empleados (apellido1, apellido2, direccion, fecha_nacim, localidad, nombre, pais, provincia) VALUES('Hernández', 'Centenero', 'Plaza Juan Pablo II', '1992-12-13', 'Valladolid', 'Carlos', 'España', 'Valladolid');
INSERT INTO empleados (apellido1, apellido2, direccion, fecha_nacim, localidad, nombre, pais, provincia) VALUES('Ruiz', 'Fernández', 'Plaza Juan Pablo II', '1993-09-29', 'Valladolid', 'Laura', 'España', 'Valladolid');
INSERT INTO empleados (apellido1, apellido2, direccion, fecha_nacim, localidad, nombre, pais, provincia) VALUES('Estebanez', 'Fernández', 'Calle la Nave', '1992-01-03', 'Villanueva del Campo', 'Juan', 'España', 'Zamora');
INSERT INTO empleados (apellido1, apellido2, direccion, fecha_nacim, localidad, nombre, pais, provincia) VALUES('Hernández', 'Centenero', 'Calle Huerta del Rey', '1999-12-24' , 'Valladolid', 'Miguel' , 'España' ,'Valladolid');
INSERT INTO empleados (apellido1, apellido2, direccion, fecha_nacim, localidad, nombre, pais, provincia) VALUES('Vega', 'Ejemplo', 'Calle de Ejemplo', '1992-12-10', 'Valladolid' , 'Leticia', 'España', 'Valladolid');
INSERT INTO empleados (apellido1, apellido2, direccion, fecha_nacim, localidad, nombre, pais, provincia) VALUES('Gutierrez', 'Ejemplo', 'Calle de Ejemplo', '1992-12-10', 'Valladolid' , 'Pepe', 'España', 'Valladolid');
INSERT INTO empleados (apellido1, apellido2, direccion, fecha_nacim, localidad, nombre, pais, provincia) VALUES('Gonzalez', 'Ejemplo', 'Calle de Ejemplo', '1996-01-17', 'Valladolid' , 'Maria', 'España', 'Valladolid');
INSERT INTO empleados (apellido1, apellido2, direccion, fecha_nacim, localidad, nombre, pais, provincia) VALUES('Centenero', 'Ejemplo', 'Calle de Ejemplo', '1990-08-18', 'Valladolid' , 'Eva', 'España', 'Valladolid');
INSERT INTO empleados (apellido1, apellido2, direccion, fecha_nacim, localidad, nombre, pais, provincia) VALUES('Hernandez', 'García', 'Calle la ruina', '1989-07-14', 'Segovia', 'Pablo', 'España', 'Segovia');
INSERT INTO empleados (apellido1, apellido2, direccion, fecha_nacim, localidad, nombre, pais, provincia) VALUES('Castelo', 'Ferreira', 'Calle las Angustias', '1991-09-29', 'Barcelona', 'Mohamed', 'España', 'Barcelona');
INSERT INTO empleados (apellido1, apellido2, direccion, fecha_nacim, localidad, nombre, pais, provincia) VALUES('Apellido1', 'Apellido2', 'Calle Ejemplo1', '1992-01-03', 'Villanueva del Campo', 'Juan', 'España', 'Zamora');
INSERT INTO empleados (apellido1, apellido2, direccion, fecha_nacim, localidad, nombre, pais, provincia) VALUES('Apellido12', 'Apellido22', 'Calle Ejemplo2', '1992-01-03', 'Villanueva del Campo', 'Bonifacio', 'España', 'Zamora');
INSERT INTO empleados (apellido1, apellido2, direccion, fecha_nacim, localidad, nombre, pais, provincia) VALUES('Apellido13', 'Apellido23', 'Calle Ejemplo3', '1992-01-03', 'Villanueva del Campo', 'Laura', 'España', 'Zamora');
INSERT INTO empleados (apellido1, apellido2, direccion, fecha_nacim, localidad, nombre, pais, provincia) VALUES('Apellido14', 'Apellido24', 'Calle Ejemplo4', '1992-01-03', 'Villanueva del Campo', 'Liliana', 'España', 'Zamora');
INSERT INTO empleados (apellido1, apellido2, direccion, fecha_nacim, localidad, nombre, pais, provincia) VALUES('Apellido15', 'Apellido25', 'Calle Ejemplo5', '1992-01-03', 'Villanueva del Campo', 'Cristina', 'España', 'Zamora');
INSERT INTO empleados (apellido1, apellido2, direccion, fecha_nacim, localidad, nombre, pais, provincia) VALUES('Apellido16', 'Apellido26', 'Calle Ejemplo6', '1992-01-03', 'Villanueva del Campo', 'Dario', 'España', 'Zamora');


/* Populate table Fichaje */
INSERT INTO fichajes (empleado_cod_empl, fecha, hora_entrada, hora_salida, tipo_fichaje, ip_origen) VALUES (1, '2019-11-04', '08:00:00', '', 'ENTRADA', '192.168.1.1');
 
/* Populate table Users and roles */
INSERT INTO users (username, password, enabled) VALUES ('admin', '$2y$12$6vxcbhYOgxlfFQxbPuApwuIobHyJHfppJqja/ZNPJjO9yYDovSXpa', 1);
INSERT INTO users (username, password, enabled) VALUES ('user', '$2y$12$fXk3Ntqbo/gbXaTpOspHye5FUgNpbOG0fexCRgCUb1HJwEstPrasq', 1);

INSERT INTO authorities (user_id, authority) VALUES (1,'ROLE_USER');
INSERT INTO authorities (user_id, authority) VALUES (1,'ROLE_ADMIN');
INSERT INTO authorities (user_id, authority) VALUES (2,'ROLE_USER');
