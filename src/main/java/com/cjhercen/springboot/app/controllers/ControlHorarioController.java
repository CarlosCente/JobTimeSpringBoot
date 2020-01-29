package com.cjhercen.springboot.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cjhercen.springboot.app.models.entity.Fichaje;
import com.cjhercen.springboot.app.models.service.impl.EmpleadoServiceImpl;
import com.cjhercen.springboot.app.models.service.interfaces.IFichajeService;
import com.cjhercen.springboot.app.util.FechaUtils;

@Controller
public class ControlHorarioController {

	@Autowired
	EmpleadoServiceImpl empleadoService;
	
	@Autowired
	IFichajeService fichajeService;
	
	FechaUtils fechaUtils = new FechaUtils();
	
	@RequestMapping(value = "/controlhorario", method = RequestMethod.GET)
	public String listar(Model model) {

		//Se obtienen todos los fichajes de todos los empleados
		List<Fichaje> listaFichajes = fichajeService.findAll();
		
		model.addAttribute("listaFichajes", listaFichajes);
		model.addAttribute("titulo", "Búsqueda de fichajes");

		return "controlhorario";
	}

	
	
}
