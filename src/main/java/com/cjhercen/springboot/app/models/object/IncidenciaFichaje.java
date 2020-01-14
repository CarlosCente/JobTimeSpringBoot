package com.cjhercen.springboot.app.models.object;

public class IncidenciaFichaje {
	
	private String tipo;
	
	private String fechaHoraEntrada;
	
	private String comentarioEntrada;

	private String fechaHoraSalida;
	
	private String comentarioSalida;
	
	private String comentarioOtro;

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getFechaHoraEntrada() {
		return fechaHoraEntrada;
	}

	public void setFechaHoraEntrada(String fechaHoraEntrada) {
		this.fechaHoraEntrada = fechaHoraEntrada;
	}

	public String getComentarioEntrada() {
		return comentarioEntrada;
	}

	public void setComentarioEntrada(String comentarioEntrada) {
		this.comentarioEntrada = comentarioEntrada;
	}

	public String getFechaHoraSalida() {
		return fechaHoraSalida;
	}

	public void setFechaHoraSalida(String fechaHoraSalida) {
		this.fechaHoraSalida = fechaHoraSalida;
	}

	public String getComentarioSalida() {
		return comentarioSalida;
	}

	public void setComentarioSalida(String comentarioSalida) {
		this.comentarioSalida = comentarioSalida;
	}

	public String getComentarioOtro() {
		return comentarioOtro;
	}

	public void setComentarioOtro(String comentarioOtro) {
		this.comentarioOtro = comentarioOtro;
	}

}
