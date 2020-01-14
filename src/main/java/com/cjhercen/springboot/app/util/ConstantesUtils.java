package com.cjhercen.springboot.app.util;

public interface ConstantesUtils {
	
	
	/*
	 * Constantes que definen los diferentes tipos de fichajes que se pueden dar
	 */
	
	public static final String TIPO_ENTRADA = "ENTRADA";
	public static final String TIPO_SALIDA = "SALIDA";

	
	/*
	 * Constantes que definen los tipos de estado y los tipos de las incidencias
	 */
	public static final String INCIDENCIA_ABIERTA = "1";
	public static final String INCIDENCIA_RESUELTA = "2";
	public static final String INCIDENCIA_ERROR = "1";
	public static final String INCIDENCIA_ADVERTENCIA = "2";
	
	/*
	 * Mensaje para las incidencias
	 */
	public static final String INCIDENCIA_PERFIL = "Incidencia sobre los datos personales del empleado";
	public static final String INCIDENCIA_DATOS_DIRECCION = "Incidencia sobre los datos de dirección del empleado";
	public static final String INCIDENCIA_FICHAJE_ENTRADA = "Incidencia sobre el fichaje de entrada del empleado";
	public static final String INCIDENCIA_FICHAJE_SALIDA = "Incidencia sobre el fichaje de salida del empleado";
	public static final String INCIDENCIA_FICHAJE_OTROS = "Incidencia sobre el fichaje del empleado (Otras)";
	
	/*
	 * Tipos de incidencias
	 */
	public static final String INCIDENCIA_ENTRADA = "entrada";
	public static final String INCIDENCIA_SALIDA = "salida";
	public static final String INCIDENCIA_OTROS = "otro";
	public static final String INCIDENCIA_NO_SELECCION = "nada";
	
	/*
	 * Ruta para las imagenes subidas al servidor
	 */
	public static final String RUTA_IMAGENES_EMPLEADOS = "C://jobtime//uploads//imagenes";
	
}
