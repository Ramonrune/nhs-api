<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
<meta charset="utf-8">
<title>Simple Markers</title>
<style>
/* Always set the map height explicitly to define the size of the div
       * element that contains the map. */
#map {
	height: 100%;
}
/* Optional: Makes the sample page fill the window. */
html, body {
	height: 100%;
	margin: 0;
	padding: 0;
}
</style>
</head>
<body>

	<div id="map"></div>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js" type="text/javascript"></script>
	
	<script async defer
		src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBvd3Lh7LOeEM7fM1chIR2C5GA2DKjC6_E&callback=initMap">
		
	</script>	

	<script>


		function initMap() {
			var ll = {
					lat : 10,
					lng : 10
				};
	
			var map = new google.maps.Map(document.getElementById('map'), {
				zoom : 3,
				center : ll
			});

	
			 $.ajax({
		         type: 'GET',
		         url: 'webapi/healthinstitution/list?start=0&end=2147483647',
		         dataType: 'json',
		         contentType: 'application/json; charset=utf-8',
		         success: function(response) {
		        	 var jsonData = JSON.parse(JSON.stringify(response));
		        	 for (var i = 0; i < jsonData.list.length; i++) {
		        	     var counter = jsonData.list[i];
	
		        	     console.log(counter.name);
		        	 	var myLatLng = {
		        				lat :  counter.latitute,
		        				lng : counter.longitute
		        			};
		        	     var marker = new google.maps.Marker({
		     				position : myLatLng,
		     				map : map,
		     				title : 'Hello World!'
		     			});

		        	     
		        	 }
		         },
		         error: function(error) {
		             console.log(error);
		         }
		     });
		}
		
	</script>
 	
</body>
</html>