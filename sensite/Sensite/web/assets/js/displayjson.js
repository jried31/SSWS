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
     $.ajax( {type: "GET", url: 'https://api.mongolab.com/api/1/databases/sensite/collections/phenomena?q={%22phenomena%22:"'+phenomenon1+'"}&c=true&apiKey=WQ-p4-Hc9W3Zoc05iAxqbvY8uib5UW_o', dataType:'json'})
             .success(function(data){ 
         allow = data;

     });
        
        
        setTimeout(function(){
                
          if(allow == "undefined")
               allow= 0;
    if(allow !=0){
            
     
         var time = date1+"_"+time1+":00";
         $.ajax( {type: "GET", url: 'http://localhost:8082/sensite/getObservations', data: {phenomena: 'rain', longitude: longitude1, latitude: latitude1, time: time}, dataType: 'json'})
         .done(function( data ) {
            jsonreturn = data;
       //console.log(data);
        });  
    }
    
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
  } },500);
}

function updateJSONTableTest() {
    $('#tabledisplayTest').html( '<table cellpadding="0" cellspacing="0" border="0" class="display" id="example"></table>' );
    $('#example').dataTable( {
        "aaData": [
            /* Reduced data set */
            [ "Trident", "Internet Explorer 4.0", "Win 95+", 4, "X" ],
            [ "Trident", "Internet Explorer 5.0", "Win 95+", 5, "C" ],
            [ "Trident", "Internet Explorer 5.5", "Win 95+", 5.5, "A" ],
            [ "Trident", "Internet Explorer 6.0", "Win 98+", 6, "A" ],
            [ "Trident", "Internet Explorer 7.0", "Win XP SP2+", 7, "A" ],
            [ "Gecko", "Firefox 1.5", "Win 98+ / OSX.2+", 1.8, "A" ],
            [ "Gecko", "Firefox 2", "Win 98+ / OSX.2+", 1.8, "A" ],
            [ "Gecko", "Firefox 3", "Win 2k+ / OSX.3+", 1.9, "A" ],
            [ "Webkit", "Safari 1.2", "OSX.3", 125.5, "A" ],
            [ "Webkit", "Safari 1.3", "OSX.3", 312.8, "A" ],
            [ "Webkit", "Safari 2.0", "OSX.4+", 419.3, "A" ],
            [ "Webkit", "Safari 3.0", "OSX.4+", 522.1, "A" ]
        ],
        "aoColumns": [
            { "sTitle": "Engine" },
            { "sTitle": "Browser" },
            { "sTitle": "Platform" },
            { "sTitle": "Version", "sClass": "center" },
            { "sTitle": "Grade", "sClass": "center" }
        ]
    } );   
}

