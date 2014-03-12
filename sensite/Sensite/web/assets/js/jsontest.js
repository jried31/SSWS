var json1 = {"Informations": [
    {
        "BaseQoI": {"DataSource": {"Sensor": {"classification": {"sensorType": "transducer"}}}},
        "BaseData": {
            "location": {
                "lon": -34,
                "lat": 23
            },
            "dateTime": "2013-05-23T13:32:45Z",
            "metric": {"QuantitativeMetric": 12.32}
        },
        "relevancy": 192
    },
    {
        "BaseQoI": {"DataSource": {"Sensor": {"classification": {"sensorType": "rain gauge"}}}},
        "BaseData": {
            "location": {
                "lon": -34,
                "lat": 23
            },
            "dateTime": "2013-05-23T13:32:45Z",
            "metric": {"QuantitativeMetric": 12.32}
        },
        "relevancy": 208
    }
]}

function displayme()
{
    alert(json1.Informations[0].BaseData.metric.QuantitativeMetric);
 //   alert(json[key].Information.BaseData.location.lon;

}