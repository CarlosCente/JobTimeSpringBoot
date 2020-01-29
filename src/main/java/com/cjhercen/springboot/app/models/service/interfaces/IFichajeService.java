package com.cjhercen.springboot.app.models.service.interfaces;

import java.util.Date;
import java.util.List;

import com.cjhercen.springboot.app.models.entity.Empleado;
import com.cjhercen.springboot.app.models.entity.Fichaje;

public interface IFichajeService {

	public void save(Fichaje fichaje);
	
	public void delete(Fichaje fichaje);

	public List<Fichaje> findByEmpleado(Empleado empleado);
	
	public Fichaje findByEmpleadoAndFecha(Empleado empleado, Date fecha);
	
	public List <Fichaje> findByFecha(Date fecha);
	
	public List <Fichaje> findAll();
	
}
