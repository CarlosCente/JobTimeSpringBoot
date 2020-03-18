package com.cjhercen.springboot.app.util;

public interface ConstantesSolicitudes {

	/*
	 * Constantes que definen los tipos de solicitudes y sus tiempos
	 */
	
	//Solicitudes o ausencias que computan como dias completos
	public static final String TIPO_VACACIONES = "Vacaciones"; // 1 dia * los dias elegidos
	public static final String TIPO_ENFERMEDAD = "Enfermedad"; // x dias hasta que den el alta
	public static final String TIPO_OPERACION_FAMILIAR = "Operación de un familiar"; // 2 días o 4 si es necesario desplazamiento
	public static final String TIPO_MATRIMONIO = "Matrimonio"; // 15 días naturales
	public static final String TIPO_NACIMIENTO = "Nacimiento de un hijo"; //2 dias o 4 si es necesario desplazamiento
	
	//Solicitudes o ausencias que computan como x horas (Indicar horas necesarias en el formulario)
	//En caso de tardar mas tiempo se podrá abrir una incidencia para indicar el cambio al administrador
	public static final String TIPO_EXAMEN = "Examen";
	public static final String TIPO_CITA_MEDICA = "Cita médica";
	public static final String TIPO_FORMACION = "Asistencia a formación";
		
	
	/*
	 * Constantes que definen los estados de las solicitudes 
	 * 1 --> Solicitud abierta
	 * 2 --> Solicitud aceptada
	 * 3 --> Solicitud rechazada
	 */
	public static final String SOLICITUD_ABIERTA = "1";
	public static final String SOLICITUD_ACEPTADA = "2";
	public static final String SOLICITUD_RECHAZADA = "3";
	
	
	/*
	 * Tiene o no permiso para contabilizar el fichaje directamente como 8 horas durante esos días
	 */
	public static final Boolean TIENE_PERMISO = true;
	public static final Boolean NO_TIENE_PERMISO = false;
	
}
