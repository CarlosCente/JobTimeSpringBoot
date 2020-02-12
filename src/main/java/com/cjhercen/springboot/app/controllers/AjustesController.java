package com.cjhercen.springboot.app.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cjhercen.springboot.app.models.entity.Usuario;
import com.cjhercen.springboot.app.models.object.FormCambioPassword;
import com.cjhercen.springboot.app.models.service.impl.UsuarioServiceImpl;

@Controller
public class AjustesController {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	UsuarioServiceImpl usuarioService;
	
	@GetMapping("/ajustes")
	public String ajustes(Model model, RedirectAttributes flash) {
		
		FormCambioPassword formCambioPassword = new FormCambioPassword();
		model.addAttribute("formCambioPassword", formCambioPassword);
		model.addAttribute("titulo", "Configuraciones disponibles");
		return "ajustes";
	}
	
	
	@RequestMapping("/ajustes/cambioPassword")
	public String cambioPassword(Model model, RedirectAttributes flash,
			@ModelAttribute("formCambioPassword") FormCambioPassword formCambioPassword) {
		
		Usuario usuarioConectado = usuarioService.findByUsername(usuarioService.getUsername());
		String passwordActual = usuarioConectado.getPassword();
		
		//Se compara el password introducido como actual para ver si conincide con la contraseña del usuario
		if(passwordEncoder.matches(formCambioPassword.getActual(), passwordActual)) {
			
			//Se comparan las dos contraseñas nuevas para ver si coinciden
			if(formCambioPassword.getNueva().equals(formCambioPassword.getConfirmacion())) {
				String passwordNew = passwordEncoder.encode(formCambioPassword.getNueva());
				usuarioConectado.setPassword(passwordNew);
				usuarioService.save(usuarioConectado);
			} else {
				log.warn("Ha ocurrido un error al intentar modificar la contraseña del usuario " + usuarioConectado.getUsername() +
						"la contraseña nueva y la de confirmación deben coincidir");
			}
			
		} else {
			log.warn("Ha ocurrido un error al intentar modificar la contraseña del usuario " + usuarioConectado.getUsername() +
					"La contraseña actual no es correcta.");
		}
		
		return "redirect:/ajustes";
	}
	
}
