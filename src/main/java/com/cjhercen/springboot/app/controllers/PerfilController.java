package com.cjhercen.springboot.app.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cjhercen.springboot.app.models.entity.Empleado;
import com.cjhercen.springboot.app.models.entity.Incidencia;
import com.cjhercen.springboot.app.models.entity.Usuario;
import com.cjhercen.springboot.app.models.object.IncidenciaDatosPersonales;
import com.cjhercen.springboot.app.models.service.impl.IncidenciaServiceImpl;
import com.cjhercen.springboot.app.models.service.impl.UsuarioServiceImpl;
import com.cjhercen.springboot.app.models.service.interfaces.IEmpleadoService;
import com.cjhercen.springboot.app.models.service.interfaces.IUploadFileService;
import com.cjhercen.springboot.app.util.ConstantesUtils;
import com.cjhercen.springboot.app.util.FechaUtils;

@Controller
public class PerfilController implements ConstantesUtils {
	
	@Autowired
	private IEmpleadoService empleadoService;
	
	@Autowired
	private UsuarioServiceImpl usuarioService;
	
	@Autowired
	private IncidenciaServiceImpl incidenciaService;
	
	@Autowired
	private IUploadFileService uploadService;
	
	FechaUtils fechaUtils = new FechaUtils();
	
	@GetMapping(value = "/perfil")
	public String editar(Map<String, Object> model, RedirectAttributes flash) {

		//Se obtiene primero el usuario conectado para obtener los datos del empleado
		String username = usuarioService.getUsername();
		Usuario usuario = usuarioService.findByUsername(username);
		Empleado empleado = usuario.getEmpleado();		

		model.put("empleado", empleado);
		model.put("titulo", "Pefil del empleado");
		return "perfil";
	}
	
	@RequestMapping(value = "/perfil/editar/{id}", method = RequestMethod.POST)
	public String saveEmpleado(@PathVariable(value = "id") Long id, @ModelAttribute("empleado") Empleado empleado,
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash) {
		Empleado empleadoBD = null;
		
		//Se obtiene primero el usuario conectado para obtener los datos del empleado
		String username = usuarioService.getUsername();
		Usuario usuario = usuarioService.findByUsername(username);
		empleadoBD = usuario.getEmpleado();		

		if (empleadoBD != null) {

			// Comprobación de foto
			String fotoActual = empleadoBD.getFoto();
			Path directorioRecursos = Paths.get(RUTA_IMAGENES_EMPLEADOS);
			String rootPath = directorioRecursos.toFile().getAbsolutePath();

			// Se borra la foto actual del servidor, a no ser que sea la misma, que se
			// mantiene
			if (fotoActual != null && !"".equals(fotoActual)) {
				uploadService.delete(fotoActual);
			}

			try {
				byte[] bytes = foto.getBytes();
				Path rutaCompleta = Paths.get(rootPath + "//" + foto.getOriginalFilename());
				Files.write(rutaCompleta, bytes);
				flash.addFlashAttribute("info", "Has subido correctamente '" + foto.getOriginalFilename() + "'");

				empleadoBD.setFoto(foto.getOriginalFilename());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			empleadoBD.setDireccion(empleado.getDireccion());
			empleadoBD.setLocalidad(empleado.getLocalidad());
			empleadoBD.setPais(empleado.getPais());
			empleadoBD.setProvincia(empleado.getProvincia());
			empleadoService.save(empleadoBD);

			flash.addFlashAttribute("success", "Has editado tu información correctamente");

		} else {
			flash.addFlashAttribute("error", "Error, no se encuentra al empleado");
		}

		return "redirect:/perfil";
	}
	
	
	@RequestMapping(value = "/perfil/incidencia/{id}", method = RequestMethod.POST)
	public String saveIncidencia(@PathVariable(value = "id") Long id,
			@ModelAttribute("incidenciaDatosPersonales") IncidenciaDatosPersonales incidenciaDatosPersonales,
			RedirectAttributes flash) {
	
		Empleado empleadoBD = null;
		
		//Se obtiene primero el usuario conectado para obtener los datos del empleado
		String username = usuarioService.getUsername();
		Usuario usuario = usuarioService.findByUsername(username);
		empleadoBD = usuario.getEmpleado();		

		if (empleadoBD != null) {
	
		/*
		 * Si no ocurre ningún error con el empleado...
		 * Se crea la incidencia con estado abierta y con tipo ERROR para el empleado que se encuentra conectado
		 */
			System.out.println(fechaUtils.obtenerFechaActual());
			Incidencia incidenciaNueva = new Incidencia();
			incidenciaNueva.setEmpleado(empleadoBD);
			incidenciaNueva.setTipo(INCIDENCIA_ERROR);
			incidenciaNueva.setEstado(INCIDENCIA_ABIERTA);
			incidenciaNueva.setFecha(fechaUtils.obtenerFechaActual());
			incidenciaNueva.setMensaje("PRUEBA");
					
			incidenciaService.save(incidenciaNueva);

			flash.addFlashAttribute("success", "Incidencia añadida correctamente");

		} else {
			flash.addFlashAttribute("error", "Error, no se ha podido crear la incidencia");
		}
		
		return "redirect:/perfil";
	}
	
	public String generarMensaje(Empleado empleado, IncidenciaDatosPersonales incidenciaDatosPersonales) {
		
		return "";
	}
	
}
