package com.cjhercen.springboot.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cjhercen.springboot.app.models.entity.Usuario;
import com.cjhercen.springboot.app.models.service.impl.UsuarioServiceImpl;
import com.cjhercen.springboot.app.util.FuncionesUtiles;

@Controller
public class IndexController {

	@Autowired
	UsuarioServiceImpl usuarioService;
	
	/*
	 * Metodo que controla la página de inicio según el tipo de usuario, si es admin empezará
	 * con la página de Incidencias una vez que haya hecho login y si es usuario normal, comenzará
	 * directamente en la pagina de Fichaje nada mas se haya logueado
	 * @return pagina a la que se dirige
	 */
	@RequestMapping(value = {"/","/index"})
	public String inicio() {
	
		//Se obtiene primero el usuario conectado para obtener los datos del empleado
		String username = usuarioService.getUsername();
		Usuario usuario = usuarioService.findByUsername(username);

		if(FuncionesUtiles.esAdmin(usuario)) {
			return "redirect:/incidencias";
		} else {
			return "redirect:/fichar";
		}
	}
	
}
