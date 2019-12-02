package com.cjhercen.springboot.app.util;

import java.util.Calendar;

public class FechaUtils {

	/*
	 * Obtener la hora en formado hh:mm como un String
	 */
	public static String obtenerHoraEnFormatoCadena() {
		
		Calendar calendario = Calendar.getInstance();
		int hora, minutos;
		hora = calendario.get(Calendar.HOUR_OF_DAY);
		minutos = calendario.get(Calendar.MINUTE);
		
		return hora + ":" + minutos;
	}
	
	
}
