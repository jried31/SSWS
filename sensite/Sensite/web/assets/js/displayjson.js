var json = [
        {"sensor": "sensor1", "data": "data1", "location": "location1", datetime: "datetime1"},
        {"sensor": "sensor2", "data": "data2", "location": "location2", datetime: "datetime2"},
        {"sensor": "sensor3", "data": "data3", "location": "location3", datetime: "datetime3"},
        {"sensor": "sensor4", "data": "data4", "location": "location4", datetime: "datetime4"},
        {"sensor": "sensor5", "data": "data5", "location": "location5", datetime: "datetime5"},
        {"sensor": "sensor6", "data": "data6", "location": "location6", datetime: "datetime6"}
    ];


function updateJSONTable()
{
  $("#mapcontainer").html("<div id='map-canvas' style='height:500px; width:1100px' align='center'></div>");
  initialize();
  $("#tabledisplay").html("");
  $("#tabledisplay").append("<br><style type='text/css'>.tftable {font-size:12px;color:#333333;width:100%;border-width: 1px;border-color: #729ea5;border-collapse: collapse;}.tftable th {font-size:12px;background-color:#acc8cc;border-width: 1px;padding: 8px;border-style: solid;border-color: #729ea5;text-align:left;}.tftable tr {background-color:#d4e3e5;}.tftable td {font-size:12px;border-width: 1px;padding: 8px;border-style: solid;border-color: #729ea5;}.tftable tr:hover {background-color:#ffffff;}</style><table id ='heytable' class='tftable' border='1'><tr><th>Sensor Type</th><th>Data Reading</th><th>Location</th><th>Datetime</th></tr></table>");

  for (var key in json) 
  {
       if (json.hasOwnProperty(key)) 
       {
          $("#heytable").append("<tr><td>" +json[key].sensor+" </td><td>"+json[key].data+"</td><td>"+json[key].location+"</td><td>"+json[key].datetime+"</td></tr>");  
       }
  }
}



