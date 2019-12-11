package com.cjhercen.springboot.app.models.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cjhercen.springboot.app.models.entity.Empleado;
import com.cjhercen.springboot.app.models.entity.Incidencia;

public interface IIncidenciaDao extends CrudRepository<Incidencia, Long>{

	public List<Incidencia> findByEmpleado(Empleado empleado);
	
	public Incidencia findByEmpleadoAndFecha(Empleado empleado, Date fecha);
	
}
