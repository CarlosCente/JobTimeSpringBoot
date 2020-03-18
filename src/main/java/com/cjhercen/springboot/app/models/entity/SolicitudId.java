package com.cjhercen.springboot.app.models.entity;

import java.io.Serializable;
import java.util.Date;

public class SolicitudId implements Serializable{

	private static final long serialVersionUID = 1L;

	Long empleado;
	Date fecha;
	String tipo;
	
	public SolicitudId() {
		
	}

	public SolicitudId(Long empleado, Date fecha, String tipo) {
		this.empleado = empleado;
		this.fecha = fecha;
		this.tipo = tipo;
	}

	public Long getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Long empleado) {
		this.empleado = empleado;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	
}
