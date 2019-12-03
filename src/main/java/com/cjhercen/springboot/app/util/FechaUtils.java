package com.cjhercen.springboot.app.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Utilidades para trabajar con las fechas y horas de los fichajes
 * @author cjhercen
 * @version 03/12/2019
 */
public class FechaUtils {


	public FechaUtils() {

	}

	/**
	 * Obtener la hora en formato hh:mm como un String
	 * @return hora en el formato hh:mm
	 */
	public String obtenerHoraEnFormatoCadena() {
		
		Calendar calendario = Calendar.getInstance();
		int hora, minutos;
		hora = calendario.get(Calendar.HOUR_OF_DAY);
		minutos = calendario.get(Calendar.MINUTE);
		System.out.println(minutos);
		String minutosString = "";
		
		if( minutos < 10) {
			minutosString = "0"+minutos;
			if(minutos == 0) {
				minutosString = "00";
			}
		} else {
			minutosString = String.valueOf(minutos);
		}
		
		return hora + ":" + minutosString;
	}
	
	/**
	 * Obtener la hora en formato dd-MM-yyyy como un String
	 * @return hora en el formato dd-MM-yyyy
	 */
	public String obtenerFechaEnFormatoCadena() {
		
		Date date = new Date();
		DateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
		
		return dateFormat.format(date);
		
	}
	
	/**
	 * Obtener la fecha actual en formato dd-MM-yyyy como un Date
	 * @return hora en el formato dd-MM-yyyy
	 */
	public Date obtenerFechaActual() {
		
		Date date = new Date();
		return date;		
	}
	
	
	
	/**
	 * Método que devuelve los minutos totales de una hora que se pase como parámetro
	 * @param hora que deseamos pasar a minutos
	 * @return minutos totales
	 */
	public int obtenerHoraEnMinutos(String hora) {
		
		int minutosTotales = 0;
		
		String[] partes = hora.split(":");
		String horas = partes[0];
		String minutos = partes[1];
		minutosTotales = Integer.parseInt(horas)*60 + Integer.parseInt(minutos);
		
		return minutosTotales;
		
	}
	
	/**
	 * Método que devuelve la hora en formato hh:mm como un string 
	 * @param minutosTotales minutos totales de la hora indicada
	 * @return hora transformada a String con formato hh:mm
	 */
	public String obtenerFechaStringDesdeInt(int minutosTotales) {
		String result = "";
		int horas = minutosTotales/60;
		int minutos = minutosTotales%60;
				
		result = String.valueOf(horas) + " : " + String.valueOf(minutos);
		
		return result;
	}
	
	/**
	 * Método para obtener el tiempo transcurrido entre los fichajes, dependiendo si se han fichado los dos o sólo entrada
	 * @param horaEntrada hora que se ha fichado al entrar
	 * @param horaSalida hora que se ha fichado al salir
	 * @return total del tiempo transcurrido de trabajo
	 */
	public String obtenerTiempoTranscurrido(String horaEntrada, String horaSalida) {
	
		String tiempoTotalFichaje = "";
		
		//Si solo se ha fichado la entrada
		if((horaEntrada != null || !"".equals(horaEntrada)) && (horaSalida == null || "".equals(horaSalida))) {
			
			String horaActual = obtenerHoraEnFormatoCadena();
			int tiempoActual = obtenerHoraEnMinutos(horaActual);
			int tiempoEntrada = obtenerHoraEnMinutos(horaEntrada);
			int tiempoTotal = tiempoActual - tiempoEntrada;

			tiempoTotalFichaje = obtenerFechaStringDesdeInt(tiempoTotal);

		//Si ya se ha fichado tanto la entrada como la salida
		} else if (horaEntrada != null || !"".equals(horaEntrada) && horaSalida != null || !"".equals(horaSalida)) {
			
			int tiempoEntrada = obtenerHoraEnMinutos(horaEntrada);
			int tiempoSalida = obtenerHoraEnMinutos(horaSalida);
			int tiempoTotal = tiempoSalida - tiempoEntrada;
		
			tiempoTotalFichaje = obtenerFechaStringDesdeInt(tiempoTotal);			
		}
		
		return tiempoTotalFichaje;
		
	}
	
}
