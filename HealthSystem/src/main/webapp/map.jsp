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
	<script>
		function findGetParameter(parameterName) {
			var result = null, tmp = [];
			location.search.substr(1).split("&").forEach(function(item) {
				tmp = item.split("=");
				if (tmp[0] === parameterName)
					result = decodeURIComponent(tmp[1]);
			});
			return result;
		}

		function initMap() {

			var lat1 = parseFloat(findGetParameter("lat"));
			var lng1 = parseFloat(findGetParameter("lng"));

			console.log(lng1);
			var myLatLng = {
				lat : lat1,
				lng : lng1
			};

			var map = new google.maps.Map(document.getElementById('map'), {
				zoom : 15,
				center : myLatLng
			});

			var marker = new google.maps.Marker({
				position : myLatLng,
				map : map,
				title : 'Hello World!'
			});
		}
	</script>
	<script async defer
		src="https://maps.googleapis.com/maps/api/js?key=&callback=initMap">
		
	</script>
</body>
</html>
