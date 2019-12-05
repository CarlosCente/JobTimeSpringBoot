package com.cjhercen.springboot.app.controllers;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cjhercen.springboot.app.models.entity.Empleado;
import com.cjhercen.springboot.app.models.service.interfaces.IEmpleadoService;
import com.cjhercen.springboot.app.util.paginator.PageRender;


@Controller
public class IndexController {

	@Autowired
	private IEmpleadoService empleadoService;
	
	Date fecha = new Date();
	String fechaFormateada = new SimpleDateFormat("dd-MM-yyyy").format(fecha);      
    
	@RequestMapping(value = {"/","/index"})
	public String inicio(@RequestParam(name = "page", defaultValue = "0") int page, Map<String, Object> model) {

		Pageable pageRequest = PageRequest.of(page, 5);

		Page<Empleado> empleados = empleadoService.findAll(pageRequest);

		PageRender<Empleado> pageRender = new PageRender<>("/index", empleados);
		model.put("empleados", empleados);
		model.put("page", pageRender);
		model.put("titulo", "Panel de Control");
		model.put("totalEmpleados", getTotalEmpleados());
		model.put("fecha",fechaFormateada);
		return "index";
	}

	/**
	 * Método para obtener el total de empleados que existen en la BBDD
	 * @return
	 */
	public int getTotalEmpleados() {
		int total = 0;
		List<Empleado> empleados = empleadoService.findAll();
		if(empleados != null) {
			total = empleados.size();
		}
		return total;
	}
	
}
