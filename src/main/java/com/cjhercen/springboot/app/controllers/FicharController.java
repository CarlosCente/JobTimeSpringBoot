package com.cjhercen.springboot.app.controllers;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FicharController {

	@RequestMapping(value = "/fichar")
	public String fichar(Map<String, Object> model) {

		model.put("titulo", "Fichaje del Empleado");
		return "fichar";
	}
	
}
