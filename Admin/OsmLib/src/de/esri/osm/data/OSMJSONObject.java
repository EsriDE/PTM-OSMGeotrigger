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
 * @author Eva Peters
 *
 */
public abstract class OSMJSONObject extends OSMJSONObjectRaw {
		
	public OSMJSONObject(JSONObject jsonObject)
	{
		super(jsonObject);
	}
	
	/**
	 * Gets the OSM tags.
	 * 
	 * @return The OSM tags.
	 * @throws GeometryReaderException If the geometry can not be read.
	 */
	public JSONObject getTagCollection() throws GeometryReaderException
	{
		JSONObject tags = null;
		try {
			tags = this.jsonObject.getJSONObject("tags");
		} catch (JSONException e) {
			throw new GeometryReaderException("Error getting tags: " + e.getMessage());
		}
		return tags;
	}
	
	/**
	 * Gets a specific OSM tag by key.
	 * 
	 * @param key The key.
	 * @return The value.
	 * @throws JSONException If the key is not found.
	 * @throws GeometryReaderException If the geometry can not be read.
	 */
	public String getTag(String key) throws JSONException, GeometryReaderException
	{
		JSONObject tags = getTags();
		return (String) tags.get(key);
	}
	
	/**
	 * Gets the ID.
	 * 
	 * @return The ID.
	 * @throws GeometryReaderException If the geometry can not be read.
	 */
	public Object getId() throws GeometryReaderException
	{
		Object id = null;
		try {
			id = this.jsonObject.get("id");
		} catch (JSONException e) {
			throw new GeometryReaderException("Error getting ID: " + e.getMessage());
		}
		return id;
	}
}
