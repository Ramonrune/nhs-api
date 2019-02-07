<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <!DOCTYPE html>
<!-- Styles -->
<style>
#chartdiv {
  width: 100%;
  height: 500px; 
}		

h3{
	font-family: Arial,Helvetica Neue,Helvetica,sans-serif; 
}					
</style>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js" type="text/javascript"></script>

<!-- Resources -->
<script src="https://www.amcharts.com/lib/3/amcharts.js"></script>
<script src="https://www.amcharts.com/lib/3/serial.js"></script>
<script src="https://www.amcharts.com/lib/3/plugins/export/export.min.js"></script>
<link rel="stylesheet" href="https://www.amcharts.com/lib/3/plugins/export/export.css" type="text/css" media="all" />
<script src="https://www.amcharts.com/lib/3/themes/light.js"></script>

<!-- Chart code -->
<script>
function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}
 
   
var rows =[];     
$.ajax({
    type: 'GET',
    url: 'webapi/physician/listAttendanceGraph?id_physician=' + getParameterByName('id_physician', window.location.href) + '&id_health_institution=' + getParameterByName('id_health_institution', window.location.href),
    dataType: 'json',
    contentType: 'application/json; charset=utf-8',
    success: function(response) {
   	 var jsonData = JSON.parse(JSON.stringify(response));
       

   	 console.log(jsonData.list);
   	 

   	var chart = AmCharts.makeChart( "chartdiv", {
   	  "type": "serial",
   	  "theme": "light",
   	  "marginRight": 40,
   	  "marginLeft": 40,
   	  "autoMarginOffset": 20,
   	  "dataDateFormat": "YYYY-MM-DD",
   	  "valueAxes": [ {
   	    "id": "v1",
   	    "axisAlpha": 0,
   	    "position": "left",
   	    "ignoreAxisWidth": true
   	  } ],
   	  "balloon": {
   	    "borderThickness": 1,
   	    "shadowAlpha": 0
   	  },
   	  "graphs": [ {
   	    "id": "g1",
   	    "balloon": {
   	      "drop": true,
   	      "adjustBorderColor": false,
   	      "color": "#ffffff",
   	      "type": "smoothedLine"
   	    },
   	    "fillAlphas": 0.2,
   	    "bullet": "round",
   	    "bulletBorderAlpha": 1,
   	    "bulletColor": "#FFFFFF",
   	    "bulletSize": 5,
   	    "hideBulletsCount": 50,
   	    "lineThickness": 2,
   	    "title": "red line",
   	    "useLineColorForBulletBorder": true,
   	    "valueField": "quantity",
   	    "balloonText": "<span style='font-size:18px;'>[[quantity]]</span>"
   	  } ],
   	  "chartCursor": {
   	    "valueLineEnabled": true,
   	    "valueLineBalloonEnabled": true,
   	    "cursorAlpha": 0,
   	    "zoomable": false,
   	    "valueZoomable": true,
   	    "valueLineAlpha": 0.5
   	  },
   	  "valueScrollbar": {
   	    "autoGridCount": true,
   	    "color": "#000000",
   	    "scrollbarHeight": 50
   	  },
   	  "categoryField": "date",
   	  "categoryAxis": {
   	    "parseDates": true,
   	    "dashLength": 1,
   	    "minorGridEnabled": true
   	  },
   	  "export": {
   	    "enabled": false
   	  },
   	  "dataProvider": 
   		jsonData.list
   	} );
   	 
    },
    error: function(error) {
        console.log(error);
    }
});



</script>

<h3 id="message">Attendances on this month</h3>


<script>

var lang = getParameterByName("lang", window.location.href);

if(lang == 'pt'){
	document.getElementById("message").innerText="Atendimentos neste mês";
}
</script>
<!-- HTML -->
<div id="chartdiv"></div>