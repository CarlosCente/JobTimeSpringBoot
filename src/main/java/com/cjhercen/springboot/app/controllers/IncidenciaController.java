package com.cjhercen.springboot.app.controllers;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cjhercen.springboot.app.models.entity.Incidencia;
import com.cjhercen.springboot.app.models.service.interfaces.IIncidenciaService;

@Controller
public class IncidenciaController {

	@Autowired
	IIncidenciaService incidenciaService;
	
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
