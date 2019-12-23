package com.cjhercen.springboot.app.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cjhercen.springboot.app.models.entity.Empleado;
import com.cjhercen.springboot.app.models.service.interfaces.IEmpleadoService;
import com.cjhercen.springboot.app.models.service.interfaces.IUploadFileService;
import com.cjhercen.springboot.app.util.ConstantesUtils;
import com.cjhercen.springboot.app.util.paginator.PageRender;

@Controller
public class EmpleadoController implements ConstantesUtils {

	@Autowired
	private IEmpleadoService empleadoService;
	
	@Autowired
	private IUploadFileService uploadService;

	private final Logger log = LoggerFactory.getLogger(getClass());

	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		/*
		 * CAMBIO FUTURO, añadir el numero de elementos a mostrar en la tabla a través
		 * de la configuración
		 */
		Pageable pageRequest = PageRequest.of(page, 6);

		Page<Empleado> empleados = empleadoService.findAll(pageRequest);

		PageRender<Empleado> pageRender = new PageRender<>("/listar", empleados);
		model.addAttribute("empleados", empleados);
		model.addAttribute("page", pageRender);
		return "listar";
	}

	@RequestMapping(value = "/form")
	public String crear(Map<String, Object> model) {

		Empleado empleado = new Empleado();
		model.put("empleado", empleado);
		model.put("titulo", "Formulario para la creación de un Empleado");
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
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash) {
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
				log.info("Se modifica la foto del empleado por: " + foto.getOriginalFilename());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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

		if (!foto.isEmpty()) {
			try {
				uploadService.copy(foto);
				flash.addFlashAttribute("info", "Has subido correctamente '" + foto.getOriginalFilename() + "'");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		String mensajeFlash = "Empleado creado con éxito!";

		empleadoService.save(empleado);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listar";
	}

	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		if (id > 0) {

			Empleado empleado = empleadoService.findOne(id);
			String foto = empleado.getFoto();
			
			uploadService.delete(foto);
			log.info("Se ha borrado correctamente la imagen '" + foto + "'");

			empleadoService.delete(id);
			flash.addFlashAttribute("success", "Empleado eliminado con éxito!");
		}
		return "redirect:/listar";
	}

}