package com.cjhercen.springboot.app.models.object;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class IncidenciaDatosPersonales {

	private String nombre;
	
	private String apellido1;
	
	private String apellido2;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fechaNacimiento;

	private boolean hayNombre;
	
	private boolean hayApellido1;

	private boolean hayApellido2;
	
	private boolean hayFechaNacimiento;
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido1() {
		return apellido1;
	}

	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}

	public String getApellido2() {
		return apellido2;
	}

	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public boolean isHayNombre() {
		return hayNombre;
	}

	public void setHayNombre(boolean hayNombre) {
		this.hayNombre = hayNombre;
	}

	public boolean isHayApellido1() {
		return hayApellido1;
	}

	public void setHayApellido1(boolean hayApellido1) {
		this.hayApellido1 = hayApellido1;
	}

	public boolean isHayApellido2() {
		return hayApellido2;
	}

	public void setHayApellido2(boolean hayApellido2) {
		this.hayApellido2 = hayApellido2;
	}

	public boolean isHayFechaNacimiento() {
		return hayFechaNacimiento;
	}

	public void setHayFechaNacimiento(boolean hayFechaNacimiento) {
		this.hayFechaNacimiento = hayFechaNacimiento;
	}
	
}
