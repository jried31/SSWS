var myJSONObject = {"Information": [
        {"sensor": "sensor1", "data": "data1", "location": "location1", datetime: "datetime1"},
        {"sensor": "sensor2", "data": "data2", "location": "location2", datetime: "datetime2"},
        {"sensor": "sensor3", "data": "data3", "location": "location3", datetime: "datetime3"},
        {"sensor": "sensor4", "data": "data4", "location": "location4", datetime: "datetime4"},
        {"sensor": "sensor5", "data": "data5", "location": "location5", datetime: "datetime5"},
        {"sensor": "sensor6", "data": "data6", "location": "location6", datetime: "datetime6"}
    ]
};

function helloworld()
{
	alert("hello world");
}

function displaytable()
{
	$.each(myJSONObject.items, function()
	{

		alert(this.sensor);
	})
}

