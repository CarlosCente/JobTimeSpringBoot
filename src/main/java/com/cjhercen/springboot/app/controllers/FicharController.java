package com.cjhercen.springboot.app.controllers;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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

import com.cjhercen.springboot.app.models.entity.Empleado;
import com.cjhercen.springboot.app.models.entity.Fichaje;
import com.cjhercen.springboot.app.models.entity.Incidencia;
import com.cjhercen.springboot.app.models.entity.Usuario;
import com.cjhercen.springboot.app.models.object.IncidenciaFichaje;
import com.cjhercen.springboot.app.models.service.impl.EmpleadoServiceImpl;
import com.cjhercen.springboot.app.models.service.impl.IncidenciaServiceImpl;
import com.cjhercen.springboot.app.models.service.impl.UsuarioServiceImpl;
import com.cjhercen.springboot.app.models.service.interfaces.IFichajeService;
import com.cjhercen.springboot.app.util.ConstantesUtils;
import com.cjhercen.springboot.app.util.FechaUtils;
import com.cjhercen.springboot.app.util.FuncionesUtiles;
import com.cjhercen.springboot.app.util.InformesUtils;

import net.sf.jasperreports.engine.JRException;

@Controller
public class FicharController implements ConstantesUtils {

	@Autowired
	EmpleadoServiceImpl empleadoService;
	
	@Autowired
	UsuarioServiceImpl usuarioService;

	@Autowired
	IncidenciaServiceImpl incidenciaService;
	
	@Autowired
	IFichajeService fichajeService;
		
	FechaUtils fechaUtils = new FechaUtils();
	
	InformesUtils informesUtils = new InformesUtils();
	
	String horaActual = fechaUtils.obtenerHoraEnFormatoCadena();
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	Date fechaActual = new Date();
	
