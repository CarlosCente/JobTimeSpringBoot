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
	public static final String INCIDENCIA_PERFIL = "Incidencia de Datos personales";
	public static final String INCIDENCIA_DATOS_DIRECCION = "Incidencia de Datos de dirección";
	public static final String INCIDENCIA_FICHAJE_ENTRADA = "Incidencia de Fichaje de entrada";
	public static final String INCIDENCIA_FICHAJE_SALIDA = "Incidencia de Fichaje de salida";
	public static final String INCIDENCIA_FICHAJE_OTROS = "Incidencia de Fichaje (Otras)";
	
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
