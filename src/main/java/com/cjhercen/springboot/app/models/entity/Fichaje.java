package com.cjhercen.springboot.app.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@IdClass(FichajeId.class)
@Table(name = "fichajes")
public class Fichaje implements Serializable {

	private static final long serialVersionUID = -8420871906339868654L;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	private Empleado empleado;

	@Id
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "fecha")
	private Date fecha;

	@Column(name = "hora_entrada")
	private String horaEntrada = "";

	@Column(name = "hora_salida")
	private String horaSalida = "";

	@Column(name = "hora_inicio_descanso")
	private String horaInicioDescanso = "";

	@Column(name = "hora_fin_descanso")
	private String horaFinDescanso = "";

	@NotNull
	@Column(name = "ip_origen")
	private String ip;

	@Column(name = "tiempo_total")
	private String tiempoTotal;
	
	@Column(name= "finalizado")
	private boolean finalizado = false;

	@PrePersist
	public void prePersist() {
		fecha = new Date();
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getHoraInicioDescanso() {
		return horaInicioDescanso;
	}

	public void setHoraInicioDescanso(String horaInicioDescanso) {
		this.horaInicioDescanso = horaInicioDescanso;
	}

	public String getHoraFinDescanso() {
		return horaFinDescanso;
	}

	public void setHoraFinDescanso(String horaFinDescanso) {
		this.horaFinDescanso = horaFinDescanso;
	}

	public String getTiempoTotal() {
		return tiempoTotal;
	}

	public void setTiempoTotal(String tiempoTotal) {
		this.tiempoTotal = tiempoTotal;
	}

	public boolean isFinalizado() {
		return finalizado;
	}

	public void setFinalizado(boolean finalizado) {
		this.finalizado = finalizado;
	}
	
	

}
