package com.cjhercen.springboot.app.models.service.interfaces;

import java.util.Date;
import java.util.List;

import com.cjhercen.springboot.app.models.entity.Empleado;
import com.cjhercen.springboot.app.models.entity.Solicitud;

public interface ISolicitudService {

	public List<Solicitud> findByEmpleado(Empleado empleado);
	
	public Solicitud findByEmpleadoAndFecha(Empleado empleado, Date fecha);
	
	public Solicitud findByEmpleadoAndFechaAndTipo(Empleado empleado, Date fecha, String tipo);
	
	public List<Solicitud> findAll();
	
	public void save(Solicitud solicitud);

	void delete(Solicitud solicitud);
		
	
}
