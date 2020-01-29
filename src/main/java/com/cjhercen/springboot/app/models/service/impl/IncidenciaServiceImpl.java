package com.cjhercen.springboot.app.models.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cjhercen.springboot.app.models.dao.IIncidenciaDao;
import com.cjhercen.springboot.app.models.entity.Empleado;
import com.cjhercen.springboot.app.models.entity.Incidencia;
import com.cjhercen.springboot.app.models.service.interfaces.IIncidenciaService;

@Service
public class IncidenciaServiceImpl implements IIncidenciaService {

	@Autowired
	IIncidenciaDao incidenciaDao;
	
	@Override
	public List<Incidencia> findByEmpleado(Empleado empleado) {
		return (List<Incidencia>)incidenciaDao.findByEmpleado(empleado);
	}

	@Override
	public Incidencia findByEmpleadoAndFecha(Empleado empleado, Date fecha) {
		return incidenciaDao.findByEmpleadoAndFecha(empleado, fecha);
	}
	
	@Override
	public Incidencia findByEmpleadoAndFechaAndMensaje(Empleado empleado, Date fecha, String mensaje) {
		return incidenciaDao.findByEmpleadoAndFechaAndMensaje(empleado, fecha, mensaje);
	}

	@Override
	public List<Incidencia> findAll() {
		return (List<Incidencia>) incidenciaDao.findAll();
	}

	@Override
	@Transactional
	public void save(Incidencia incidencia) {
		incidenciaDao.save(incidencia);	
	}

	@Override
	public void delete(Incidencia incidencia) {
		incidenciaDao.delete(incidencia);
	}


}
