package com.cjhercen.springboot.app.controllers;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cjhercen.springboot.app.models.entity.Empleado;
import com.cjhercen.springboot.app.models.entity.Fichaje;
import com.cjhercen.springboot.app.models.entity.Usuario;
import com.cjhercen.springboot.app.models.service.impl.EmpleadoServiceImpl;
import com.cjhercen.springboot.app.models.service.impl.UsuarioServiceImpl;
import com.cjhercen.springboot.app.models.service.interfaces.IFichajeService;
import com.cjhercen.springboot.app.util.FechaUtils;

@Controller
public class ControlHorarioController {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	EmpleadoServiceImpl empleadoService;
	
	@Autowired
	IFichajeService fichajeService;
	
	@Autowired
	UsuarioServiceImpl usuarioService;
	
	FechaUtils fechaUtils = new FechaUtils();
	
	@RequestMapping(value = "/controlhorario", method = RequestMethod.GET)
	public String listar(Model model) {

		//Se obtienen todos los fichajes de todos los empleados
		List<Fichaje> listaFichajes = fichajeService.findAll();
		
		model.addAttribute("listaFichajes", listaFichajes);
		model.addAttribute("titulo", "Gestión de los fichajes de los empleados");

		return "controlhorario";
	}

	
	@RequestMapping(value = "/controlhorario/eliminar/{cod_empl}/{fecha}")
	public String eliminarIncidencia(@PathVariable(value = "cod_empl") Long cod_empl ,
			@PathVariable(value = "fecha") String fecha,
			RedirectAttributes flash) {

		Date fechaFichaje = fechaUtils.obtenerFechaApartirString(fecha);
		Empleado empleado = empleadoService.findOne(cod_empl);
		Fichaje fichajeABorrar = fichajeService.findByEmpleadoAndFecha(empleado, fechaFichaje);
		
		fichajeService.delete(fichajeABorrar);

		log.info("Se ha borrado correctamente el fichaje con fecha " + fichajeABorrar.getFecha() + " del empleado: "
				+ fichajeABorrar.getEmpleado().getNombre() + " " + fichajeABorrar.getEmpleado().getApellido1() + 
				" " + fichajeABorrar.getEmpleado().getApellido2());
		
		flash.addFlashAttribute("success", "Fichaje borrado con éxito");
		
		return "redirect:/controlhorario";
	}
	
	
	@RequestMapping(value = "/controlhorario/editarFichaje" , method = RequestMethod.GET)
	public String editarFichaje(@RequestParam(value = "username") String username ,
			@RequestParam(value = "fecha") String fecha,
			@RequestParam(value = "ipOrigen") String ipOrigen,
			@RequestParam(value = "horaDeEntrada") String horaDeEntrada,
			@RequestParam(value = "horaDeSalida") String horaDeSalida,
			Model model, RedirectAttributes flash) {
		
		Usuario usuario = usuarioService.findByUsername(username);
		Empleado empleado = usuario.getEmpleado();
		Date fechaFichaje = fechaUtils.obtenerFechaApartirString(fecha);
		Fichaje fichajeModif = fichajeService.findByEmpleadoAndFecha(empleado, fechaFichaje);
		
		if(fichajeModif == null) {
			log.error("No existe el fichaje");
		} else {
			
			//Solo se puede modificar la IP, la hora de entrada y la hora de salida
			
			if(!ipOrigen.equals(fichajeModif.getIp())) {
				fichajeModif.setIp(ipOrigen);
			}
			
			if(!horaDeEntrada.equals(fichajeModif.getHoraEntrada())) {
				fichajeModif.setHoraEntrada(horaDeEntrada);
				//Actualizar tiempo total
				
				String totalTiempo = fechaUtils.obtenerTiempoTranscurrido(fichajeModif.getHoraEntrada(), fichajeModif.getHoraSalida());
				fichajeModif.setTiempoTotal(fechaUtils.formatearFechas2digitos(totalTiempo));
				fichajeService.save(fichajeModif);
			}
			
			if(!horaDeSalida.equals(fichajeModif.getHoraSalida())) {
				fichajeModif.setHoraSalida(horaDeSalida);
				
				//Actualizar tiempo total
				String totalTiempo = fechaUtils.obtenerTiempoTranscurrido(fichajeModif.getHoraEntrada(), fichajeModif.getHoraSalida());
				fichajeModif.setTiempoTotal(fechaUtils.formatearFechas2digitos(totalTiempo));
				fichajeService.save(fichajeModif);
			}
			
			fichajeService.save(fichajeModif);
			
		}
		
		return "redirect:/controlhorario";
	}
	
	
}
