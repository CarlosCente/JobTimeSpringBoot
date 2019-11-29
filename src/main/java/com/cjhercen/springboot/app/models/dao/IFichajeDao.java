package com.cjhercen.springboot.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.cjhercen.springboot.app.models.entity.Fichaje;

public interface IFichajeDao extends CrudRepository<Fichaje, Long> {

}
