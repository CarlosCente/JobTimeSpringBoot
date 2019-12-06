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
INSERT INTO fichajes (empleado_cod_empl, fecha, hora_entrada, hora_salida, hora_inicio_descanso, hora_fin_descanso, ip_origen, tiempo_total, finalizado) VALUES (1, '2019-11-04', '08:00:00', '', '', '', '192.168.1.1', '', false);
INSERT INTO fichajes (empleado_cod_empl, fecha, hora_entrada, hora_salida, hora_inicio_descanso, hora_fin_descanso, ip_origen, tiempo_total, finalizado) VALUES (2, '2019-11-04', '08:00:00', '', '', '', '192.168.1.1', '', false);

 
/* Populate table Users and roles */
INSERT INTO users (username, password, enabled, cod_empl) VALUES ('admin', '$2y$12$6vxcbhYOgxlfFQxbPuApwuIobHyJHfppJqja/ZNPJjO9yYDovSXpa', 1, 1);
INSERT INTO users (username, password, enabled, cod_empl) VALUES ('laura', '$2a$10$PSPfqJj5gIvRiZyTa6qeJeCVbgFJcg9T4DoNQdOO9ZbV8mEgufYrC', 1, 2);
INSERT INTO users (username, password, enabled, cod_empl) VALUES ('juan', '$2a$10$6ozAFyj9nj3NddUArLobueiRGotr5JoFJ/.8jL1Cige1.RMHyA/ci', 1, 3);
INSERT INTO users (username, password, enabled, cod_empl) VALUES ('miguel', '$2a$10$VkWegNk0u7qQqgB.b7m6YOUX67VLdSxmdQ3Oxn5HHnxR3RhlrkNiO', 1, 4);


INSERT INTO authorities (user_id, authority) VALUES (1,'ROLE_USER');
INSERT INTO authorities (user_id, authority) VALUES (1,'ROLE_ADMIN');
INSERT INTO authorities (user_id, authority) VALUES (2,'ROLE_USER');
