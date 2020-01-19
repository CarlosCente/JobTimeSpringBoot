package com.cjhercen.springboot.app.controllers;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cjhercen.springboot.app.models.entity.Empleado;
import com.cjhercen.springboot.app.models.entity.Incidencia;
import com.cjhercen.springboot.app.models.entity.Usuario;
import com.cjhercen.springboot.app.models.object.IncidenciaDatosPersonales;
import com.cjhercen.springboot.app.models.service.impl.IncidenciaServiceImpl;
import com.cjhercen.springboot.app.models.service.impl.UsuarioServiceImpl;
import com.cjhercen.springboot.app.models.service.interfaces.IEmpleadoService;
import com.cjhercen.springboot.app.util.ConstantesUtils;
import com.cjhercen.springboot.app.util.FechaUtils;

@Controller
public class PerfilController implements ConstantesUtils {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IEmpleadoService empleadoService;
	
	@Autowired
	private UsuarioServiceImpl usuarioService;
	
	@Autowired
	private IncidenciaServiceImpl incidenciaService;
	
	FechaUtils fechaUtils = new FechaUtils();
	
	@GetMapping(value = "/perfil")
	public String editar(Map<String, Object> model, RedirectAttributes flash) {

		//Se obtiene primero el usuario conectado para obtener los datos del empleado
		String username = usuarioService.getUsername();
		Usuario usuario = usuarioService.findByUsername(username);
		Empleado empleado = usuario.getEmpleado();		

		//Introduzco en el modelo el objeto para poder crear las incidencias
		IncidenciaDatosPersonales incidenciaDatosPersonales = new IncidenciaDatosPersonales();	
		model.put("incidenciaDatosPersonales", incidenciaDatosPersonales);
		
		model.put("empleado", empleado);
		model.put("titulo", "Pefil del empleado");
		return "perfil";
	}
	
	@RequestMapping(value = "/perfil/editar/{id}", method = RequestMethod.POST)
	public String saveEmpleado(@PathVariable(value = "id") Long id, @ModelAttribute("empleado") Empleado empleado,
		 RedirectAttributes flash) {
		Empleado empleadoBD = null;
		
		//Se obtiene primero el usuario conectado para obtener los datos del empleado
		String username = usuarioService.getUsername();
		Usuario usuario = usuarioService.findByUsername(username);
		empleadoBD = usuario.getEmpleado();		

		if (empleadoBD != null) {
			
			String descripcionInci = generarDescripcionAdvertencia(empleadoBD, empleado);

			empleadoBD.setDireccion(empleado.getDireccion());
			empleadoBD.setLocalidad(empleado.getLocalidad());
			empleadoBD.setPais(empleado.getPais());
			empleadoBD.setProvincia(empleado.getProvincia());
			
			//Se genera la advertencia para el administrador (empleadoBDCopia guarda los datos que habia
			// anteriormente en la BBDD antes de hacer la modificacion)
			generarIncidenciaAdvertencia(empleadoBD, empleado, descripcionInci);
			
			//Se guardan las modificaciones
			empleadoService.save(empleadoBD);

			flash.addFlashAttribute("success", "Has editado tu información correctamente");

		} else {
			flash.addFlashAttribute("error", "Error, no se encuentra al empleado");
		}

		return "redirect:/perfil";
	}
	
