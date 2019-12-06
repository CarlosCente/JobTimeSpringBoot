package com.cjhercen.springboot.app.util;

import javax.servlet.http.HttpServletRequest;

public class FuncionesUtiles {

	/**
	 * Función que devuelte la IP del cliente que está accediendo y en caso de ser en local la transforma a 
	 * 127.0.0.1
	 * @param request con la petición
	 * @return ip devuelve la IP del cliente
	 */
	public static String obtenerIp( HttpServletRequest request) {
		
		String ip = "";
		ip = request.getRemoteAddr();
		
		if("0:0:0:0:0:0:0:1".equals(ip)) {
			ip = "127.0.0.1";
		}
		
		return ip;
		
	}
	
}
