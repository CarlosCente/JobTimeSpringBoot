package com.cjhercen.springboot.app.models.entity;

import java.io.Serializable;
import java.util.Date;

public class FichajeId implements Serializable {

	private static final long serialVersionUID = 1L;

	Empleado empleado;
	Date fecha;
	
	public FichajeId() {
		
	}
	
	public FichajeId(Empleado empleado, Date fecha) {
		this.empleado = empleado;
		this.fecha = fecha;
	}

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((empleado == null) ? 0 : empleado.hashCode());
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
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
		FichajeId other = (FichajeId) obj;
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
		return true;
	}

	@Override
	public String toString() {
		return "FichajeId [empleado=" + empleado + ", fecha=" + fecha + "]";
	}
	
	
	
	
	
}
