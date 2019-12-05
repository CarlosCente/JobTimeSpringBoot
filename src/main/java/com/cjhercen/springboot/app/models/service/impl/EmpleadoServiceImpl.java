package com.cjhercen.springboot.app.models.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cjhercen.springboot.app.models.dao.IEmpleadoDao;
import com.cjhercen.springboot.app.models.entity.Empleado;
import com.cjhercen.springboot.app.models.service.interfaces.IEmpleadoService;

@Service
public class EmpleadoServiceImpl implements IEmpleadoService {

	@Autowired
	private IEmpleadoDao empleadoDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Empleado> findAll() {
		// TODO Auto-generated method stub
		return (List<Empleado>) empleadoDao.findAll();
	}

	@Override
	@Transactional
	public void save(Empleado empleado) {
		empleadoDao.save(empleado);
		
	}

	@Override
	@Transactional(readOnly = true)
	public Empleado findOne(Long id) {
		// TODO Auto-generated method stub
		return empleadoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		empleadoDao.deleteById(id);
		
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Empleado> findAll(Pageable pageable) {
		return empleadoDao.findAll(pageable);
	}
}
