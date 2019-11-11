package com.cjhercen.springboot.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cjhercen.springboot.app.models.entity.Empleado;

public interface IEmpleadoService {
	
	public List<Empleado> findAll();

	public Page<Empleado> findAll(Pageable pageable);
	
	public void save(Empleado cliente);
	
	public Empleado findOne(Long id);
	
	public void delete(Long id);
	
}
