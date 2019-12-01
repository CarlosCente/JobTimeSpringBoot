package com.cjhercen.springboot.app.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cjhercen.springboot.app.models.dao.IUsuarioDao;
import com.cjhercen.springboot.app.models.entity.Empleado;
import com.cjhercen.springboot.app.models.entity.Fichaje;
import com.cjhercen.springboot.app.models.entity.Usuario;
import com.cjhercen.springboot.app.models.service.impl.EmpleadoServiceImpl;
import com.cjhercen.springboot.app.models.service.impl.FichajeServiceImpl;
import com.cjhercen.springboot.app.models.service.impl.UsuarioServiceImpl;

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
	
	
	@RequestMapping(value = "/fichar")
	public String fichar(Map<String, Object> model,  HttpServletRequest request) {

		//Se obtiene primero el usuario conectado para obtener los datos del empleado
		String username = usuarioService.getUsername();
		Usuario usuario = usuarioDao.findByUsername(username);
		Empleado empleado = usuario.getEmpleado();
		
		//Se obtiene la IP del cliente
		String ipCliente = "-";
		if (request != null) {
			ipCliente = request.getHeader("X-FORWARDED-FOR");
            if (ipCliente == null || "-".equals(ipCliente)) {
            	ipCliente = request.getRemoteAddr();
            }
        }
		
		//Datos del fichaje
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
				
		model.put("titulo", "Fichaje del Empleado");
		model.put("empleado", empleado);
		model.put("ip_cliente", ipCliente);
		
		return "fichar";
	}
	
}
