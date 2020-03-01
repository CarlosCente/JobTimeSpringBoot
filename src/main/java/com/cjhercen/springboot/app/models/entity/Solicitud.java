package com.cjhercen.springboot.app.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@IdClass(SolicitudId.class)
@Table(name = "solicitudes")
public class Solicitud implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	private Empleado empleado;

	@Id
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "fecha")
	private Date fecha;

	@Id
	@NotEmpty
	@Column(name = "tipo")
	private String tipo;
	
	@NotEmpty
	@Column(name = "estado")
	private String estado;
	
	private int tiempoNecesario = 0;
	
	private String necesitaDesplazamiento = "No";
	
	@Length(min = 0, max = 300)
	@Lob
	@Column(name = "descripcion")
	private String descripcion;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "fechaInicioPermiso")
	private Date fechaInicioPermiso;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "fechaInicioVacaciones")
	private Date fechaInicioVacaciones;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "fechaFinVacaciones")
	private Date fechaFinVacaciones;
	
	
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

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public int getTiempoNecesario() {
		return tiempoNecesario;
	}

	public void setTiempoNecesario(int tiempoNecesario) {
		this.tiempoNecesario = tiempoNecesario;
	}

	public String getNecesitaDesplazamiento() {
		return necesitaDesplazamiento;
	}

	public void setNecesitaDesplazamiento(String necesitaDesplazamiento) {
		this.necesitaDesplazamiento = necesitaDesplazamiento;
	}

	public Date getFechaInicioPermiso() {
		return fechaInicioPermiso;
	}

	public void setFechaInicioPermiso(Date fechaInicioPermiso) {
		this.fechaInicioPermiso = fechaInicioPermiso;
	}

	public Date getFechaInicioVacaciones() {
		return fechaInicioVacaciones;
	}

	public void setFechaInicioVacaciones(Date fechaInicioVacaciones) {
		this.fechaInicioVacaciones = fechaInicioVacaciones;
	}

	public Date getFechaFinVacaciones() {
		return fechaFinVacaciones;
	}

	public void setFechaFinVacaciones(Date fechaFinVacaciones) {
		this.fechaFinVacaciones = fechaFinVacaciones;
	}
	
	
		
}
