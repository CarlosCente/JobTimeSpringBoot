$(document).ready(function() {
  $('[data-toggle="tooltip"]').tooltip()
});


function enable_disable(string,campo){
	
	var isChecked = document.getElementById(string).checked;
	if(isChecked){
		document.getElementById(campo).disabled = false;
	} else{
		document.getElementById(campo).disabled = true;
	}
	
}

function cargarSeleccionado(){
	
	var horaEntrada = document.getElementById("horaEntrada");
	var comentarioEntrada = document.getElementById("comentarioEntrada");
	var horaSalida = document.getElementById("horaSalida");
	var comentarioSalida = document.getElementById("comentarioSalida");
	var tipoOtro = document.getElementById("tipoOtro");
	var seleccion = document.getElementById("sel1").value;

	if(seleccion == 'nada'){
		//Campos que desaparecen
		horaSalida.hidden = true;
		comentarioSalida.hidden=true;
		horaEntrada.hidden = true;
		comentarioEntrada.hidden=true;
		tipoOtro.hidden = true;
		
		//Se desactivan todos los campos
		horaSalida.disabled = true;
		comentarioSalida.disabled=true;
		horaEntrada.disabled = true;
		comentarioEntrada.disabled=true;
		tipoOtro.disabled = true;
	}
	
	if(seleccion == 'entrada'){
		//Campos que desaparecen y se desactivan
		horaSalida.hidden = true;
		horaSalida.disabled = true;
		comentarioSalida.hidden=true;
		comentarioSalida.disabled=true;
		tipoOtro.hidden = true;
		tipoOtro.disabled = true;
		
		//Campos que se muestran
		horaEntrada.hidden = false;
		horaEntrada.disabled = false;
		comentarioEntrada.hidden=false;
		comentarioEntrada.disabled = false;

	}
	
	if(seleccion == 'salida'){
		//Campos que desaparecen y se desactivan
		horaEntrada.hidden = true;
		horaEntrada.disabled = true;
		comentarioEntrada.hidden=true;
		comentarioEntrada.disabled=true;
		tipoOtro.hidden = true;
		tipoOtro.disabled = true;
	
		//Campos que se muestran
		horaSalida.hidden = false;
		horaSalida.disabled = false;
		comentarioSalida.hidden=false;
		comentarioSalida.disabled = false;

	}
	
	if(seleccion == 'otro'){
		//Campos que desaparecen y se desactivan
		horaSalida.hidden = true;
		horaSalida.disabled = true;
		comentarioSalida.hidden=true;
		comentarioSalida.disabled = true;
		horaEntrada.hidden = true;
		horaEntrada.disabled = true;
		comentarioEntrada.hidden=true;
		comentarioEntrada.disabled=true;
		
		//Campos que se muestran y se activan
		tipoOtro.hidden = false;
		tipoOtro.disabled = false;
	}
	
}


/*
 * FUNCIONES PARA OBTENER FECHA, USUARIO Y MENSAJE PARA CONTROLAR LAS FUNCIONES DE LA TABLA DE INCIDENCIAS
 */

function borrarIncidencia(){
		
	$("#tablaIncidencias tr").click(function(){
	
			
			var fecha = $(this).find('td:nth-child(2)').html();
			var usuario = $(this).find('td:first').html();
			var mensaje = $(this).find('td:nth-child(4)').html();
	
			console.log(fecha + " " + usuario + " " + mensaje);
			
		});

}

function obtenerDatosFilaTabla(){

	$("#tablaIncidencias tr").click(function(){

		
		var fecha = $(this).find('td:nth-child(2)').html();
		var usuario = $(this).find('td:first').html();
		var mensaje = $(this).find('td:nth-child(4)').html();

		console.log(fecha + " " + usuario + " " + mensaje);
		
	});

}

function obtenerUsuario(){
	$("#tablaIncidencias tr").click(function(){

		alert(usuario);

	});

}


function obtenerMensaje(){

	$("#tablaIncidencias tr").click(function(){

		return mensaje;

	});

}

/*
 * JAVASCRIPT DE LA VISTA controlHorario
 */
