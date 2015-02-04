package de.esri.osm.data;

import org.json.JSONObject;

/**
 * Represents a JSON object for OSM 
 * retrieved by an Overpass request.
 * 
 * Example:
 * 
 * http://www.overpass-api.de/api/interpreter?data=[out:json];node(48.0,11.2,48.7,11.7)[highway][fixme];out geom;
 * 
 * {
	  "type": "node",
	  "id": 296231135,
	  "lat": 48.1503477,
	  "lon": 11.5757082,
	  "tags": {
	    "bus_routes": "154; 153",
	    "fixme": "genaue Position",
	    "highway": "bus_stop",
	    "name": "Türkenstraße",
	    "operator": "MVG",
	    "public_transport": "stop_position",
	    "wheelchair": "yes"
	  }
}
 * 
 * @author Eva Peters
 *
 */
public abstract class OSMJSONObjectRaw extends JSONObject {
	
	protected JSONObject jsonObject;
		
	public OSMJSONObjectRaw(JSONObject jsonObject)
	{
		super();
		
		this.jsonObject = jsonObject;
	}
	
	/**
	 * Gets the OSM type.
	 * 
	 * @param jsonObject The OSM object as JSON.
	 * @return The OSM type.
	 */
	public static String getType(JSONObject jsonObject)
	{
		return (String) jsonObject.get("type");
	}
	
	/**
	 * Gets the OSM type.
	 * 
	 * @return The OSM type.
	 */
	public String getType()
	{
		return getType(this.jsonObject);
	}
	
	/**
	 * Gets the OSM tags.
	 * 
	 * @return The OSM tags.
	 */
	public JSONObject getTags()
	{
		return this.jsonObject.getJSONObject("tags");
	}
}
