package com.cjhercen.springboot.app.models.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cjhercen.springboot.app.models.dao.IFichajeDao;
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
	@Transactional(readOnly = true)
	public Fichaje findOne(Long id) {
		return fichajeDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Fichaje> findAll() {
		// TODO Auto-generated method stub
		return (List<Fichaje>) fichajeDao.findAll();
	}

	@Override
	@Transactional
	public void delete(Long id) {
		fichajeDao.deleteById(id);
	}

}
