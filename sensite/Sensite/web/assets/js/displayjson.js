
var allow;
var json;
$(window).load(function(){
    alert("loaded");
  $('.hideme').find('div').hide();
$('.clickme').click(function() {
  alert("click");

    $(this).parent().next('.hideme').find('div').slideToggle(400);     
    });
    });

function sendToParse(phenomenon1, latitude1, longitude1, date1, time1){

     $.ajax( {type: "GET", url: 'https://api.mongolab.com/api/1/databases/sensite/collections/phenomena?q={%22phenomena%22:"'+phenomenon1+'"}&c=true&apiKey=WQ-p4-Hc9W3Zoc05iAxqbvY8uib5UW_o', dataType:'json'})
             .success(function(data){ 
         allow = data;

     });
        
         var time = date1+"_"+time1+":00";
         $.ajax( {type: "GET", url: 'http://localhost:8083/sensite/getObservations', data: {phenomena: 'rain', longitude: '23', latitude: '-34', time: '2013-05-23_13:32:45'}, dataType: 'json'})
         .done(function( data ) {
            json = data;
        });  
    
  setTimeout(function(){  
  $("#errormsg").html("");
  $("#mapcontainer").html("");
  $("#tabledisplay").html("");
  
  if(allow == 0)  
  {
      $("#errormsg").html("<h2><center><span style='color:red'>You have entered a phenomenon that does not exist. </span></center></h2>");
     return;
  }
      
  
  if($("#lat").val()== "")
  {
     $("#errormsg").html("<h2><center><span style='color:red'>You have entered an invalid latitude. </span></center></h2>");
     return;
  }
  if($("#long").val()== "")
  {
     $("#errormsg").html("<h2><center><span style='color:red'>You have entered an invalid longitude. </span></center></h2>");
     return;
  }
  if($("#date").val().length != 10)
  {
     $("#errormsg").html("<h2><center><span style='color:red'>You have entered an invalid date. </span></center></h2>");
     return;
  }
  if($("#time").val() =="")
  {
     $("#errormsg").html("<h2><center><span style='color:red'>You have entered an invalid time. </span></center></h2>");
     return;
  }

 $("#mapcontainer").html("<div id='map-canvas' style='height:500px; width:1100px' align='center'></div>");
// initialize();
 $("#tabledisplay").append("<br><style type='text/css'>.tftable {font-size:12px;color:#333333;width:100%;border-width: 1px;border-color: #729ea5;border-collapse: collapse;}.tftable th {font-size:12px;background-color:#acc8cc;border-width: 1px;padding: 8px;border-style: solid;border-color: #729ea5;text-align:left;}.tftable tr {background-color:#d4e3e5;}.tftable td {font-size:12px;border-width: 1px;padding: 8px;border-style: solid;border-color: #729ea5;}.tftable tr:hover {background-color:#ffffff;}</style><table id ='heytable' class='tftable' border='1'><tr><th>Sensor Type</th><th>Data Reading</th><th>Latitude</th><th>Longitude</th><th>Datetime</th></tr></table>");
  for (var key = 0; key < json1.Informations.length; key++) 
  {   
      //alert(json.Informations[0].BaseQoI.DataSource.Sensor.classification.sensorType);
       $("#heytable").append("<tr><td class = 'clickme'>" +json1.Informations[key].BaseQoI.DataSource.Sensor.classification.sensorType+" </td><td>"+json1.Informations[key].BaseData.metric.QuantitativeMetric+"</td><td>"+json1.Informations[key].BaseData.location.lat+"</td><td>"+json1.Informations[key].BaseData.location.lon+"</td><td>"+json1.Informations[key].BaseData.dateTime+"</td></tr><tr class='hideme'><td colspan='5' style='padding:0px;'><div>json</div></td></tr>");  

  } 

  $('.hideme').find('div').hide();
$('.clickme').click(function() {
  alert("cliuuuuck");

    $(this).parent().next('.hideme').find('div').slideToggle(400);     
    });

  },1000);
}