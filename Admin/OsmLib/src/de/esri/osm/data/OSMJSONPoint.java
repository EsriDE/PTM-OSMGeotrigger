package de.esri.osm.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class OSMJSONPoint 
{
	private static Logger log = LogManager.getLogger(OSMJSONPoint.class.getName());
	private JSONObject jsonObject;
	
	/**
	 * Constructor.
	 * 
	 * The jsonObject must at least contain "lat" and "lon".
	 * 
	 * Example1 (vertex from an OSM way):
	 * 
	 * 	{ "lat": 48.6974111, "lon": 11.4264920 }
	 * 
	 * 
	 * Example 2 (OSM node):
	 * 
	 * {
		 "lon":11.5757082,
		 "id":296231135,
		 "type":"node",
		 "lat":48.1503477,
		 "tags":{
			"wheelchair":"yes",
			"fixme":"genaue Position",
			"name":"Türkenstraße",
			"public_transport":"stop_position",
			"highway":"bus_stop",
			"operator":"MVG",
			"bus_routes":"154; 153"
		 }
		}
	 * 
	 * @param jsonObject
	 */
	public OSMJSONPoint(JSONObject jsonObject)
	{
		this.jsonObject = jsonObject;
	}
	
	/**
	 * Gets the latitude.
	 * 
	 * @return The latitude.
	 */
	public double getLatitude()
	{
		double latitude = 0.0;
		try {
			latitude = (Double) this.jsonObject.get("lat");
		} catch (JSONException e) {
			log.error("Error getting latitude: " + e.getMessage());
		}
		return latitude;
	}
	
	/**
	 * Gets the longitude.
	 * 
	 * @return The longitude.
	 */
	public double getLongitude()
	{
		double longitude = 0.0;
		try {
			longitude = (Double) this.jsonObject.get("lon");
		} catch (JSONException e) {
			log.error("Error getting longitude: " + e.getMessage());
		}
		return longitude;
	}
}
