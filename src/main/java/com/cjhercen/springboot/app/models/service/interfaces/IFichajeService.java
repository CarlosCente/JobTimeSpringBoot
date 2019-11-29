package com.cjhercen.springboot.app.models.service.interfaces;

import java.util.List;

import com.cjhercen.springboot.app.models.entity.Fichaje;

public interface IFichajeService {

	public void save(Fichaje fichaje);

	public Fichaje findOne(Long id);
	
	public List<Fichaje> findAll();

	public void delete(Long id);

	
}
