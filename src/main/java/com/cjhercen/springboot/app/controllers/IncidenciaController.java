package com.cjhercen.springboot.app.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cjhercen.springboot.app.models.entity.Empleado;
import com.cjhercen.springboot.app.models.entity.Incidencia;
import com.cjhercen.springboot.app.models.service.interfaces.IEmpleadoService;
import com.cjhercen.springboot.app.models.service.interfaces.IIncidenciaService;
import com.cjhercen.springboot.app.util.FechaUtils;

@Controller
public class IncidenciaController {

	@Autowired
	IIncidenciaService incidenciaService;
	
	@Autowired
	IEmpleadoService empleadoService;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	FechaUtils fechaUtils = new FechaUtils();
	
	@RequestMapping(value = "/incidencias")
	public String crear(Map<String, Object> model) {

		ArrayList<Incidencia> incidenciasPresentes = (ArrayList<Incidencia>) incidenciaService.findAll();
		int totalErrores = calcularIncidenciasErrores(incidenciasPresentes);
		int totalAdvertencias = calcularIncidenciasAdvertencias(incidenciasPresentes);
		int totalSolucionadas = calcularIncidenciasSolucionadas(incidenciasPresentes);
		int totalAbiertas = calcularIncidenciasAbiertas(incidenciasPresentes);
		
		model.put("incidencias", incidenciasPresentes);
		model.put("totalErrores", totalErrores);
		model.put("totalAdvertencias", totalAdvertencias);
		model.put("totalSolucionadas", totalSolucionadas);
		model.put("totalAbiertas", totalAbiertas);
		model.put("titulo", "Incidencias de los empleados");
		return "incidencias";
	}
	
	@RequestMapping(value = "/incidencias/eliminar/{mensaje}/{cod_empl}/{fecha}")
	public String eliminarIncidencia(@PathVariable(value = "mensaje") String mensaje,
			@PathVariable(value = "cod_empl") Long cod_empl ,
			@PathVariable(value = "fecha") String fecha,
			RedirectAttributes flash) {

		Date fechaIncidencia = fechaUtils.obtenerFechaApartirString(fecha);
		Empleado empleado = empleadoService.findOne(cod_empl);
		Incidencia incidenciaABorrar = incidenciaService.
				findByEmpleadoAndFechaAndMensaje(empleado, fechaIncidencia, mensaje);
		
		incidenciaService.delete(incidenciaABorrar);

		log.info("Se ha borrado correctamente la incidencia");
		flash.addFlashAttribute("success", "Incidencia borrada con éxito");
		
		return "redirect:/incidencias";
	}
	
	private int calcularIncidenciasErrores(ArrayList<Incidencia> listaIncidencias) {
		
		int total = 0;
		for(Incidencia inci : listaIncidencias) {
			if(inci.getTipo().equals("1")) {
				total++;
			}
		}
		return total;
	}
	

	private int calcularIncidenciasAdvertencias(ArrayList<Incidencia> listaIncidencias) {
		
		int total = 0;
		for(Incidencia inci : listaIncidencias) {
			if(inci.getTipo().equals("2")) {
				total++;
			}
		}
		return total;
	}
	
	private int calcularIncidenciasSolucionadas(ArrayList<Incidencia> listaIncidencias) {
		
		int total = 0;
		for(Incidencia inci : listaIncidencias) {
			if(inci.getEstado().equals("2")) {
				total++;
			}
		}
		return total;
	}

	private int calcularIncidenciasAbiertas(ArrayList<Incidencia> listaIncidencias) {
		
		int total = 0;
		for(Incidencia inci : listaIncidencias) {
			if(inci.getEstado().equals("1")) {
				total++;
			}
		}
		return total;
	}
	
}