	@RequestMapping(value = "/perfil/incidencia", method = RequestMethod.POST)
	public String saveIncidencia(@ModelAttribute("incidenciaDatosPersonales") IncidenciaDatosPersonales incidenciaDatosPersonales,
			BindingResult bindingResult, RedirectAttributes flash) {
	
		/*
		 * Se controla que no vaya la incidencia completamente vacía
		 */
		if(!incidenciaDatosPersonales.isHayNombre() && !incidenciaDatosPersonales.isHayApellido1() && 
				!incidenciaDatosPersonales.isHayApellido2() && !incidenciaDatosPersonales.isHayFechaNacimiento()) {
			
			flash.addFlashAttribute("error", "Error, no se puede crear una incidencia vacía");
			return "redirect:/perfil";
			
		}
		
		Empleado empleadoBD = null;
		
		//Se obtiene primero el usuario conectado para obtener los datos del empleado
		String username = usuarioService.getUsername();
		Usuario usuario = usuarioService.findByUsername(username);
		empleadoBD = usuario.getEmpleado();		

		if (empleadoBD != null) {
	
		/*
		 * Si no ocurre ningún error con el empleado...
		 * Se crea la incidencia con estado abierta y con tipo ERROR para el empleado que se encuentra conectado
		 */

			Incidencia incidenciaNueva = new Incidencia();
			incidenciaNueva.setEmpleado(empleadoBD);
			incidenciaNueva.setTipo(INCIDENCIA_ERROR);
			incidenciaNueva.setEstado(INCIDENCIA_ABIERTA);
			incidenciaNueva.setFecha(fechaUtils.obtenerFechaActual());
			incidenciaNueva.setMensaje(INCIDENCIA_PERFIL);
			incidenciaNueva.setDescripcion(generarDescripcion(empleadoBD, incidenciaDatosPersonales));
					
			incidenciaService.save(incidenciaNueva);

			log.info("Se ha generado una incidencia de tipo '"+ INCIDENCIA_PERFIL +"' al querer modificar "
					+ "el empleado sus datos personales del perfil.");
			
			flash.addFlashAttribute("success", "Incidencia añadida correctamente");

		} else {
			flash.addFlashAttribute("error", "Error, no se ha podido crear la incidencia");
		}
		
		return "redirect:/perfil";
	}
	
	/**
	 * Método que genera la descripción de la incidencia según del tipo que sea y de los datos
	 * @param empleado empleado que crea la incidencia (usuario conectado)
	 * @param incidenciaDatosPersonales datos pasados en el modal
	 * @return String con la descripcion de la incidencia
	 */
	public String generarDescripcion(Empleado empleado, IncidenciaDatosPersonales incidenciaDatosPersonales) {
		
		String mensaje = "";
		String anadirNombre = "";
		String anadirApellido1 = "";
		String anadirApellido2 = "";
		String anadirFecha = "";
		
		
		if(incidenciaDatosPersonales.isHayNombre()) {
			anadirNombre = " Nombre, el valor correcto sería "+incidenciaDatosPersonales.getNombre();
		}
		
		if(incidenciaDatosPersonales.isHayApellido1()) {
			if(!"".equals(anadirNombre)){
				anadirApellido1 = " , también en el campo Primer Apellido, el valor correcto sería "+incidenciaDatosPersonales.getApellido1();
			} else {
				anadirApellido1 = " Primer Apellido, el valor correcto sería "+incidenciaDatosPersonales.getApellido1();
			}
		}
		
		if(incidenciaDatosPersonales.isHayApellido2()) {
			if(!"".equals(anadirApellido1) || !"".equals(anadirNombre)) {
				anadirApellido2 = " , también en el campo Segundo Apellido, el valor correcto sería "+incidenciaDatosPersonales.getApellido2();
			} else {
				anadirApellido2 = " Segundo Apellido, el valor correcto sería "+incidenciaDatosPersonales.getApellido2();
			}
		}
		
		if(incidenciaDatosPersonales.isHayFechaNacimiento()) {
			if(!"".equals(anadirApellido1) || !"".equals(anadirNombre) || !"".equals(anadirApellido2)) {
				anadirFecha = " , también en el campo Fecha de Nacimiento, el valor correcto sería "+ fechaUtils.obtenerFechaParametroEnFormatoCadena(incidenciaDatosPersonales.getFechaNacimiento());
			}else {
				anadirFecha = " Fecha de Nacimiento, el valor correcto sería "+fechaUtils.obtenerFechaParametroEnFormatoCadena(incidenciaDatosPersonales.getFechaNacimiento());
			}
		}
		
		mensaje = "El usuario "+empleado.getUsuario().getUsername() +" tiene un error en sus datos personales en el campo "
				+ anadirNombre + anadirApellido1 + anadirApellido2 + anadirFecha;
		
		return mensaje;
	}
	
