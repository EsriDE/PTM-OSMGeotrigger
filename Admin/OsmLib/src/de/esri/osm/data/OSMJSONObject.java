package de.esri.osm.data;

import org.json.JSONException;
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
 * @author evp
 *
 */
public abstract class OSMJSONObject extends JSONObject {
	
	protected JSONObject jsonObject;
		
	public OSMJSONObject(JSONObject jsonObject)
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
	
	/**
	 * Gets a specific OSM tag by key.
	 * 
	 * @param key The key.
	 * @return The value.
	 * @throws JSONException If the key is not found.
	 */
	public String getTag(String key) throws JSONException
	{
		JSONObject tags = getTags();
		return (String) tags.get(key);
	}
	
	/**
	 * Gets the ID.
	 * 
	 * @return The ID.
	 */
	public Object getId()
	{
		return this.jsonObject.get("id");
	}
}
