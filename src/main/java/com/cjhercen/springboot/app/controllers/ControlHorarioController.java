package com.cjhercen.springboot.app.controllers;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cjhercen.springboot.app.models.entity.Empleado;
import com.cjhercen.springboot.app.models.entity.Fichaje;
import com.cjhercen.springboot.app.models.entity.Usuario;
import com.cjhercen.springboot.app.models.object.FormNuevoFichaje;
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
		
		//Objeto para crear fichaje
		FormNuevoFichaje formNuevoFichaje = new FormNuevoFichaje();
		
		//Se obtienen todos los fichajes de todos los empleados
		List<Fichaje> listaFichajes = fichajeService.findAll();
		
		model.addAttribute("formNuevoFichaje", formNuevoFichaje);
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
		flash.addFlashAttribute("tipo", "Información");
		flash.addFlashAttribute("message", "Fichaje borrado con éxito");
		
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
			flash.addFlashAttribute("tipo", "Error");
			flash.addFlashAttribute("message", "No existe el fichaje");
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
	
	
	@RequestMapping(value = "/formFichaje", method = RequestMethod.POST)
	public String guardarFichaje(@Valid FormNuevoFichaje formNuevoFichaje, BindingResult result, Model model,
			RedirectAttributes flash) {

		
		String usuarioString = formNuevoFichaje.getUsuario();
		Usuario usuario = usuarioService.findByUsername(usuarioString);
		
		//Se controla si existe el usuario y el empleado para asociarlo al fichaje
		if(usuario == null) {
			log.info("El usuario " + usuarioString + "no existe");
			flash.addFlashAttribute("tipo", "Error");
			flash.addFlashAttribute("message", "El usuario no existe en el sistema");
			return "redirect:/controlhorario";
		} else {
			if(usuario.getEmpleado() == null) {
				log.info("El empleado no existe");
				flash.addFlashAttribute("tipo", "Error");
				flash.addFlashAttribute("message", "El usuario no existe o no está asociado a un empleado");
				return "redirect:/controlhorario";
			}
		}
				
		//Se comprueba si la fecha es correcta
		Date fechaFichaje = formNuevoFichaje.getFecha();
		Date fechaActual = new Date();
		if(fechaFichaje == null) {
			log.info("La fecha no es correcta");
			flash.addFlashAttribute("tipo", "Error");
			flash.addFlashAttribute("message", "La fecha no es correcta o no se ha introducido un valor");
			return "redirect:/controlhorario";
		} else if(fechaFichaje.after(fechaActual)) {
			//Compruebo que no se haya seleccinado una fecha futura, tiene que ser igual o anterior a la actual
			log.info("La fecha no puede ser posterior a la fecha actual");
			flash.addFlashAttribute("tipo", "Error");
			flash.addFlashAttribute("message", "La fecha no puede ser posterior a la fecha actual");
			return "redirect:/controlhorario";
		}
		
		Empleado empleado = usuario.getEmpleado();
		
		Fichaje fichajeComprueba = fichajeService.findByEmpleadoAndFecha(empleado, fechaFichaje);
		if(fichajeComprueba == null) {
			//Se crea el usuario asociado al empleado que se acaba de crear
			
			Fichaje fichaje = new Fichaje();
			fichaje.setEmpleado(empleado);
			//Establecer valores de hora de entrada y salida en caso de que vengan informados
			fichaje.setFecha(fechaFichaje);
			String horaEntrada = formNuevoFichaje.getHoraEntrada();
			String horaSalida = formNuevoFichaje.getHoraSalida();
			
			if(horaEntrada != null) {
				if(horaEntrada.length() > 0) {
					fichaje.setHoraEntrada(horaEntrada);
				}
			} else {
				fichaje.setHoraEntrada(null);
			}
			
			if(horaSalida != null) {
				if(horaSalida.length() > 0) {
					fichaje.setHoraSalida(horaSalida);
				}
			} else {
				fichaje.setHoraSalida(null);
			}

			//El valor de la ip definido como "creado por admin"
			fichaje.setIp("Creado por admin");
			
			//Establecer el valor del tiempo total
			String totalTiempo = fechaUtils.obtenerTiempoTranscurrido(fichaje.getHoraEntrada(), fichaje.getHoraSalida());
			fichaje.setTiempoTotal(fechaUtils.formatearFechas2digitos(totalTiempo));
			
			fichajeService.save(fichaje);
			log.info("Se ha creado correctamente el fichaje " + fichaje.toString());
			flash.addFlashAttribute("tipo", "Información");
			flash.addFlashAttribute("message", "El Fichaje se ha creado correctamente");
			return "redirect:controlhorario";
		
		} else {
			log.info("El fichaje ya existe ");
			flash.addFlashAttribute("tipo", "Error");
			flash.addFlashAttribute("message", "El Fichaje ya existe");
			return "redirect:/controlhorario";
		}
		
		
	}

	
	
}
