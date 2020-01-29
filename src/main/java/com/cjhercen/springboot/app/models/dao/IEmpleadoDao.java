package com.cjhercen.springboot.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.cjhercen.springboot.app.models.entity.Empleado;

public interface IEmpleadoDao extends CrudRepository<Empleado, Long>{


}
