package com.cjhercen.springboot.app.models.dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.cjhercen.springboot.app.models.entity.Empleado;
import com.cjhercen.springboot.app.models.entity.Fichaje;
import com.cjhercen.springboot.app.models.entity.FichajeId;

@Transactional
public interface IFichajeDao extends CrudRepository<Fichaje, FichajeId> {

	public List<Fichaje> findByEmpleado(Empleado empleado);
	
	public Fichaje findByEmpleadoAndFecha(Empleado empleado, Date fecha);

	
}
