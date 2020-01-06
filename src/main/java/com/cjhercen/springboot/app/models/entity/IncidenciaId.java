package com.cjhercen.springboot.app.models.entity;

import java.io.Serializable;
import java.util.Date;

public class IncidenciaId implements Serializable{

	private static final long serialVersionUID = 1L;

	Long empleado;
	Date fecha;
	String mensaje;
	
	public IncidenciaId() {
		
	}
	
	public IncidenciaId(Long empleado, Date fecha, String mensaje) {
		this.empleado = empleado;
		this.fecha = fecha;
		this.mensaje = mensaje;
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
	
	public String getMensaje() {
		return mensaje;
	}
	
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((empleado == null) ? 0 : empleado.hashCode());
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + ((mensaje == null) ? 0 : mensaje.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IncidenciaId other = (IncidenciaId) obj;
		if (empleado == null) {
			if (other.empleado != null)
				return false;
		} else if (!empleado.equals(other.empleado))
			return false;
		if (fecha == null) {
			if (other.fecha != null)
				return false;
		} else if (!fecha.equals(other.fecha))
			return false;
		if (mensaje == null) {
			if (other.mensaje != null)
				return false;
		} else if (!mensaje.equals(other.mensaje))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "IncidenciaId [empleado=" + empleado + ", fecha=" + fecha + ", mensaje="+ mensaje + "]";
	}
	
	
}
