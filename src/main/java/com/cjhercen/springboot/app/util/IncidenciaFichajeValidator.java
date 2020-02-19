package com.cjhercen.springboot.app.util;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cjhercen.springboot.app.models.object.IncidenciaFichaje;

@Component
public class IncidenciaFichajeValidator implements Validator {

	FechaUtils fechaUtils = new FechaUtils();
	
	@Override
	public boolean supports(Class<?> clazz) {
		return IncidenciaFichaje.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		IncidenciaFichaje incidencia = (IncidenciaFichaje) obj;
		
		Date fechaActual = new Date();
		Date fechaEntrada = fechaUtils.obtenerFechaApartirString(incidencia.getFechaHoraEntrada());
		Date fechaSalida = fechaUtils.obtenerFechaApartirString(incidencia.getFechaHoraSalida());

		if(fechaEntrada.before(fechaActual) || fechaSalida.before(fechaActual)) {
			errors.rejectValue("fecha", "La fecha no puede ser posterior a la fecha actual");
		}
		
		
	}

}
