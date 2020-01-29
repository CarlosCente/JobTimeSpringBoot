package com.cjhercen.springboot.app.models.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cjhercen.springboot.app.models.dao.IFichajeDao;
import com.cjhercen.springboot.app.models.entity.Empleado;
import com.cjhercen.springboot.app.models.entity.Fichaje;
import com.cjhercen.springboot.app.models.service.interfaces.IFichajeService;

@Service
public class FichajeServiceImpl implements IFichajeService {

	@Autowired
	private IFichajeDao fichajeDao;
	
	@Override
	@Transactional
	public void save(Fichaje fichaje) {
		fichajeDao.save(fichaje);		 
	}

	@Override
	public void delete(Fichaje fichaje) {
		fichajeDao.delete(fichaje);
	}

	@Override
	public List<Fichaje> findByEmpleado(Empleado empleado) {
		return fichajeDao.findByEmpleado(empleado);
	}

	@Override
	public Fichaje findByEmpleadoAndFecha(Empleado empleado, Date fecha) {
		return fichajeDao.findByEmpleadoAndFecha(empleado, fecha);
	}

	@Override
	public ArrayList<Fichaje> findAll() {
		return fichajeDao.findAll();
	}

	@Override
	public List <Fichaje> findByFecha(Date fecha) {
		return fichajeDao.findByFecha(fecha);
	}
	
	

}
