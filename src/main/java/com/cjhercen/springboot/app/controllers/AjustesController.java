package com.cjhercen.springboot.app.controllers;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cjhercen.springboot.app.models.entity.Empleado;
import com.cjhercen.springboot.app.models.entity.Incidencia;
import com.cjhercen.springboot.app.models.entity.Usuario;
import com.cjhercen.springboot.app.models.object.FormCambioPassword;
import com.cjhercen.springboot.app.models.service.impl.UsuarioServiceImpl;
import com.cjhercen.springboot.app.models.service.interfaces.IIncidenciaService;
import com.cjhercen.springboot.app.util.ConstantesUtils;
import com.cjhercen.springboot.app.util.FechaUtils;

@Controller
public class AjustesController {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	UsuarioServiceImpl usuarioService;
	
	@Autowired
	IIncidenciaService incidenciaService;
	

	FechaUtils fechaUtils = new FechaUtils();
	
	@GetMapping("/ajustes")
	public String ajustes(Model model, RedirectAttributes flash) {
		
		FormCambioPassword formCambioPassword = new FormCambioPassword();
		model.addAttribute("formCambioPassword", formCambioPassword);
		
		Usuario usuarioConectado = usuarioService.findByUsername(usuarioService.getUsername());
		Empleado empleado = usuarioConectado.getEmpleado();
		
		// Incidencias que tenga el usuario abiertas
		ArrayList<Incidencia> listaIncidencias = (ArrayList<Incidencia>) incidenciaService.findByEmpleado(empleado);
		ArrayList<Incidencia> incidenciasAbiertas = obtenerAbiertas(listaIncidencias);
		
		model.addAttribute("incidenciasAbiertas", incidenciasAbiertas);
		model.addAttribute("titulo", "Configuraciones disponibles");
		return "ajustes";
	}
	
	@RequestMapping("/ajustes/cambioPassword")
	public String cambioPassword(Model model, RedirectAttributes flash,
			@Valid FormCambioPassword formCambioPassword, BindingResult result) {
		
		Usuario usuarioConectado = usuarioService.findByUsername(usuarioService.getUsername());
		String passwordActual = usuarioConectado.getPassword();
		
		//Se comprueba si existen errores
		if(result.hasErrors()) {
			log.info("Existen errores de validación al intentar cambiar la constraseña");
			return "ajustes";
		}
		
		//Se compara el password introducido como actual para ver si conincide con la contraseña del usuario
		if(passwordEncoder.matches(formCambioPassword.getActual(), passwordActual)) {
			
			//Se comparan las dos contraseñas nuevas para ver si coinciden
			if(formCambioPassword.getNueva().equals(formCambioPassword.getConfirmacion())) {
				String passwordNew = passwordEncoder.encode(formCambioPassword.getNueva());
				usuarioConectado.setPassword(passwordNew);
				usuarioService.save(usuarioConectado);
				//mensaje de exito al cambiar la contraseña
				flash.addFlashAttribute("tipo", "Información");
				flash.addFlashAttribute("message", "La contraseña se ha modificado correctamente");
			} else {
				log.warn("Ha ocurrido un error al intentar modificar la contraseña del usuario " + usuarioConectado.getUsername() +
						" la contraseña nueva y la de confirmación deben coincidir");
				flash.addFlashAttribute("tipo", "Error");
				flash.addFlashAttribute("message", "Las nuevas contraseñas no coinciden");
			}
			
		} else {
			log.warn("Ha ocurrido un error al intentar modificar la contraseña del usuario " + usuarioConectado.getUsername() +
					" La contraseña actual no es correcta.");
			flash.addFlashAttribute("tipo", "Error");
			flash.addFlashAttribute("message", "La contraseña actual no es correcta");
		}
		
		return "redirect:/ajustes";
	}
	
	
	@RequestMapping(value = "/ajustes/eliminar/{mensaje}/{cod_empl}/{fecha}")
	public String eliminarIncidencia(@PathVariable(value = "mensaje") String mensaje,
			@PathVariable(value = "cod_empl") Long cod_empl ,
			@PathVariable(value = "fecha") String fecha,
			RedirectAttributes flash) {

		Date fechaIncidencia = fechaUtils.obtenerFechaApartirString(fecha);
		Usuario usuarioConectado = usuarioService.findByUsername(usuarioService.getUsername());
		Empleado empleado = usuarioConectado.getEmpleado();
		Incidencia incidenciaABorrar = incidenciaService.
				findByEmpleadoAndFechaAndMensaje(empleado, fechaIncidencia, mensaje);
		
		incidenciaService.delete(incidenciaABorrar);

		log.info("Se ha borrado correctamente la incidencia " + incidenciaABorrar.toString());
		flash.addFlashAttribute("tipo", "Información");
		flash.addFlashAttribute("message", "La incidencia se ha eliminado correctamente");

		return "redirect:/ajustes";
	}
	
	/*
	 * Metodo para obtener solo las incidencias del usuario que se encuentren abiertas y que sean errores
	 */
	private ArrayList<Incidencia> obtenerAbiertas(ArrayList<Incidencia> listaIncidencias) {
		ArrayList<Incidencia> listaAbiertas = new ArrayList<Incidencia>();
		
		//Se recorren las incidencias añadiendo a la lista solo las que se encuentren abiertas
		for(Incidencia incidencia : listaIncidencias) {
			if(incidencia.getEstado().equals(ConstantesUtils.INCIDENCIA_ABIERTA) && incidencia.getTipo().
					equals(ConstantesUtils.INCIDENCIA_ERROR)) {
				listaAbiertas.add(incidencia);
			}
		}
		
		return listaAbiertas;
	}

	
}
