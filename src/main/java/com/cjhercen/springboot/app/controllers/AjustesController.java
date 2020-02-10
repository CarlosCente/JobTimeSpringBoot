package com.cjhercen.springboot.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AjustesController {

	@GetMapping("/ajustes")
	public String ajustes(Model model, RedirectAttributes flash) {
		
		model.addAttribute("titulo", "Configuraciones disponibles");
		return "ajustes";
	}
	
	
}