	@RequestMapping(value = "/fichar")
	public String fichar(Map<String, Object> model,  HttpServletRequest request) {

		//Introduzco el objeto en el modelo para poder crear incidencias
		IncidenciaFichaje incidenciaFichaje = new IncidenciaFichaje();
		model.put("incidenciaFichaje", incidenciaFichaje);
		
		//Se obtiene primero el usuario conectado para obtener los datos del empleado
		String username = usuarioService.getUsername();
		Usuario usuario = usuarioService.findByUsername(username);
		Empleado empleado = usuario.getEmpleado();		
				
		//Comprobación si ya se ha hecho el fichaje de entrada o de salida
		Boolean hayEntrada = false;
		Boolean haySalida = false;
		String porcentaje = "0%";
		
		//En caso de que sea domingo no se habilita el fichaje
		Boolean esDomingo = false;
		esDomingo = fechaUtils.esDomingo(fechaActual);
		
		//Datos del fichaje (Hora entrada y hora de salida)
		Fichaje fichajeEmpleado = fichajeService.findByEmpleadoAndFecha(empleado, fechaUtils.obtenerFechaActual());
		if(fichajeEmpleado != null) {
			if(fichajeEmpleado.getHoraEntrada() == null | "".equals(fichajeEmpleado.getHoraEntrada())) {
				model.put("horaEntrada", "-");
			} else {
				model.put("horaEntrada", fichajeEmpleado.getHoraEntrada());
				hayEntrada = true;
			}
			
			if(fichajeEmpleado.getHoraSalida() == null | "".equals(fichajeEmpleado.getHoraSalida())) {
				model.put("horaSalida", "-");
			} else {
				model.put("horaSalida", fichajeEmpleado.getHoraSalida());
				haySalida = true;
			}			
			
			//Cálculo del tiempo transcurrido del fichaje
			String totalTiempo = fechaUtils.obtenerTiempoTranscurrido(fichajeEmpleado.getHoraEntrada(), fichajeEmpleado.getHoraSalida());
			model.put("totalTiempo" , fechaUtils.formatearFechas2digitos(totalTiempo));
			porcentaje = calcularPorcentajeFichado(totalTiempo);

			
		} else {
			model.put("horaEntrada", "-");
			model.put("horaSalida", "-");
			model.put("totalTiempo", "00:00");
		}
		
		if("-".equals(porcentaje)) {
			porcentaje = "0%";
		}
		model.put("porcentaje", porcentaje);
		model.put("fechaHora", fechaUtils.obtenerFechaEnFormatoCadena() + " " + horaActual);
		model.put("horaActual", horaActual);
		model.put("titulo", "Datos del empleado y estadísticas del fichaje");
		model.put("empleado", empleado);
		model.put("ip_cliente", FuncionesUtiles.obtenerIp(request));
		model.put("esAdmin", FuncionesUtiles.esAdmin(usuario));
		
		//Mirar a ver si se permite el fichaje, si es false si se puede, si es true se bloquea
		Boolean noPermiteEntrada = false;
		Boolean noPermiteSalida = false;
		
		if(hayEntrada) {
			noPermiteEntrada = true;
		}else {
			if(esDomingo) {
				noPermiteEntrada = true;
			}
		}
		
		if(haySalida) {
			noPermiteSalida = true;
		} else {
			if(esDomingo) {
				noPermiteSalida = true;
			}
		}
		
		model.put("noPermiteEntrada", noPermiteEntrada);
		model.put("noPermiteSalida", noPermiteSalida);
		return "fichar";
	}
	
	
	@RequestMapping(value = "/fichar/entrada/")
	public String ficharEntrada(RedirectAttributes flash, HttpServletRequest request) {

		//Se obtiene primero el usuario conectado para obtener los datos del empleado
		String username = usuarioService.getUsername();
		Usuario usuario = usuarioService.findByUsername(username);
		Empleado empleado = usuario.getEmpleado();
		
		Fichaje fichajeComprueba = fichajeService.findByEmpleadoAndFecha(empleado, fechaUtils.obtenerFechaActual());
		
		if(fichajeComprueba != null) {
			flash.addFlashAttribute("tipo", "Error");
			flash.addFlashAttribute("message", "Ya se ha realizado el fichaje de entrada de hoy");
		} else {
			Fichaje fichajeEntrada = new Fichaje();
			fichajeEntrada.setEmpleado(empleado);
			fichajeEntrada.setFecha(fechaUtils.obtenerFechaActual());
			fichajeEntrada.setHoraEntrada(fechaUtils.obtenerHoraEnFormatoCadena());
			fichajeEntrada.setIp(request.getRemoteAddr());
			fichajeEntrada.setSemanaDelAnnio(fechaUtils.obtenerSemana(fechaActual));
			
			fichajeService.save(fichajeEntrada);	
			
			flash.addFlashAttribute("tipo", "Información");
			flash.addFlashAttribute("message", "Has fichado la entrada correctamente a las " + fechaUtils.obtenerHoraEnFormatoCadena());		}
			log.info("El usuario " + empleado.getUsuario().getUsername() + "ha fichado la entrada correctamente a las " + fechaUtils.obtenerHoraEnFormatoCadena()
				+ " el día " + fechaUtils.obtenerFechaActual());
		
		return "redirect:/fichar";
	}
		
	
	@RequestMapping(value = "/fichar/salida/")
	public String ficharSalida(RedirectAttributes flash, HttpServletRequest request) {

		//Se obtiene primero el usuario conectado para obtener los datos del empleado
		String username = usuarioService.getUsername();
		Usuario usuario = usuarioService.findByUsername(username);
		Empleado empleado = usuario.getEmpleado();

		//Se comprueba si existe el fichaje de entrada, sino devolvería error
		Fichaje fichajeComprueba = fichajeService.findByEmpleadoAndFecha(empleado, fechaUtils.obtenerFechaActual());
		
		if(fichajeComprueba == null) {
			flash.addFlashAttribute("tipo", "Error");
			flash.addFlashAttribute("message", "No se ha fichado aún la entrada de hoy");
		} else {
			
			if(fichajeComprueba.getHoraSalida() != null && !"".equals(fichajeComprueba.getHoraSalida())){
				
				flash.addFlashAttribute("tipo", "Error");
				flash.addFlashAttribute("message", "Ya se ha realizado el fichaje de salida de hoy");
				
			} else {
				//Una vez que se han realizado los dos fichajes, se guarda el tiempo total en la BBDD y marcamos el fichaje
				//de dia como finalizado.
				
				String totalTiempo = fechaUtils.obtenerTiempoTranscurrido(fichajeComprueba.getHoraEntrada(), fichajeComprueba.getHoraSalida());
				fichajeComprueba.setHoraSalida(fechaUtils.obtenerHoraEnFormatoCadena());
				fichajeComprueba.setIp(FuncionesUtiles.obtenerIp(request));
				fichajeComprueba.setTiempoTotal(fechaUtils.formatearFechas2digitos(totalTiempo));
				fichajeComprueba.setFinalizado(true);
				
				fichajeService.save(fichajeComprueba);	
				
				flash.addFlashAttribute("tipo", "Información");
				flash.addFlashAttribute("message", "Has fichado la salida correctamente a las " + fechaUtils.obtenerHoraEnFormatoCadena());	
				log.info("El usuario " + empleado.getUsuario().getUsername() + "ha fichado la salida correctamente a las " + fechaUtils.obtenerHoraEnFormatoCadena()
				+ " el día " + fechaUtils.obtenerFechaActual());
			}
	
		}
		
		return "redirect:/fichar";
	}
	
	
	@RequestMapping(value = "/fichar/incidencia", method = RequestMethod.POST)
	public String saveIncidencia(@ModelAttribute("incidenciaFichaje") IncidenciaFichaje incidenciaFichaje,
			BindingResult bindingResult, RedirectAttributes flash) {
	
		/*
		 * Se controla que no vaya la incidencia completamente vacía
		 */
		if(INCIDENCIA_NO_SELECCION.equals(incidenciaFichaje.getTipo())){
			flash.addFlashAttribute("tipo", "Error");
			flash.addFlashAttribute("message", "No es posible crear una incidencia vacía");
			return "redirect:/fichar";
		}
		
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
		
			Incidencia incidenciaNueva = new Incidencia();
			incidenciaNueva.setEmpleado(empleadoBD);
			incidenciaNueva.setTipo(INCIDENCIA_ERROR);
			incidenciaNueva.setEstado(INCIDENCIA_ABIERTA);
			incidenciaNueva.setFecha(fechaUtils.obtenerFechaActual());
			
			//Se establece el mensaje (el tipo de incidencia que es)
			if(INCIDENCIA_ENTRADA.equals(incidenciaFichaje.getTipo())) {
				incidenciaNueva.setMensaje(INCIDENCIA_FICHAJE_ENTRADA);
			} else if (INCIDENCIA_SALIDA.equals(incidenciaFichaje.getTipo())) {
				incidenciaNueva.setMensaje(INCIDENCIA_FICHAJE_SALIDA);
			} else if (INCIDENCIA_OTROS.equals(incidenciaFichaje.getTipo())) {
				incidenciaNueva.setMensaje(INCIDENCIA_FICHAJE_OTROS);
			}
			
			//Se establece la descripcion dependiendo tambien del tipo de incidencia
			incidenciaNueva.setDescripcion(generarDescripcion(empleadoBD, incidenciaFichaje));
					
			incidenciaService.save(incidenciaNueva);

			flash.addFlashAttribute("tipo", "Información");
			flash.addFlashAttribute("message", "Incidencia añadida correctamente");
			log.info("El usuario " + empleadoBD.getUsuario().getUsername() + "ha creado correctamente una indicencia");

		} else {
			flash.addFlashAttribute("tipo", "Error");
			flash.addFlashAttribute("message", "No se ha podido crear la incidencia");
		}
		
