package com.cjhercen.springboot.app.models.service.interfaces;

import com.cjhercen.springboot.app.models.entity.Fichaje;

public interface IFichajeService {

	public void save(Fichaje fichaje);
	
	public void delete(Long id);

	
}
