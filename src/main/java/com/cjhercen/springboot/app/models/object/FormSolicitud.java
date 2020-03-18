package com.cjhercen.springboot.app.models.object;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

public class FormSolicitud {

	@NotEmpty(message = "Se debe seleccionar un tipo obligatoriamente")
	String tipo;
	
	String requiereDesplazamiento;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date fecha;
	
	@Min(0)
	@Max(8)
	int tiempoNecesario;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date inicioVacaciones;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date finVacaciones;
	
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

	public int getTiempoNecesario() {
		return tiempoNecesario;
	}

	public void setTiempoNecesario(int tiempoNecesario) {
		this.tiempoNecesario = tiempoNecesario;
	}

	public Date getInicioVacaciones() {
		return inicioVacaciones;
	}

	public void setInicioVacaciones(Date inicioVacaciones) {
		this.inicioVacaciones = inicioVacaciones;
	}

	public Date getFinVacaciones() {
		return finVacaciones;
	}

	public void setFinVacaciones(Date finVacaciones) {
		this.finVacaciones = finVacaciones;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}	
	
}
