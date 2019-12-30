package com.cjhercen.springboot.app.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cjhercen.springboot.app.models.object.FormBusqueda;
import com.cjhercen.springboot.app.models.service.impl.EmpleadoServiceImpl;
import com.cjhercen.springboot.app.models.service.impl.FichajeServiceImpl;
import com.cjhercen.springboot.app.util.FechaUtils;

@Controller
public class ControlHorarioController {

	@Autowired
	EmpleadoServiceImpl empleadoService;
	
	@Autowired
	FichajeServiceImpl fichajeService;
	
	FechaUtils fechaUtils = new FechaUtils();
	
	@RequestMapping(value = "/controlhorario", method = RequestMethod.GET)
	public String listar(Model model) {

		FormBusqueda formBusqueda = new FormBusqueda();
		model.addAttribute("formBusqueda", formBusqueda);
		model.addAttribute("titulo", "Búsqueda de fichajes");

		return "controlhorario";
	}

	@RequestMapping(value = "/formBusqueda", method = RequestMethod.POST)
	public String busqueda(@Valid FormBusqueda formBusqueda, BindingResult result, Model model,
			RedirectAttributes flash, SessionStatus status) {

		System.out.println(formBusqueda.getNombre());
		System.out.println(formBusqueda.getApellido1());
		System.out.println(formBusqueda.getApellido2());
		
		if(formBusqueda.getFechaFin()!=null)
		System.out.println(fechaUtils.obtenerFechaParametroEnFormatoCadena(formBusqueda.getFechaInicio()));
		
		if(formBusqueda.getFechaFin()!=null)
		System.out.println(fechaUtils.obtenerFechaParametroEnFormatoCadena(formBusqueda.getFechaFin()));

		return "redirect:controlhorario";
	}
		
	
}
