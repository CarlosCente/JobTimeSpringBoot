package com.cjhercen.springboot.app.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cjhercen.springboot.app.models.dao.IFichajeDao;
import com.cjhercen.springboot.app.models.dao.IUsuarioDao;
import com.cjhercen.springboot.app.models.entity.Empleado;
import com.cjhercen.springboot.app.models.entity.Fichaje;
import com.cjhercen.springboot.app.models.entity.Usuario;
import com.cjhercen.springboot.app.models.service.impl.EmpleadoServiceImpl;
import com.cjhercen.springboot.app.models.service.impl.FichajeServiceImpl;
import com.cjhercen.springboot.app.models.service.impl.UsuarioServiceImpl;
import com.cjhercen.springboot.app.util.FechaUtils;
import com.cjhercen.springboot.app.util.FuncionesUtiles;

@Controller
public class FicharController {

	@Autowired
	EmpleadoServiceImpl empleadoService;
	
	@Autowired
	UsuarioServiceImpl usuarioService;

	@Autowired
	FichajeServiceImpl fichajeService;
	
	@Autowired
	IUsuarioDao usuarioDao;
	
	@Autowired
	IFichajeDao fichajeDao;
	
	FechaUtils fechaUtils = new FechaUtils();
	
	String horaActual = fechaUtils.obtenerHoraEnFormatoCadena();
	
	@RequestMapping(value = "/fichar")
	public String fichar(Map<String, Object> model,  HttpServletRequest request) {

		//Se obtiene primero el usuario conectado para obtener los datos del empleado
		String username = usuarioService.getUsername();
		Usuario usuario = usuarioDao.findByUsername(username);
		Empleado empleado = usuario.getEmpleado();		
				
		//Datos del fichaje (Hora entrada y hora de salida)
		Fichaje fichajeEmpleado = fichajeDao.findByEmpleadoAndFecha(empleado, fechaUtils.obtenerFechaActual());
		if(fichajeEmpleado != null) {
			if(fichajeEmpleado.getHoraEntrada() == null | "".equals(fichajeEmpleado.getHoraEntrada())) {
				model.put("horaEntrada", "-");
			} else {
				model.put("horaEntrada", fichajeEmpleado.getHoraEntrada());
			}
			
			if(fichajeEmpleado.getHoraSalida() == null | "".equals(fichajeEmpleado.getHoraSalida())) {
				model.put("horaSalida", "-");
			} else {
				model.put("horaSalida", fichajeEmpleado.getHoraSalida());
			}			
			
			//Cálculo del tiempo transcurrido del fichaje
			String totalTiempo = fechaUtils.obtenerTiempoTranscurrido(fichajeEmpleado.getHoraEntrada(), fichajeEmpleado.getHoraSalida());
			model.put("totalTiempo" , fechaUtils.formatearFechas2digitos(totalTiempo));
			
		} else {
			model.put("horaEntrada", "-");
			model.put("horaSalida", "-");
			model.put("totalTiempo", "-");
		}
		
		model.put("fechaHora", fechaUtils.obtenerFechaEnFormatoCadena() + " " + horaActual);
		model.put("horaActual", horaActual);
		model.put("titulo", "Fichaje del Empleado");
		model.put("empleado", empleado);
		model.put("ip_cliente", FuncionesUtiles.obtenerIp(request));
		model.put("esAdmin", FuncionesUtiles.esAdmin(usuario));
		
		return "fichar";
	}
	
	
	@RequestMapping(value = "/fichar/entrada/")
	public String ficharEntrada(RedirectAttributes flash, HttpServletRequest request) {

		//Se obtiene primero el usuario conectado para obtener los datos del empleado
		String username = usuarioService.getUsername();
		Usuario usuario = usuarioDao.findByUsername(username);
		Empleado empleado = usuario.getEmpleado();

		Fichaje fichajeComprueba = fichajeDao.findByEmpleadoAndFecha(empleado, fechaUtils.obtenerFechaActual());
		
		if(fichajeComprueba != null) {
			flash.addFlashAttribute("error", "Ya has realizado el fichaje de entrada hoy!");
		} else {
			Fichaje fichajeEntrada = new Fichaje();
			fichajeEntrada.setEmpleado(empleado);
			fichajeEntrada.setFecha(fechaUtils.obtenerFechaActual());
			fichajeEntrada.setHoraEntrada(fechaUtils.obtenerHoraEnFormatoCadena());
			fichajeEntrada.setIp(request.getRemoteAddr());
			
			fichajeService.save(fichajeEntrada);	
			
			flash.addFlashAttribute("success", "Has fichado la entrada correctamente a las " + fechaUtils.obtenerHoraEnFormatoCadena());	
		}
		
		return "redirect:/fichar";
	}
		
	
	@RequestMapping(value = "/fichar/salida/")
	public String ficharSalida(RedirectAttributes flash, HttpServletRequest request) {

		//Se obtiene primero el usuario conectado para obtener los datos del empleado
		String username = usuarioService.getUsername();
		Usuario usuario = usuarioDao.findByUsername(username);
		Empleado empleado = usuario.getEmpleado();

		//Se comprueba si existe el fichaje de entrada, sino devolvería error
		Fichaje fichajeComprueba = fichajeDao.findByEmpleadoAndFecha(empleado, fechaUtils.obtenerFechaActual());
		
		if(fichajeComprueba == null) {
			flash.addFlashAttribute("error", "No has fichado aún la entrada de hoy");
		} else {
			
			if(fichajeComprueba.getHoraSalida() != null && !"".equals(fichajeComprueba.getHoraSalida())){
				
				flash.addFlashAttribute("error", "Ya has realizado el fichaje de salida hoy");	
				
			} else {
				//Una vez que se han realizado los dos fichajes, se guarda el tiempo total en la BBDD y marcamos el fichaje
				//de dia como finalizado.
				
				String totalTiempo = fechaUtils.obtenerTiempoTranscurrido(fichajeComprueba.getHoraEntrada(), fichajeComprueba.getHoraSalida());
				fichajeComprueba.setHoraSalida(fechaUtils.obtenerHoraEnFormatoCadena());
				fichajeComprueba.setIp(FuncionesUtiles.obtenerIp(request));
				fichajeComprueba.setTiempoTotal(fechaUtils.formatearFechas2digitos(totalTiempo));
				fichajeComprueba.setFinalizado(true);
				
				fichajeService.save(fichajeComprueba);	
				
				flash.addFlashAttribute("success", "Has fichado la salida correctamente a las " + fechaUtils.obtenerHoraEnFormatoCadena());	
			}
	
		}
		
		return "redirect:/fichar";
	}
	
	
}
