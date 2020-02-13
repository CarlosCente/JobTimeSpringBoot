package com.cjhercen.springboot.app.models.object;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class FormCambioPassword {

	@NotEmpty(message="La contraseña actual es obligatoria")
	private String actual;
	
	@NotEmpty(message="La nueva contraseña es obligatoria")
	@Size(min = 8, max = 16, message="La contraseña tiene que tener entre 8 y 16 caracteres")
	private String nueva;
	
	@NotEmpty(message="La confirmación de la nueva contraseña es obligatoria")
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