		return "redirect:/fichar";
	}

	
	@RequestMapping(value = "/informe/semanal")
	public void realizarInformeSemanal(RedirectAttributes flash,  HttpServletResponse response) {

		HashMap<String,Object> listaParametros = new HashMap<String,Object>();
		//Se obtiene primero el usuario conectado para obtener los datos del empleado
		String username = usuarioService.getUsername();
		Usuario usuario = usuarioService.findByUsername(username);
		
		//Se pasa el codigo del empleado como parámetro al informe
		String cod_empl = usuario.getEmpleado().getCod_empl().toString();
	
		listaParametros.put("COD_EMPL", cod_empl);
		listaParametros.put("SEMANAL", "1");
		listaParametros.put("SEMANA", String.valueOf(fechaUtils.obtenerSemana(fechaActual)));
	
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
	
	@RequestMapping(value = "/informe/mensual")
	public void realizarInformeMensual(RedirectAttributes flash,  HttpServletResponse response) {

		HashMap<String,Object> listaParametros = new HashMap<String,Object>();
		//Se obtiene primero el usuario conectado para obtener los datos del empleado
		String username = usuarioService.getUsername();
		Usuario usuario = usuarioService.findByUsername(username);
		
		//Se pasa el codigo del empleado como parámetro al informe
		String cod_empl = usuario.getEmpleado().getCod_empl().toString();
		listaParametros.put("COD_EMPL", cod_empl);
		listaParametros.put("MENSUAL", "1");
		listaParametros.put("MES", String.valueOf(fechaUtils.obtenerMesActual(fechaActual)));
	
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
	
	
	public String calcularPorcentajeFichado(String tiempoTotal) {
		DecimalFormat df = new DecimalFormat("#");
		int totalTiempo = fechaUtils.obtenerHoraEnMinutos(tiempoTotal);
		String value = "0";
		double totalJornada = 480;
		double porcentajeInt;
		
		if(totalTiempo > 0) {
			porcentajeInt = totalTiempo / totalJornada*100;
			value = df.format(porcentajeInt);
		} 
		
		return value +'%';
	}
	
	/**
	 * Método que genera la descripción de la incidencia según del tipo que sea y de los datos
	 * @param empleado empleado que crea la incidencia (usuario conectado)
	 * @param incidenciaFichaje datos pasados en el modal
	 * @return String con la descripcion de la incidencia
	 */
	public String generarDescripcion(Empleado empleado, IncidenciaFichaje incidenciaFichaje) {
		
		String mensaje = "";
				
		if(INCIDENCIA_ENTRADA.equals(incidenciaFichaje.getTipo())) {
			
			String fechaHoraEntrada = incidenciaFichaje.getFechaHoraEntrada();
			String comentarioEntrada = incidenciaFichaje.getComentarioEntrada();
			
			String [] partesEntrada = fechaHoraEntrada.split(" ");
			String fechaEntrada = partesEntrada[0];
			String horaEntrada = partesEntrada[1];
			
			mensaje = "El usuario "+empleado.getUsuario().getUsername() + ", ha creado una incidencia con su fichaje de entrada del día ("
				+ fechaEntrada +") la hora correcta sería a las ("+ horaEntrada +"). Además el usuario ha añadido el siguiente comentario:"
						+ " "+comentarioEntrada;
		} 
		
		if(INCIDENCIA_SALIDA.equals(incidenciaFichaje.getTipo())) {
			String fechaHoraSalida = incidenciaFichaje.getFechaHoraSalida();
			String comentarioSalida = incidenciaFichaje.getComentarioSalida();
			
			String [] partesSalida = fechaHoraSalida.split(" ");
			String fechaSalida = partesSalida[0];
			String horaSalida = partesSalida[1];
			
			mensaje = "El usuario "+empleado.getUsuario().getUsername() + ", ha creado una incidencia con su fichaje de salida del día ("
					+ fechaSalida + ") la hora correcta sería a las ("+ horaSalida + "). Además el usuario ha añadido el siguiente comentario:"
					+ " " + comentarioSalida;
		} 

		if(INCIDENCIA_OTROS.equals(incidenciaFichaje.getTipo())) {
			String comentarioOtro = incidenciaFichaje.getComentarioOtro();
			
			mensaje = "El usuario "+empleado.getUsuario().getUsername() + ", ha creado una incidencia y ha dejado el"
							+ " siguiente comentario: " + comentarioOtro;

		} 
		
		return mensaje;
	}
	
	
}
