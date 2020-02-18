package com.cjhercen.springboot.app.models.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cjhercen.springboot.app.models.dao.ISolicitudDao;
import com.cjhercen.springboot.app.models.entity.Empleado;
import com.cjhercen.springboot.app.models.entity.Solicitud;
import com.cjhercen.springboot.app.models.service.interfaces.ISolicitudService;

@Service
public class SolicitudServiceImpl implements ISolicitudService{

	@Autowired
	ISolicitudDao solicitudDao;
	
	@Override
	public List<Solicitud> findByEmpleado(Empleado empleado) {
		return (List<Solicitud>) solicitudDao.findByEmpleado(empleado);
	}

	@Override
	public Solicitud findByEmpleadoAndFecha(Empleado empleado, Date fecha) {
		return solicitudDao.findByEmpleadoAndFecha(empleado, fecha);
	}

	@Override
	public Solicitud findByEmpleadoAndFechaAndTipo(Empleado empleado, Date fecha, String tipo) {
		return solicitudDao.findByEmpleadoAndFechaAndTipo(empleado, fecha, tipo);
	}

	@Override
	public List<Solicitud> findAll() {
		return (List<Solicitud>) solicitudDao.findAll();
	}

	@Override
	public void save(Solicitud solicitud) {
		solicitudDao.save(solicitud);		
	}

	@Override
	public void delete(Solicitud solicitud) {
		solicitudDao.delete(solicitud);
	}

}
