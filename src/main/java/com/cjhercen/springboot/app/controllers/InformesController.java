package com.cjhercen.springboot.app.controllers;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cjhercen.springboot.app.models.entity.Usuario;
import com.cjhercen.springboot.app.models.object.FormInforme;
import com.cjhercen.springboot.app.models.service.impl.UsuarioServiceImpl;
import com.cjhercen.springboot.app.util.FechaUtils;
import com.cjhercen.springboot.app.util.InformesUtils;

import net.sf.jasperreports.engine.JRException;

@Controller
public class InformesController {
	
	@Autowired
	UsuarioServiceImpl usuarioService;
	
	InformesUtils informesUtils = new InformesUtils();
	
	FechaUtils fechaUtils = new FechaUtils();
		
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	Date fechaActual = new Date();
	
	@RequestMapping(value = "/informes")
	public String informes(Map<String, Object> model) {
		
		FormInforme formInforme = new FormInforme();
		List<Usuario> listaUsuarios = usuarioService.findAll();
		listaUsuarios = ordenarLista(listaUsuarios);
		
		model.put("listaUsuarios", listaUsuarios);
		model.put("formInforme", formInforme);
		return "informes";
	}

	@RequestMapping(value = "/informes/semanal")
	public void realizarInformeSemanal(RedirectAttributes flash,  HttpServletResponse response,
			@ModelAttribute("formInforme") FormInforme formInforme, BindingResult result) {

		HashMap<String,Object> listaParametros = new HashMap<String,Object>();
		//Se obtiene primero el usuario conectado para obtener los datos del empleado
		String username = formInforme.getUsuarioInforme();
		Usuario usuario = usuarioService.findByUsername(username);
		
		//Se pasa el codigo del empleado como parámetro al informe
		String cod_empl = usuario.getEmpleado().getCod_empl().toString();
	
		listaParametros.put("COD_EMPL", cod_empl);
		listaParametros.put("SEMANAL", "1");
		listaParametros.put("SEMANA", String.valueOf(fechaUtils.obtenerSemana(fechaActual)));
		listaParametros.put("CONSULTA_FICHAJES","AND WEEK(fecha)='"+  String.valueOf(fechaUtils.obtenerSemana(fechaActual)) +"'");
		listaParametros.put("CONSULTA_PERMISOS","AND WEEK(fecha_inicio_permiso)='"+  String.valueOf(fechaUtils.obtenerSemana(fechaActual)) +"'");
		listaParametros.put("CONSULTA_VACACIONES", "AND WEEK(fecha_inicio_vacaciones)='"+  String.valueOf(fechaUtils.obtenerSemana(fechaActual)) +"'");
	
		try {
			informesUtils.crearInformePDF(response, listaParametros);
			log.info("El informe semanal se ha emitido correctamente para el usuario " + usuario.getUsername());
		} catch (JRException e) {
			log.error("Ha ocurrido un error a la hora de imprimir el informe semanal");
			flash.addFlashAttribute("tipo", "Error");
			flash.addFlashAttribute("message", "No se ha podido imprimir el informe semanal");
			e.getStackTrace();
		} catch (SQLException e) {
			log.error("Ha ocurrido un error con la BBDD");
			flash.addFlashAttribute("tipo", "Error");
			flash.addFlashAttribute("message", "Ha ocurrido un error con la conexión de la BBDD");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@RequestMapping(value = "/informes/mensual")
	public void realizarInformeMensual(RedirectAttributes flash,  HttpServletResponse response,
			@ModelAttribute("formInforme") FormInforme formInforme, BindingResult result) {

		HashMap<String,Object> listaParametros = new HashMap<String,Object>();
		//Se obtiene primero el usuario conectado para obtener los datos del empleado
		String username = formInforme.getUsuarioInforme();
		Usuario usuario = usuarioService.findByUsername(username);
		
		//Se pasa el codigo del empleado como parámetro al informe
		String cod_empl = usuario.getEmpleado().getCod_empl().toString();
		listaParametros.put("COD_EMPL", cod_empl);
		listaParametros.put("MENSUAL", "1");
		listaParametros.put("MES", String.valueOf(fechaUtils.obtenerMesActual(fechaActual)));
		listaParametros.put("CONSULTA_FICHAJES","AND MONTH(fecha)='"+  String.valueOf(fechaUtils.obtenerMesActual(fechaActual)) +"'");
		listaParametros.put("CONSULTA_PERMISOS","AND MONTH(fecha_inicio_permiso)='"+  String.valueOf(fechaUtils.obtenerMesActual(fechaActual)) +"'");
		listaParametros.put("CONSULTA_VACACIONES", "AND MONTH(fecha_inicio_vacaciones)='"+  String.valueOf(fechaUtils.obtenerMesActual(fechaActual)) +"'");
	
		try {
			informesUtils.crearInformePDF(response, listaParametros);
			log.info("El informe mensual se ha emitido correctamente para el usuario " + usuario.getUsername());
		} catch (JRException e) {
			log.error("Ha ocurrido un error a la hora de imprimir el informe mensual");
			flash.addFlashAttribute("tipo", "Error");
			flash.addFlashAttribute("message", "No se ha podido imprimir el informe mensual");
			e.getStackTrace();
		} catch (SQLException e) {
			log.error("Ha ocurrido un error con la BBDD");
			flash.addFlashAttribute("tipo", "Error");
			flash.addFlashAttribute("message", "Ha ocurrido un error con la conexión de la BBDD");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	@RequestMapping(value = "/informes/personalizado", method = RequestMethod.POST)
	public void realizarInformePersonalizado(@ModelAttribute("formInforme") FormInforme formInforme, BindingResult result,
			RedirectAttributes flash,  HttpServletResponse response) {

		HashMap<String,Object> listaParametros = new HashMap<String,Object>();
		//Se obtiene primero el usuario conectado para obtener los datos del empleado
		String username = formInforme.getUsuarioInforme();
		Usuario usuario = usuarioService.findByUsername(username);
		
		//Se pasa el codigo del empleado como parámetro al informe
		String cod_empl = usuario.getEmpleado().getCod_empl().toString();
		listaParametros.put("COD_EMPL", cod_empl);
		listaParametros.put("PERSONALIZADO", "1");
		listaParametros.put("DESDE", formInforme.getFechaDesde());
		listaParametros.put("HASTA", formInforme.getFechaHasta());
		
		String fechaDesde = fechaUtils.obtenerFechaParametroEnFormatoSQL(formInforme.getFechaDesde());
		String fechaHasta = fechaUtils.obtenerFechaParametroEnFormatoSQL(formInforme.getFechaHasta());
		listaParametros.put("DESDE_FORMATO_SQL", fechaDesde);
		listaParametros.put("HASTA_FORMATO_SQL", fechaHasta);
		listaParametros.put("CONSULTA_FICHAJES","AND fecha BETWEEN '"+ fechaDesde +"' AND '"+ fechaHasta + "'" );
		listaParametros.put("CONSULTA_PERMISOS","AND fecha_inicio_permiso BETWEEN '"+ fechaDesde +"' AND '"+ fechaHasta + "'" );
		listaParametros.put("CONSULTA_VACACIONES","AND fecha_inicio_vacaciones BETWEEN '"+ fechaDesde +"' AND '"+ fechaHasta + "'" );
		
		try {
			informesUtils.crearInformePDF(response, listaParametros);
			log.info("El informe personalizado se ha emitido correctamente para el usuario " + usuario.getUsername()
			 + " con los parametros: " + listaParametros);
		} catch (JRException e) {
			log.error("Ha ocurrido un error a la hora de imprimir el informe personalizado");
			flash.addFlashAttribute("tipo", "Error");
			flash.addFlashAttribute("message", "No se ha podido imprimir el informe personalizado");
			e.getStackTrace();
		} catch (SQLException e) {
			log.error("Ha ocurrido un error con la BBDD");
			flash.addFlashAttribute("tipo", "Error");
			flash.addFlashAttribute("message", "Ha ocurrido un error con la conexión de la BBDD");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/*
	 * Método para  eliminar el usuario admin para que no
	 * aparezca en los registros al elegir el usuario para el informe
	 */
	private List<Usuario> ordenarLista(List<Usuario> listaUsuarios) {
		List<Usuario> listaOrdenada = listaUsuarios;
		
		Iterator<Usuario> it = listaOrdenada.iterator();
		while(it.hasNext()) {
			Usuario usuario = (Usuario) it.next();     
		    if(usuario.getUsername().equals("admin")) {
		    	it.remove();
		    }
		}
				
		return listaOrdenada;
	}
	
	
}
