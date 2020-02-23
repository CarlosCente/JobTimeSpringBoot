package com.cjhercen.springboot.app.models.object;

import javax.validation.constraints.NotEmpty;

public class FormSolicitud {

	@NotEmpty
	String tipo;

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
}
