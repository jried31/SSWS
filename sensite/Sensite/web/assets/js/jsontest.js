var json1 = [{
    "_id": {
        "$oid": "531fb8b1e4b0c890ffc89607"
    },
    "Information": {
        "BaseQoI": {
            "DataSource": {
                "Sensor": {
                    "classification": {
                        "sensorType": "Geiger Counter"
                    }
                }
            }
        },
        "BaseData": {
            "dateTime": {
                "$date": "2013-05-23T20:32:45.000Z"
            },
            "location": {
                "lon": 34.02,
                "lat": -118.1
            },
            "metric": {
                "QuantitativeMetric": 12.32
            }
        }
    }
},

{
    "_id": {
        "$oid": "531fb8b1e4b0c890ffc89607"
    },
    "Information": {
        "BaseQoI": {
            "DataSource": {
                "Sensor": {
                    "classification": {
                        "sensorType": "Sensor1"
                    }
                }
            }
        },
        "BaseData": {
            "dateTime": {
                "$date": "2013-05-23T20:32:45.000Z"
            },
            "location": {
                "lon": 34.01,
                "lat": -118.01
            },
            "metric": {
                "QuantitativeMetric": 12.32
            }
        }
    }
},
{
    "_id": {
        "$oid": "531fb8b1e4b0c890ffc89607"
    },
    "Information": {
        "BaseQoI": {
            "DataSource": {
                "Sensor": {
                    "classification": {
                        "sensorType": "Sensor2"
                    }
                }
            }
        },
        "BaseData": {
            "dateTime": {
                "$date": "2013-05-23T20:32:45.000Z"
            },
            "location": {
                "lon": 34.03,
                "lat": -118.02
            },
            "metric": {
                "QuantitativeMetric": 12.32
            }
        }
    }
},
{
    "_id": {
        "$oid": "531fb8b1e4b0c890ffc89607"
    },
    "Information": {
        "BaseQoI": {
            "DataSource": {
                "Sensor": {
                    "classification": {
                        "sensorType": "Sensor3"
                    }
                }
            }
        },
        "BaseData": {
            "dateTime": {
                "$date": "2013-05-23T20:32:45.000Z"
            },
            "location": {
                "lon": 34.01,
                "lat": -117.99
            },
            "metric": {
                "QuantitativeMetric": 12.32
            }
        }
    }
}]

function displayme()
{
    for (var key in json)
    {
        if (json.hasOwnProperty(key)) 
       {
        alert(json1[key].Information.BaseQoI.DataSource.Sensor.classification.sensorType);
        alert(json1[key].Information.BaseData.location.lon);
        alert(json1[key].Information.BaseData.metric.QuantitativeMetric);
        alert(json1[key].Information.BaseData.dateTime.$date);
       }
    }
 //   alert(json[key].Information.BaseData.location.lon;

}