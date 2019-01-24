$(document).ready(function() {
    
	$("#checkZipCode").click(function() {
		$.ajax({
			url: "/api/v1/wind/"+ $("#zipcode").val(),
			method: "GET",
		}).done(function(result) {
			$("#zipcode-output").text(JSON.stringify(result));
	    }).fail(function(error) {
	    	console.log(error);
	    });
	});
	
	$("#checkGeolocation").click(function() {
		if (navigator.geolocation) {
			navigator.geolocation.getCurrentPosition(getPosition);
		} else {
			$("#geolocation-output").text("Your browser does not support Geolocation");
		}
	});
    
});

function getPosition(position) {
	var geoLoc = {
		    latitude:  position.coords.latitude,
		    longitude: position.coords.longitude
	};
	
	$.ajax({
		url: "/api/v1/wind/geo",
		method: "GET",
		data: geoLoc
	}).done(function(result) {
		$("#geolocation-output").text(JSON.stringify(result));
    }).fail(function(error) {
    	console.log(error);
    });
	
	return;
}