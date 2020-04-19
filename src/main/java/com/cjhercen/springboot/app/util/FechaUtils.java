package com.cjhercen.springboot.app.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilidades para trabajar con las fechas y horas de los fichajes
 * 
 * @author cjhercen
 * @version 03/12/2019
 */
public class FechaUtils {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	public FechaUtils() {

	}

	/**
	 * Obtener la hora en formato hh:mm como un String
	 * 
	 * @return hora en el formato hh:mm
	 */
	public String obtenerHoraEnFormatoCadena() {

		Calendar calendario = Calendar.getInstance();
		int hora, minutos;
		hora = calendario.get(Calendar.HOUR_OF_DAY);
		minutos = calendario.get(Calendar.MINUTE);
		String minutosString = "";

		if (minutos < 10) {
			minutosString = "0" + minutos;
			if (minutos == 0) {
				minutosString = "00";
			}
		} else {
			minutosString = String.valueOf(minutos);
		}

		return hora + ":" + minutosString;
	}

	/**
	 * Obtener la hora en formato dd-MM-yyyy como un String
	 * 
	 * @return hora en el formato dd-MM-yyyy
	 */
	public String obtenerFechaEnFormatoCadena() {

		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		return dateFormat.format(date);

	}
	
	/**
	 * Obtener la hora en formato dd-MM-yyyy como un String
	 * 
	 * @param fecha fecha que se quiere transformar en cadena al formato dd-MM-yyyy
	 * @return hora en el formato dd-MM-yyyy
	 */
	public String obtenerFechaParametroEnFormatoCadena(Date fecha) {

		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		return dateFormat.format(fecha);

	}
	
	/**
	 * Obtener la hora en formato yyyy-MM-dd como un String
	 * 
	 * @param fecha fecha que se quiere transformar en cadena al formato yyyy-MM-dd
	 * @return hora en el formato yyyy-MM-dd
	 */
	public String obtenerFechaParametroEnFormatoSQL(Date fecha) {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		return dateFormat.format(fecha);

	}

	/**
	 * Obtener la fecha actual en formato dd-MM-yyyy como un Date
	 * 
	 * @return hora en el formato dd-MM-yyyy
	 */
	public Date obtenerFechaActual() {

		Date date = new Date();
		return date;
	}
	
