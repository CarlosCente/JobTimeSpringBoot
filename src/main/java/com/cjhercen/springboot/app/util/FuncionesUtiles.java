package com.cjhercen.springboot.app.util;

import javax.servlet.http.HttpServletRequest;

import com.cjhercen.springboot.app.models.entity.Role;
import com.cjhercen.springboot.app.models.entity.Usuario;

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
	
	/**
	 * Método que nos devuelve true si el usuario es admin y false si no lo es
	 * @param usuario usuario que esta conectado a la aplicación
	 * @return true si es admin, false sino
	 */
	public static boolean esAdmin (Usuario usuario) {
		
		boolean esAdmin = false;
		
		for(Role role : usuario.getRoles()) {
			if(role.getAuthority().equals("ROLE_ADMIN")) {
				esAdmin = true;
			}
		}
		
		return esAdmin;
		
	}
	
}
