package com.cjhercen.springboot.app.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cjhercen.springboot.app.models.entity.Empleado;

public interface IEmpleadoDao extends PagingAndSortingRepository<Empleado, Long>{


}
