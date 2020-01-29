package com.cjhercen.springboot.app.models.service.interfaces;

import java.util.List;

import com.cjhercen.springboot.app.models.entity.Empleado;

public interface IEmpleadoService {
	
	public List<Empleado> findAll();
	
	public void save(Empleado empleado);
	
	public Empleado findOne(Long id);
	
	public void delete(Long id);
	
	
}
