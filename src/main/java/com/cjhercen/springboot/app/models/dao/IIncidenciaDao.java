package com.cjhercen.springboot.app.models.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cjhercen.springboot.app.models.entity.Empleado;
import com.cjhercen.springboot.app.models.entity.Incidencia;
import com.cjhercen.springboot.app.models.entity.IncidenciaId;

public interface IIncidenciaDao extends CrudRepository<Incidencia, IncidenciaId>{

	public List<Incidencia> findByEmpleado(Empleado empleado);
	
	public Incidencia findByEmpleadoAndFecha(Empleado empleado, Date fecha);
	
}