	/**
	 * Método que genera una incidencia de tipo advertencia para indicar que ha modificado sus datos
	 * de direccion al administrador. Se comparan los datos de la BD con los que se pasan en el formulario
	 * @param empleadoBD datos del empleado en la BBDD
	 * @param empleadoForm datos del empleado en el formulario
	 */
	private void generarIncidenciaAdvertencia(Empleado empleadoBD, Empleado empleadoForm, String descripcion) {
		
		Incidencia incidenciaNueva = new Incidencia();
		incidenciaNueva.setEmpleado(empleadoBD);
		incidenciaNueva.setTipo(INCIDENCIA_ADVERTENCIA);
		incidenciaNueva.setEstado(INCIDENCIA_ABIERTA);
		incidenciaNueva.setFecha(fechaUtils.obtenerFechaActual());
		incidenciaNueva.setMensaje(INCIDENCIA_DATOS_DIRECCION);
		incidenciaNueva.setDescripcion(descripcion);
				
		incidenciaService.save(incidenciaNueva);

		log.info("Se ha generado una incidencia de tipo '"+ INCIDENCIA_DATOS_DIRECCION +"' al modificar "
				+ "el empleado sus datos de dirección");
		
	}
	
	private String generarDescripcionAdvertencia(Empleado empleadoBD, Empleado empleadoForm) {
		
		String anadirDireccion = "";
		String anadirLocalidad = "";
		String anadirPais = "";
		String anadirProvincia = "";
		String separador = ", ";
		
		String mensaje = "El usuario " + empleadoBD.getUsuario().getUsername() + " ("+ empleadoBD.getNombre() +
				" " + empleadoBD.getApellido1() + " " + empleadoBD.getApellido2() + "), ha modificado los siguientes"
					+ " datos de dirección: ";
		
		if(!empleadoBD.getDireccion().equals(empleadoForm.getDireccion())) {
			anadirDireccion = " el campo 'Direccion', el nuevo valor es: " + empleadoForm.getDireccion();
		}
		
		if(!empleadoBD.getLocalidad().equals(empleadoForm.getLocalidad())) {
			anadirLocalidad = " el campo 'Localidad', el nuevo valor es: " + empleadoForm.getLocalidad();
		}
		
		if(!empleadoBD.getPais().equals(empleadoForm.getPais())) {
			anadirPais = " el campo 'Pais', el nuevo valor es: " + empleadoForm.getPais();
		}
		
		if(!empleadoBD.getProvincia().equals(empleadoForm.getProvincia())) {
			anadirProvincia = " el campo 'Provincia', el nuevo valor es: " + empleadoForm.getProvincia();
		}
		
		if(!"".equals(anadirDireccion)) {
			mensaje += anadirDireccion;
		}
		
		if(!"".equals(anadirLocalidad)) {
			if(!"".equals(anadirDireccion)){
				mensaje += separador;
				mensaje += anadirLocalidad;
			} else {
				mensaje += anadirLocalidad;
			}
			
		}
		
		if(!"".equals(anadirPais)) {
			if(!"".equals(anadirDireccion) || !"".equals(anadirLocalidad)){
				mensaje += separador;
				mensaje += anadirPais;
			} else {
				mensaje += anadirPais;
			}
		}
		
		if(!"".equals(anadirProvincia) || !"".equals(anadirLocalidad) || !"".equals(anadirPais)) {
			if(!"".equals(anadirDireccion)){
				mensaje += separador;
				mensaje += anadirProvincia;
			} else {
				mensaje += anadirProvincia;
			}
		}
		
		return mensaje;
	}
	
}
