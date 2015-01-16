package de.esri.osm.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.esri.geotrigger.core.TriggerBuilder;

/**
 * Represents a JSON feature object for ArcGIS.
 * used for ArcGIS REST requests such as in:
 * 
 * http://resources.arcgis.com/en/help/arcgis-rest-api/index.html#/Feature_object/02r3000000n8000000/
 * 
 * Example:
 * 
 * {
      "attributes":{
         "KeyValueAll":"{\"osmid\":296231135,\"layer\":\"FIXME_Points_Test\",\"tags\":{\"wheelchair\":\"yes\",\"fixme\":\"genaue Position\",\"name\":\"Türkenstraße\",\"public_transport\":\"stop_position\",\"highway\":\"bus_stop\",\"operator\":\"MVG\",\"bus_routes\":\"154; 153\"}}",
         "fixme":"genaue Position",
         "OSMID":296231135,
         "name":"Türkenstraße",
         "highway":"bus_stop",
         "osmorgurl":"http://www.openstreetmap.org/node/296231135"
      },
      "geometry":{
         "x":11.5757082,
         "y":48.1503477,
         "spatialReference":{
            "wkid":4326
         }
      }
   }
 * 
 * @author evp
 *
 */
public abstract class ArcGISJSONObject extends JSONObject {
	private static Logger log = LogManager.getLogger(ArcGISJSONObject.class.getName());
		
	protected JSONObject attributes;
	
	protected JSONObject geometry;
	
	
	public ArcGISJSONObject()
	{
		super();
		
		this.attributes = new JSONObject();
		this.geometry = new JSONObject();
		
		try {
			this.put("attributes", this.attributes);
			this.put("geometry", this.geometry);
			
			JSONObject jsonObjectWkid = new JSONObject();
			jsonObjectWkid.put("wkid", 4326);	
			this.geometry.put("spatialReference", jsonObjectWkid);
		} catch (JSONException e) {
			log.error("Could not set attributes: " + e.getMessage());
		}
	}
	
	/**
	 * Adds an attribute.
	 * 
	 * @param fieldName The field name.
	 * @param value The name as Object.
	 */
	public void addAttributeField(String fieldName, Object value)
	{
		try {
			this.attributes.put(fieldName, value);
		} catch (JSONException e) {
			log.error("Could not add attribute field: " + e.getMessage());
		}
	}
	
	/**
	 * Adds an attribute.
	 * 
	 * @param fieldName The field name.
	 * @param value The name as String.
	 */
	public void addAttributeField(String fieldName, String value)
	{
		try {
			this.attributes.put(fieldName, value);
		} catch (JSONException e) {
			log.error("Could not add attribute field: " + e.getMessage());
		}
	}
	
	/**
	 * Adds an attribute.
	 * 
	 * @param fieldName The field name.
	 * @param value The name as JSONObject.
	 */
	public void addAttributeField(String fieldName, JSONObject value)
	{
		try {
			this.attributes.put(fieldName, value);
		} catch (JSONException e) {
			log.error("Could not add attribute field: " + e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see org.json.JSONObject#toString()
	 */
//	public String toString()
//	{		
//		JSONArray jsonArray = new JSONArray();
//		jsonArray.put(this);
//		String json = jsonArray.toString();
//		
//		return json;
//	}
}
