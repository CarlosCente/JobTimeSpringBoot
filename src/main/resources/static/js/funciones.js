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


function modificarFichaje(){
	
	var urlAjax = $("#urlAjaxModificarFichaje").val();
	var usuario=$('#eUsuario').val();
    var fecha=$('#eFecha').val();
	var ipOrigen=$('#eIp').val();
	var horaDeEntrada=$('#horaEntradaControl').val();
	var horaDeSalida=$('#horaSalidaControl').val();
	
	$.ajax({
		type: "GET",
		url: urlAjax,
		data: {
			"username" : usuario,
			"fecha" : fecha,
			"ipOrigen" : ipOrigen,
			"horaDeEntrada" : horaDeEntrada,
			"horaDeSalida" : horaDeSalida
		},
		success: function(result){
			$('#modalEditar').modal('hide');
			location.reload();			
		}
	});
		
}


function abrirModalEditar(){
	
	var table = $('#tablaFichajes').DataTable();
	$('#tablaFichajes tbody').one('click', 'tr', function () {
        var data = table.row( this ).data();
        var usuario=data[0];
        var fecha=data[1];
    	var ipOrigen=data[2];
    	var horaDeEntrada=data[3];
    	var horaDeSalida=data[4];
    	
    		
    	$('#modalEditar').modal('show');
    	$('#eUsuario').val(usuario);
    	$('#eFecha').val(fecha);
    	$('#eIp').val(ipOrigen);
    	$('#horaEntradaControl').val(horaDeEntrada);
    	$('#horaSalidaControl').val(horaDeSalida);
	});

}

/*
 * Método que controla en la vista solicitudes los formularios que aparecen o desaparecen según el tipo
 * seleccionado en el selectable y se modifican los dias seleccionados segun las opciones elegidas en el formulario
 */

function cargarSolicitudSeleccionada(){
	
	var seleccion = document.getElementById("tipoSolicitud").value;
	var cardAdicionales = document.getElementById("cardDatosAdicionales");
	var cardVacacioness = document.getElementById("cardVacaciones");
	
	var desplazamiento = document.getElementById("desplazamiento");
	var fechaInicioPermiso = document.getElementById("fechaInicioPermiso");
	var inicioVacaciones = document.getElementById("inicioVacaciones");
	var finVacaciones = document.getElementById("finVacaciones");
	var diasSolicitadosVacaciones = document.getElementById("diasSolicitadosVacaciones");
	var labelInicioVacaciones = document.getElementById("labelInicioVacaciones");
	var labelFinVacaciones = document.getElementById("labelFinVacaciones");
	
	var cardDiasSolicitados = document.getElementById("cardDiasSolicitados");
	var diasSolicitados = document.getElementById("diasSolicitados");
	diasSolicitados.innerHTML = '0';
	diasSolicitadosVacaciones.innerHTML = '0 días naturales';
	
	//La tarjeta de vacaciones solo para la opción de vacaciones
	if(seleccion == '1'){
		cardVacacioness.hidden = false;
		cardAdicionales.hidden = true;
		desplazamiento.disabled = true;
		fechaInicioPermiso.disabled = true;
		inicioVacaciones.hidden = false;
		finVacaciones.hidden = false;
		inicioVacaciones.disabled = false;
		finVacaciones.disabled = false;
		labelInicioVacaciones.hidden = false;
		labelFinVacaciones.hidden = false;
	}
	
	if(seleccion == '3'){
		cardAdicionales.hidden = false;
		cardVacaciones.hidden = true;
		desplazamiento.disabled = true;
		fechaInicioPermiso.disabled = false;
		cardDiasSolicitados.hidden = false;
		inicioVacaciones.disabled = true;
		finVacaciones.disabled = true;
		diasSolicitados.innerHTML = '15 días naturales';
		
	}
	
	if(seleccion == '2' || seleccion == '4'){
		cardAdicionales.hidden = false;
		cardVacaciones.hidden = true;
		desplazamiento.disabled = false;
		fechaInicioPermiso.disabled = false;
		cardDiasSolicitados.hidden = false;
		inicioVacaciones.disabled = true;
		finVacaciones.disabled = true;
		diasSolicitados.innerHTML = '2 días laborables';
	}
	
	if(seleccion == '0'){
		cardAdicionales.hidden = true;
		cardVacaciones.hidden = true;
	}
	
	
}

function modificarDiasTotales() {
	var seleccion = document.getElementById("tipoSolicitud").value;
	var desplazamiento = document.getElementById("desplazamiento");
	var diasSolicitados = document.getElementById("diasSolicitados");
	diasSolicitados.innerHTML = '0';
	
	if(desplazamiento.checked){
		if(seleccion == '2' || seleccion == '4'){
			diasSolicitados.innerHTML = '4 días laborables';
		}
	} else{
		if(seleccion == '2' || seleccion == '4'){
			diasSolicitados.innerHTML = '2 días laborables';
		}
	}
	
}

function calcularDiasTotales() {
	var inicioVacaciones = document.getElementById("inicioVacaciones").value;
	var finVacaciones = document.getElementById("finVacaciones").value;
	var diasSolicitadosVacaciones = document.getElementById("diasSolicitadosVacaciones");
	
	if(inicioVacaciones.length == 0 || finVacaciones.length == 0){
		diasSolicitadosVacaciones.innerHTML = '0 días naturales';
	}
	
	if(inicioVacaciones.length > 0 && finVacaciones.length > 0){
		var fechaIni = new Date(inicioVacaciones);
		var fechaFin = new Date(finVacaciones);
		var diasDif = fechaFin.getTime() - fechaIni.getTime();		
		var contdias = Math.round(diasDif/(1000*60*60*24)) + 1;
		
		if(contdias > 0){
			diasSolicitadosVacaciones.innerHTML = contdias + ' días naturales';
		} else{
			diasSolicitadosVacaciones.innerHTML = '0 días naturales';
		}
		
	}

	
	
}
