package de.esri.osm.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * Represents a JSON point for OSM (e.g. vertex).
 * retrieved by an Overpass request.
 * 
 * The jsonObject must at least contain "lat" and "lon".
 * 
 * Example 1 (vertex from an OSM way):
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
 * @author Eva Peters
 *
 */
public class OSMJSONPoint 
{
	private JSONObject jsonObject;
	
	/**
	 * Constructor.
	 * 
	 * The jsonObject must at least contain "lat" and "lon" (see above).
	 * 
	 * @param jsonObject The JSON object.
	 */
	public OSMJSONPoint(JSONObject jsonObject)
	{
		this.jsonObject = jsonObject;
	}
	
	/**
	 * Gets the latitude.
	 * 
	 * @return The latitude.
	 * @throws GeometryReaderException If the geometry can not be read.
	 */
	public double getLatitude() throws GeometryReaderException
	{
		double latitude = 0.0;
		try {
			latitude = (Double) this.jsonObject.get("lat");
		} catch (JSONException e) {
			throw new GeometryReaderException("Error getting latitude: " + e.getMessage());
		}
		return latitude;
	}
	
	/**
	 * Gets the longitude.
	 * 
	 * @return The longitude.
	 * @throws GeometryReaderException If the geometry can not be read.
	 */
	public double getLongitude() throws GeometryReaderException
	{
		double longitude = 0.0;
		try {
			longitude = (Double) this.jsonObject.get("lon");
		} catch (JSONException e) {
			throw new GeometryReaderException("Error getting longitude: " + e.getMessage());
		}
		return longitude;
	}
}
