package com.cjhercen.springboot.app.models.object;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class FormInforme {

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fechaDesde;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fechaHasta;

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	
	
	
}
