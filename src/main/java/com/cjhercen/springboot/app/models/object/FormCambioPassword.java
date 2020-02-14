package com.cjhercen.springboot.app.models.object;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class FormCambioPassword {

	private String actual;
	
	@Size(min = 8, max = 20, message = "No cumple la longitud necesaria")
	@Pattern(regexp = "^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{8,20}$" , message = "Debe cumplir las condiciones")
	private String nueva;
	
	private String confirmacion;

	public String getActual() {
		return actual;
	}

	public void setActual(String actual) {
		this.actual = actual;
	}

	public String getNueva() {
		return nueva;
	}

	public void setNueva(String nueva) {
		this.nueva = nueva;
	}

	public String getConfirmacion() {
		return confirmacion;
	}

	public void setConfirmacion(String confirmacion) {
		this.confirmacion = confirmacion;
	}
	
	
	
}
