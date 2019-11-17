package com.cjhercen.springboot.app.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cjhercen.springboot.app.models.entity.Empleado;
import com.cjhercen.springboot.app.models.service.IEmpleadoService;
import com.cjhercen.springboot.app.util.paginator.PageRender;

@Controller
@SessionAttributes("cliente")
public class EmpleadoController {

	@Autowired
	private IEmpleadoService empleadoService;

	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Empleado empleado = empleadoService.findOne(id);
		if (empleado == null) {
			flash.addFlashAttribute("error", "El empleado no existe en la base de datos");
			return "redirect:/listar";
		}

		model.put("empleado", empleado);
		model.put("titulo", "Detalle empleado: " + empleado.getNombre() + " " + 
					empleado.getApellido1() + " " + empleado.getApellido2());

		return "ver";
	}

	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 10);

		Page<Empleado> empleados = empleadoService.findAll(pageRequest);

		PageRender<Empleado> pageRender = new PageRender<>("/listar", empleados);
		model.addAttribute("titulo", "Listado de empleados");
		model.addAttribute("empleados", empleados);
		model.addAttribute("page", pageRender);
		return "listar";
	}

	@RequestMapping(value = "/form")
	public String crear(Map<String, Object> model) {

		Empleado empleado = new Empleado();
		model.put("empleado", empleado);
		model.put("titulo", "Formulario de Empleado");
		return "form";
	}

	@GetMapping(value = "/editar/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Empleado empleado = null;

		if (id > 0) {
			empleado = empleadoService.findOne(id);
			if (empleado == null) {
				flash.addFlashAttribute("error", "El ID del empleado no existe en la BBDD!");
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", "El ID del empleado no puede ser cero!");
			return "redirect:/listar";
		}
		
		model.put("empleado", empleado);
		model.put("titulo", "Editar Empleado");
		return "editar";
	}
	
	@RequestMapping(value = "/save/{id}", method = RequestMethod.POST)
	public String saveEmpleado(@PathVariable(value = "id") Long id, @ModelAttribute("empleado") Empleado empleado, RedirectAttributes flash) {
		Empleado empleadoBD = null;
						
		empleadoBD = empleadoService.findOne(id);
		
		if(empleadoBD != null) {
			empleadoBD.setApellido1(empleado.getApellido1());
			empleadoBD.setApellido2(empleado.getApellido2());
			empleadoBD.setDireccion(empleado.getDireccion());
			empleadoBD.setLocalidad(empleado.getLocalidad());
			empleadoBD.setNombre(empleado.getNombre());
			empleadoBD.setPais(empleado.getPais());
			empleadoBD.setProvincia(empleado.getProvincia());
			empleadoBD.setFechaNacim(empleado.getFechaNacim());
			empleadoService.save(empleadoBD);
			
			flash.addFlashAttribute("success", "El empleado se ha editado correctamente!");	
		} else {
			flash.addFlashAttribute("error", "No se encuentra el ID del empleado");
		}
						
		return "redirect:/listar";
	}
	
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Empleado empleado, BindingResult result, Model model,
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Empleado");
			return "form";
		}

		String 	mensajeFlash = "Empleado creado con éxito!";
			
		empleadoService.save(empleado);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listar";
	}

	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		if (id > 0) {
			empleadoService.delete(id);
			flash.addFlashAttribute("success", "Empleado eliminado con éxito!");
		}
		return "redirect:/listar";
	}

}