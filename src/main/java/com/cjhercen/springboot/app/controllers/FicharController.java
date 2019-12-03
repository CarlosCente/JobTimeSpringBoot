package com.cjhercen.springboot.app.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cjhercen.springboot.app.models.dao.IUsuarioDao;
import com.cjhercen.springboot.app.models.entity.Empleado;
import com.cjhercen.springboot.app.models.entity.Fichaje;
import com.cjhercen.springboot.app.models.entity.Usuario;
import com.cjhercen.springboot.app.models.service.impl.EmpleadoServiceImpl;
import com.cjhercen.springboot.app.models.service.impl.FichajeServiceImpl;
import com.cjhercen.springboot.app.models.service.impl.UsuarioServiceImpl;
import com.cjhercen.springboot.app.util.FechaUtils;

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
	
	FechaUtils fechaUtils = new FechaUtils();
	
	
	@RequestMapping(value = "/fichar")
	public String fichar(Map<String, Object> model,  HttpServletRequest request) {

		//Se obtiene primero el usuario conectado para obtener los datos del empleado
		String username = usuarioService.getUsername();
		Usuario usuario = usuarioDao.findByUsername(username);
		Empleado empleado = usuario.getEmpleado();
		
		//Datos del fichaje (Hora entrada y hora de salida)
		Fichaje fichajeEmpleado = fichajeService.findOne(empleado.getCod_empl());
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
		} else {
			model.put("horaEntrada", "-");
			model.put("horaSalida", "-");
			model.put("totalTiempo", "-");
		}
		
		//Cálculo del tiempo transcurrido del fichaje
		model.put("totalTiempo" ,fechaUtils.obtenerTiempoTranscurrido(fichajeEmpleado.getHoraEntrada(), fichajeEmpleado.getHoraSalida()));
		model.put("titulo", "Fichaje del Empleado");
		model.put("empleado", empleado);
		model.put("ip_cliente", request.getRemoteAddr());
		
		return "fichar";
	}
	
	
	@RequestMapping(value = "/fichar/entrada/")
	public String ficharEntrada(RedirectAttributes flash, Map<String, Object> model, HttpServletRequest request) {

		//Se obtiene primero el usuario conectado para obtener los datos del empleado
		String username = usuarioService.getUsername();
		Usuario usuario = usuarioDao.findByUsername(username);
		Empleado empleado = usuario.getEmpleado();

		//Como es el fichaje de entrada se supone que no puede haber ningun fichaje existente
		Fichaje fichajeEntrada = new Fichaje();
		
		fichajeEntrada.setEmpleado(empleado);
		fichajeEntrada.setFecha(fechaUtils.obtenerFechaActual());
		fichajeEntrada.setHoraEntrada(fechaUtils.obtenerHoraEnFormatoCadena());
		fichajeEntrada.setIp(request.getRemoteAddr());
		
		fichajeService.save(fichajeEntrada);
		
		model.put("titulo", "Fichaje del Empleado");
		model.put("empleado", empleado);
		model.put("ip_cliente", request.getRemoteAddr());
		
		return "fichar";
	}
	
	
	
}
