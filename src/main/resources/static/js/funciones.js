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
