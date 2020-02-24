package com.cjhercen.springboot.app.models.object;

import javax.validation.constraints.NotEmpty;

public class FormSolicitud {

	@NotEmpty
	String tipo;
	
	String requiereDesplazamiento;

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getRequiereDesplazamiento() {
		return requiereDesplazamiento;
	}

	public void setRequiereDesplazamiento(String requiereDesplazamiento) {
		this.requiereDesplazamiento = requiereDesplazamiento;
	}

	
	
}
