package com.cjhercen.springboot.app.controllers;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class IndexController {

	@RequestMapping(value = {"/","/index"})
	public String crear(Map<String, Object> model) {

		model.put("titulo", "Panel de Control");
		return "index";
	}

	
}
