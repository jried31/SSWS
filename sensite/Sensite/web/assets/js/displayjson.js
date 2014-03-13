
var allow;
var json;
$(window).load(function(){
    
  $('.hideme').find('div').hide();
$('.clickme').click(function() {
 

    $(this).parent().next('.hideme').find('div').slideToggle(400);     
    });
    });

function sendToParse(phenomenon1, latitude1, longitude1, date1, time1){

     $.ajax( {type: "GET", url: 'https://api.mongolab.com/api/1/databases/sensite/collections/phenomena?q={%22phenomena%22:"'+phenomenon1+'"}&c=true&apiKey=WQ-p4-Hc9W3Zoc05iAxqbvY8uib5UW_o', dataType:'json'})
             .success(function(data){ 
         allow = data;

     });
        
         var time = date1+"_"+time1+":00";
         $.ajax( {type: "GET", url: 'http://localhost:8082/sensite/getObservations', data: {phenomena: phenomenon1, longitude: latitude1, latitude: '-34', time: time}, dataType: 'json'})
         .done(function( data ) {
            json = data;
           // alert(json.Informations.length);
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

 $("#mapcontainer").html("<div id='map-canvas' style='height:500px; width:100%' align='center'></div>");
 initialize();
 $("#tabledisplay").append("<br><table id ='heytable' class='tftable' border='1'><tr><th>Expand</th><th>Sensor Type</th><th>Data Reading</th><th>Latitude</th><th>Longitude</th><th>Datetime</th></tr></table>");
  for (var key = 0; key < json.Informations.length; key++) 
  {   

      var j = JSON.stringify(json.Informations[key]);
      //alert(j);
       $("#heytable").append("<tr><td class = 'clickme'><center><h5><b>+</b></h5></center></td><td>" +json.Informations[key].BaseQoI.DataSource.Sensor.classification.sensorType+" </td><td>"+json.Informations[key].BaseData.metric.QuantitativeMetric+"</td><td>"+json.Informations[key].BaseData.location.lat+"</td><td>"+json.Informations[key].BaseData.location.lon+"</td><td>"+json.Informations[key].BaseData.dateTime+"</td></tr><tr class='hideme'><td colspan='6' style='padding:0px;'><div>"+j+"</div></td></tr>");  

  } 

  $('.hideme').find('div').hide();
$('.clickme').click(function() {

    $(this).parent().next('.hideme').find('div').slideToggle(400);     
    });

  },1000);
}