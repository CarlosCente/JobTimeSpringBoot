package com.cjhercen.springboot.app.models.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cjhercen.springboot.app.models.entity.Empleado;
import com.cjhercen.springboot.app.models.entity.Solicitud;
import com.cjhercen.springboot.app.models.entity.SolicitudId;

public interface ISolicitudDao extends CrudRepository <Solicitud, SolicitudId>{

	public List<Solicitud> findByEmpleado(Empleado empleado);
	
	public Solicitud findByEmpleadoAndFecha(Empleado empleado, Date fecha);
	
	public Solicitud findByEmpleadoAndFechaAndTipo(Empleado empleado, Date fecha, String tipo);
	
}