	/**
	 * Obtener una fecha de tipo Date a partir de una fecha String
	 * @param fecha fecha String a transformar
	 * @return Date fecha en tipo Date
	 */
	public Date obtenerFechaApartirString(String fecha) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(fecha);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return date;
	}
	
	
	/**
	 * Obtener una fecha de tipo Date a partir de una fecha String dd/MM/yyyy
	 * @param fecha fecha String a transformar
	 * @return Date fecha en tipo Date
	 */
	public Date obtenerFechaApartirStringFormato2(String fecha) {
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		Date journeyDate = null;
		try {
			journeyDate = df.parse(fecha);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return journeyDate;
	}
	
	/**
	 * Obtener una fecha de tipo Date a partir de una fecha String dd-MM-yyyy
	 * @param fecha fecha String a transformar
	 * @return Date fecha en tipo Date
	 */
	public Date obtenerFechaApartirStringFormato3(String fecha) {
		
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
		Date journeyDate = null;
		try {
			journeyDate = df.parse(fecha);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return journeyDate;
	}

	/**
	 * Método que devuelve los minutos totales de una hora que se pase como
	 * parámetro
	 * 
	 * @param hora que deseamos pasar a minutos
	 * @return minutos totales
	 */
	public int obtenerHoraEnMinutos(String hora) {

		int minutosTotales = 0;

		String[] partes = hora.split(":");
		String horas = partes[0].trim();
		String minutos = partes[1].trim();
		minutosTotales = Integer.parseInt(horas) * 60 + Integer.parseInt(minutos);

		return minutosTotales;

	}

	/**
	 * Método que devuelve la hora en formato hh:mm como un string
	 * 
	 * @param minutosTotales minutos totales de la hora indicada
	 * @return hora transformada a String con formato hh:mm
	 */
	public String obtenerFechaStringDesdeInt(int minutosTotales) {
		String result = "";
		int horas = minutosTotales / 60;
		int minutos = minutosTotales % 60;

		result = String.valueOf(horas) + " : " + String.valueOf(minutos);

		return result;
	}

	/**
	 * Método para obtener el tiempo transcurrido entre los fichajes, dependiendo si
	 * se han fichado los dos o sólo entrada
	 * 
	 * @param horaEntrada hora que se ha fichado al entrar
	 * @param horaSalida  hora que se ha fichado al salir
	 * @return total del tiempo transcurrido de trabajo
	 */
	public String obtenerTiempoTranscurrido(String horaEntrada, String horaSalida) {

		String tiempoTotalFichaje = "";

		// Si solo se ha fichado la entrada
		if ((horaEntrada != null && !"".equals(horaEntrada)) && (horaSalida == null || "".equals(horaSalida))) {

			String horaActual = obtenerHoraEnFormatoCadena();
			int tiempoActual = obtenerHoraEnMinutos(horaActual);
			int tiempoEntrada = obtenerHoraEnMinutos(horaEntrada);
			int tiempoTotal = tiempoActual - tiempoEntrada;

			tiempoTotalFichaje = obtenerFechaStringDesdeInt(tiempoTotal);

			// Si ya se ha fichado tanto la entrada como la salida
		} else if (horaEntrada != null && !"".equals(horaEntrada) && horaSalida != null || !"".equals(horaSalida)) {

			int tiempoEntrada = obtenerHoraEnMinutos(horaEntrada);
			int tiempoSalida = obtenerHoraEnMinutos(horaSalida);
			int tiempoTotal = tiempoSalida - tiempoEntrada;

			tiempoTotalFichaje = obtenerFechaStringDesdeInt(tiempoTotal);
		}

		return tiempoTotalFichaje;

	}

	public String formatearFechas2digitos(String tiempoTotalFichaje) {
		
		if(!"".equals(tiempoTotalFichaje)) {
			
			String[] partes = tiempoTotalFichaje.split(":");
			String horas = partes[0].trim();
			String minutos = partes[1].trim();
	
			if (horas.length() == 1) {
				horas = "0" + horas;
			}
	
			if (minutos.length() == 1) {
				minutos = "0" + minutos;
			}
			
			return horas + ":" + minutos;
			
		}else {
			return "00:00";
		}
	}

	/*
	 * Método para obtener el numero de la semana dentro del año, para añadir en la BBDD junto al fichaje
	 */
	public int obtenerSemana(Date fecha) {
		int semana = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		semana = calendar.get(Calendar.WEEK_OF_YEAR);
		return semana;
	}
	
	/*
	 * Método para comprobar si el dia de la semana es domingo
	 */
	public Boolean esDomingo(Date fecha) {
		Boolean esDomingo = false;
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);
		
		if(diaSemana == 1) {
			esDomingo = true;
			log.info("Hoy es domingo, el usuario no puede fichar");
		}
		return esDomingo;
	}
	
	/*
	 * Metodo para comprobar si el dia es laboral (que no sea sabado ni domingo)
	 */
	public Boolean esFinDeSemana(Date fecha) {
		Boolean esFinDeSemana = false;
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);
		
		if(diaSemana ==1 || diaSemana == 7) {
			esFinDeSemana = true;
			log.info("Es fin de semana, no cuenta para el permiso o solicitud");
		}
		
		return esFinDeSemana;
	}
	
	
	public int obtenerMesActual(Date fecha) {
		int semana = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		semana = calendar.get(Calendar.MONTH)+1;
		return semana;
	}
	
	/*
	 * Método que suma x dias a la fecha que se pase como parámetro
	 */
	public Date sumarDias(Date fecha, int dias) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.DAY_OF_YEAR, dias);
		return calendar.getTime();
		
	}
	
}
