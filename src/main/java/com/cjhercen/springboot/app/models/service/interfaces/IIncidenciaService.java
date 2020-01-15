package com.cjhercen.springboot.app.models.service.interfaces;

import java.util.Date;
import java.util.List;

import com.cjhercen.springboot.app.models.entity.Empleado;
import com.cjhercen.springboot.app.models.entity.Incidencia;

public interface IIncidenciaService {

	public List<Incidencia> findByEmpleado(Empleado empleado);
	
	public Incidencia findByEmpleadoAndFecha(Empleado empleado, Date fecha);
	
	public Incidencia findByEmpleadoAndFechaAndMensaje(Empleado empleado, Date fecha, String mensaje);
	
	public List<Incidencia> findAll();
	
	public void save(Incidencia incidencia);

	void delete(Incidencia incidencia);
		
}
