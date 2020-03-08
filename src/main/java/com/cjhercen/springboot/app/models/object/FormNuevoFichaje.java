package com.cjhercen.springboot.app.models.object;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class FormNuevoFichaje {

	String usuario;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date fecha;
	
	String horaEntrada;
	
	String horaSalida;

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getHoraEntrada() {
		return horaEntrada;
	}

	public void setHoraEntrada(String horaEntrada) {
		this.horaEntrada = horaEntrada;
	}

	public String getHoraSalida() {
		return horaSalida;
	}

	public void setHoraSalida(String horaSalida) {
		this.horaSalida = horaSalida;
	}
	
	
	
}
