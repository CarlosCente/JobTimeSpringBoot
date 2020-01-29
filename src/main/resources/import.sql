/* Populate table empleados */
INSERT INTO empleados (apellido1, apellido2, direccion, fecha_nacim, localidad, nombre, pais, provincia, es_admin) VALUES('Hernández', 'Centenero', 'Plaza Juan Pablo II', '1992-12-13', 'Valladolid', 'Carlos', 'España', 'Valladolid', 1);
INSERT INTO empleados (apellido1, apellido2, direccion, fecha_nacim, localidad, nombre, pais, provincia, es_admin) VALUES('Ruiz', 'Fernández', 'Plaza Juan Pablo II', '1993-09-29', 'Valladolid', 'Laura', 'España', 'Valladolid', 0);
INSERT INTO empleados (apellido1, apellido2, direccion, fecha_nacim, localidad, nombre, pais, provincia, es_admin) VALUES('Estebanez', 'Fernández', 'Calle la Nave', '1992-01-03', 'Villanueva del Campo', 'Juan', 'España', 'Zamora', 0);
INSERT INTO empleados (apellido1, apellido2, direccion, fecha_nacim, localidad, nombre, pais, provincia, es_admin) VALUES('Hernández', 'Centenero', 'Calle Huerta del Rey', '1999-12-24' , 'Valladolid', 'Miguel' , 'España' ,'Valladolid', 0);
INSERT INTO empleados (apellido1, apellido2, direccion, fecha_nacim, localidad, nombre, pais, provincia, es_admin) VALUES('Vega', 'Ejemplo', 'Calle de Ejemplo', '1992-12-10', 'Valladolid' , 'Leticia', 'España', 'Valladolid', 0);
INSERT INTO empleados (apellido1, apellido2, direccion, fecha_nacim, localidad, nombre, pais, provincia, es_admin) VALUES('Gutierrez', 'Ejemplo', 'Calle de Ejemplo', '1992-12-10', 'Valladolid' , 'Pepe', 'España', 'Valladolid', 0);


/* Populate table Fichaje */
INSERT INTO fichajes (empleado_cod_empl, fecha, hora_entrada, hora_salida, ip_origen, tiempo_total, finalizado) VALUES (1, '2019-11-04', '08:00', '', '192.168.1.1', '', false);
INSERT INTO fichajes (empleado_cod_empl, fecha, hora_entrada, hora_salida, ip_origen, tiempo_total, finalizado) VALUES (2, '2019-11-04', '08:00', '', '192.168.1.1', '', false);
INSERT INTO fichajes (empleado_cod_empl, fecha, hora_entrada, hora_salida, ip_origen, tiempo_total, finalizado) VALUES (2, '2020-01-04', '14:00', '', '192.168.1.1', '', false);
INSERT INTO fichajes (empleado_cod_empl, fecha, hora_entrada, hora_salida, ip_origen, tiempo_total, finalizado) VALUES (2, '2020-01-23', '', '16:00', '192.168.1.1', '', false);

 
/* Populate table Users and roles */
INSERT INTO users (username, password, enabled, cod_empl) VALUES ('admin', '$2y$12$6vxcbhYOgxlfFQxbPuApwuIobHyJHfppJqja/ZNPJjO9yYDovSXpa', 1, 1);
INSERT INTO users (username, password, enabled, cod_empl) VALUES ('laura', '$2a$10$PSPfqJj5gIvRiZyTa6qeJeCVbgFJcg9T4DoNQdOO9ZbV8mEgufYrC', 1, 2);
INSERT INTO users (username, password, enabled, cod_empl) VALUES ('juan', '$2a$10$6ozAFyj9nj3NddUArLobueiRGotr5JoFJ/.8jL1Cige1.RMHyA/ci', 1, 3);
INSERT INTO users (username, password, enabled, cod_empl) VALUES ('miguel', '$2y$12$vMYXOOoVzK4srU6SAHpseeUGiSg0gyKc2nrw9d1//DYvrd7FtTfYS', 1, 4);
INSERT INTO users (username, password, enabled, cod_empl) VALUES ('leticia', '$2a$10$6ozAFyj9nj3NddUArLobueiRGotr5JoFJ/.8jL1Cige1.RMHyA/ci', 1, 5);
INSERT INTO users (username, password, enabled, cod_empl) VALUES ('pepe', '$2y$12$vMYXOOoVzK4srU6SAHpseeUGiSg0gyKc2nrw9d1//DYvrd7FtTfYS', 1, 6);

INSERT INTO authorities (user_id, authority) VALUES (1,'ROLE_ADMIN');
INSERT INTO authorities (user_id, authority) VALUES (2,'ROLE_USER');
INSERT INTO authorities (user_id, authority) VALUES (3,'ROLE_USER');
INSERT INTO authorities (user_id, authority) VALUES (4,'ROLE_USER');
INSERT INTO authorities (user_id, authority) VALUES (5,'ROLE_USER');
INSERT INTO authorities (user_id, authority) VALUES (6,'ROLE_USER');


/* Populate table incidencias */
INSERT INTO incidencias (estado, fecha, mensaje, tipo, empleado_cod_empl,descripcion) VALUES ('2', '2020-01-16', 'Incidencia de Fichaje de salida', '1', 1,'El usuario admin (Carlos Hernández Centenero), ha creado una incidencia con su fichaje de entrada del día (20/01/2020) la hora correcta sería a las (8:00). Además el usuario ha añadido el siguiente comentario: Se me olvidó');
INSERT INTO incidencias (estado, fecha, mensaje, tipo, empleado_cod_empl,descripcion) VALUES ('1', '2020-01-23', 'Incidencia de Fichaje de entrada', '1', 2,'El usuario laura (Laura Ruiz Fernández), ha creado una incidencia con su fichaje de entrada del día (23/01/2020) la hora correcta sería a las (8:00). Además el usuario ha añadido el siguiente comentario: Problemas con el acceso al ordenador al llegar a la oficina.');
INSERT INTO incidencias (estado, fecha, mensaje, tipo, empleado_cod_empl,descripcion) VALUES ('1', '2020-01-23', 'Incidencia de Fichaje de salida', '1', 2,'El usuario laura (Laura Ruiz Fernández), ha creado una incidencia con su fichaje de salida del día (23/01/2020) la hora correcta sería a las (16:00). Además el usuario ha añadido el siguiente comentario: Problemas con el acceso al ordenador al llegar a la oficina.');
INSERT INTO incidencias (estado, fecha, mensaje, tipo, empleado_cod_empl,descripcion) VALUES ('1', '2020-01-23', 'Incidencia de Datos personales', '1', 2,'El usuario laura tiene un error en sus datos personales en el campo  (Nombre), el valor correcto sería (asdad) , en el campo (Primer Apellido), el valor correcto sería (xzczczxc) , en el campo (Segundo Apellido), el valor correcto sería (bnbnnbn) , en el campo (Fecha de Nacimiento), el valor correcto sería (14-01-1985)');

