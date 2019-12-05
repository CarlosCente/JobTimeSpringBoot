package com.cjhercen.springboot.app.models.service.impl;

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
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

}
