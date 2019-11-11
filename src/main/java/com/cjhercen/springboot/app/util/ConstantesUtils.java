package com.cjhercen.springboot.app.util;

public class ConstantesUtils {
	
	
	/*
	 * Constantes que definen los diferentes tipos de fichajes que se pueden dar
	 */
	
	private static final String TIPO_ENTRADA = "ENTRADA";
	private static final String TIPO_SALIDA = "SALIDA";
	private static final String TIPO_RECUPERA_ENTRADA = "RECUPERA_ENTRADA";
	private static final String TIPO_RECUPERA_SALIDA = "RECUPERA_SALIDA";
	
	
	public static String getTipoEntrada() {
		return TIPO_ENTRADA;
	}
	public static String getTipoSalida() {
		return TIPO_SALIDA;
	}
	public static String getTipoRecuperaEntrada() {
		return TIPO_RECUPERA_ENTRADA;
	}
	public static String getTipoRecuperaSalida() {
		return TIPO_RECUPERA_SALIDA;
	}
	
	
}
