package com.cjhercen.springboot.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "empleados")
public class Empleado implements Serializable {

	private static final long serialVersionUID = -7273881145340481070L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long cod_empl;
	
	@NotEmpty
	@Length(min = 2, max = 20)
	private String nombre;

	@NotEmpty
	private String apellido1;
	
	@NotEmpty
	private String apellido2;

	@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "fecha_nacim")
	private Date fechaNacim;
	
	private String direccion;
	
	private String pais;
	
	private String provincia;
	
	private String localidad;

	@OneToMany(mappedBy = "empleado" ,fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Fichaje> fichajes;	
	
	public Empleado() {
		fichajes = new ArrayList<Fichaje>();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido1() {
		return apellido1;
	}

	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}

	public String getApellido2() {
		return apellido2;
	}

	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}

	public Date getFechaNacim() {
		return fechaNacim;
	}

	public void setFechaNacim(Date fechaNacim) {
		this.fechaNacim = fechaNacim;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public Long getCod_empl() {
		return cod_empl;
	}

	public void setCod_empl(Long cod_empl) {
		this.cod_empl = cod_empl;
	}

	public List<Fichaje> getFichajes() {
		return fichajes;
	}

	public void setFichajes(List<Fichaje> fichajes) {
		this.fichajes = fichajes;
	}
	
	/*
	 * Para añadir un fichaje en la lista de fichajes
	 */
	public void addFichaje(Fichaje fichaje) {
		fichajes.add(fichaje);
	}
	
}
