var json = [
        {"sensor": "sensor1", "data": "data1", "latitude": "34", "longitude": "-118", "datetime": "datetime1"},
        {"sensor": "sensor2", "data": "data2", "latitude": "34.01", "longitude": "-118.01", "datetime": "datetime2"},
        {"sensor": "sensor3", "data": "data3", "latitude": "34", "longitude": "-118.02", "datetime": "datetime3"},
        {"sensor": "sensor4", "data": "data4", "latitude": "34.01", "longitude": "-118.02", "datetime": "datetime4"},
        {"sensor": "sensor5", "data": "data5", "latitude": "34", "longitude": "-117.99", "datetime": "datetime5"},
        {"sensor": "sensor6", "data": "data6", "latitude": "34.01", "longitude": "-117.99", "datetime": "datetime6"}
    ];
var allow;
var jsonreturn;

function sendToParse(phenomenon1, latitude1, longitude1, date1, time1){
     $.ajax( {type: "GET", url: 'https://api.mongolab.com/api/1/databases/sensite/collections/phenomena?q={%22phenomena%22:"'+phenomenon1+'"}&c=true&apiKey=WQ-p4-Hc9W3Zoc05iAxqbvY8uib5UW_o', dataType:'json'}).done(function(data)
     { 
         allow = data;
     });
          
    if(allow !=0){
            
     
         var time = date1+"_"+time1+":00";
         $.ajax( {type: "GET", url: 'http://localhost:8082/sensite/getObservations', data: {phenomena: 'rain', longitude: longitude1, latitude: latitude1, time: time}, dataType: 'json'})
         .done(function( data ) {
            jsonreturn = data;
       //console.log(data);
        });  
    }
}




function updateJSONTable()
{
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
  initialize();

  $("#tabledisplay").append("<br><style type='text/css'>.tftable {font-size:12px;color:#333333;width:100%;border-width: 1px;border-color: #729ea5;border-collapse: collapse;}.tftable th {font-size:12px;background-color:#acc8cc;border-width: 1px;padding: 8px;border-style: solid;border-color: #729ea5;text-align:left;}.tftable tr {background-color:#d4e3e5;}.tftable td {font-size:12px;border-width: 1px;padding: 8px;border-style: solid;border-color: #729ea5;}.tftable tr:hover {background-color:#ffffff;}</style><table id ='heytable' class='tftable' border='1'><tr><th>Sensor Type</th><th>Data Reading</th><th>Latitude</th><th>Longitude</th><th>Datetime</th></tr></table>");

  for (var key in json) 
  {
       if (json.hasOwnProperty(key)) 
       {
          $("#heytable").append("<tr><td>" +json[key].sensor+" </td><td>"+json[key].data+"</td><td>"+json[key].latitude+"</td><td>"+json[key].longitude+"</td><td>"+json[key].datetime+"</td></tr>");  
       }
  }
}



