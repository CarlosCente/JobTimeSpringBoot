package com.cjhercen.springboot.app.controllers;

import java.util.ArrayList;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cjhercen.springboot.app.models.entity.Empleado;
import com.cjhercen.springboot.app.models.entity.Role;
import com.cjhercen.springboot.app.models.entity.Usuario;
import com.cjhercen.springboot.app.models.service.impl.UsuarioServiceImpl;
import com.cjhercen.springboot.app.models.service.interfaces.IEmpleadoService;
import com.cjhercen.springboot.app.util.ConstantesUtils;

@Controller
public class EmpleadoController implements ConstantesUtils {

	@Autowired
	private IEmpleadoService empleadoService;
	
	@Autowired
	private UsuarioServiceImpl usuarioService;

	//private final Logger log = LoggerFactory.getLogger(getClass());

	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String listar(Model model) {

		ArrayList<Empleado> empleados = (ArrayList<Empleado>) empleadoService.findAll();

		model.addAttribute("empleados", empleados);
		return "listar";
	}

	@RequestMapping(value = "/form")
	public String crear(Map<String, Object> model) {

		Empleado empleado = new Empleado();
		model.put("empleado", empleado);
		model.put("titulo", "Creación de un Empleado");
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
		model.put("titulo", "Modificar los datos del empleado");
		return "editar";
	}

	@RequestMapping(value = "/save/{id}", method = RequestMethod.POST)
	public String saveEmpleado(@PathVariable(value = "id") Long id, @ModelAttribute("empleado") Empleado empleado,
			RedirectAttributes flash) {
		Empleado empleadoBD = null;

		empleadoBD = empleadoService.findOne(id);

		if (empleadoBD != null) {

			if (empleado.getNombre() == null || "".equals(empleado.getNombre()) || empleado.getNombre().length() < 2
					|| empleado.getNombre().length() > 20) {
				flash.addFlashAttribute("error",
						"El campo nombre no puede estar vacío y debe tener entre 2 y 20 caracteres");
				return "redirect:/editar/" + id;
			}

			if (empleado.getApellido1() == null || "".equals(empleado.getApellido1())
					|| empleado.getApellido1().length() < 2 || empleado.getApellido1().length() > 20) {
				flash.addFlashAttribute("error",
						"El primer apellido no puede estar vacío y debe tener entre 2 y 20 caracteres");
				return "redirect:/editar/" + id;
			}

			if (empleado.getApellido2() == null || "".equals(empleado.getApellido2())
					|| empleado.getApellido2().length() < 2 || empleado.getApellido2().length() > 20) {
				flash.addFlashAttribute("error",
						"El segundo apellido no puede estar vacío y debe tener entre 2 y 20 caracteres");
				return "redirect:/editar/" + id;
			}

			// Una vez se hayan hecho las comprobaciones si todos los campos están correctos
			// se edita el empleado correctamente
			
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
			RedirectAttributes flash, SessionStatus status) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Empleado");
			return "form";
		}

		String mensajeFlash = "Empleado creado con éxito!";

		empleadoService.save(empleado);
		
		//Se crea el usuario asociado al empleado que se acaba de crear
		Usuario usuario = new Usuario();
		
		//Se obtiene el numero total de usuarios para asignarle el siguiente ID
		ArrayList<Usuario> totalUsuarios = usuarioService.findAll();
		Long idUsuarioNuevo = (long) (totalUsuarios.size() + 1);
		
		//Nombre de usuario se genera con las 3 primeras letras del nombre + 3 primeras letras del primer apellido
		// + 3 primeras letras del segundo apellido
		usuario.setUsername(generarUsername(empleado.getNombre(), empleado.getApellido1(), empleado.getApellido2()));
		usuario.setPassword("$2y$12$vMYXOOoVzK4srU6SAHpseeUGiSg0gyKc2nrw9d1//DYvrd7FtTfYS");
				
		//El rol del usuario se define según lo elegido en el formulario de creacion
		ArrayList<Role> listaRol = new ArrayList<Role>();
		Role role = new Role();
		
		
		role.setAuthority("ROLE_USER");
		
		
		role.setId(empleado.getCod_empl());
		listaRol.add(role);
		usuario.setRoles(listaRol);
		usuario.setId(idUsuarioNuevo);
		usuario.setEnabled(true);
		usuario.setEmpleado(empleado);
		usuarioService.save(usuario);
		
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
	
	private String generarUsername(String nombre, String primerApellido, String segundoApellido) {
		return nombre.substring(0,3) + primerApellido.substring(0,3) + segundoApellido.substring(0,3);
	}

}