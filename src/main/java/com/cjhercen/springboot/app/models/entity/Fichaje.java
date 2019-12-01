package com.cjhercen.springboot.app.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "fichajes")
public class Fichaje implements Serializable {

	private static final long serialVersionUID = -8420871906339868654L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cod_fic;
	
	@ManyToOne(fetch = FetchType.LAZY)
    private Empleado empleado;
		
	@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "fecha")
	private Date fecha;
	
	@Column(name = "hora_entrada")
	private String horaEntrada;

	@Column(name = "hora_salida")
	private String horaSalida;
	
	@NotNull
	@Column(name = "tipo_fichaje")
	private String tipoFichaje;
	
	@NotNull
	@Column(name = "ip_origen")
	private String ip;

	@PrePersist
	public void prePersist() {
		fecha = new Date();
	}

	
	public Long getCod_fic() {
		return cod_fic;
	}

	public void setCod_fic(Long cod_fic) {
		this.cod_fic = cod_fic;
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

	public String getTipoFichaje() {
		return tipoFichaje;
	}

	public void setTipoFichaje(String tipoFichaje) {
		this.tipoFichaje = tipoFichaje;
	}


	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}

	
	
}
