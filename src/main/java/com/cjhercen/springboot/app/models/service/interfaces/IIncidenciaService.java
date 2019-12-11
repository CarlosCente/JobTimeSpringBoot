package com.cjhercen.springboot.app.models.service.interfaces;

import java.util.Date;
import java.util.List;

import com.cjhercen.springboot.app.models.entity.Empleado;
import com.cjhercen.springboot.app.models.entity.Incidencia;

public interface IIncidenciaService {

	public List<Incidencia> findByEmpleado(Empleado empleado);
	
	public Incidencia findByEmpleadoAndFecha(Empleado empleado, Date fecha);
	
	public List<Incidencia> findAll();
	
}
