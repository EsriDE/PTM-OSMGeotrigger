package de.esri.osm.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
         "data":"{\"osmid\":296231135,\"layer\":\"FIXME_Points_Test\",\"tags\":{\"wheelchair\":\"yes\",\"fixme\":\"genaue Position\",\"name\":\"Türkenstraße\",\"public_transport\":\"stop_position\",\"highway\":\"bus_stop\",\"operator\":\"MVG\",\"bus_routes\":\"154; 153\"}}",
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
 * @author Eva Peters
 *
 */
public abstract class ArcGISJSONObject extends JSONObject {
		
	protected JSONObject attributes;
	
	protected JSONObject geometry;
	
	
	/**
	 * Constructor.
	 * 
	 * @throws GeometryGenerationException If the geometry can not be generated.
	 */
	public ArcGISJSONObject() throws GeometryGenerationException
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
			throw new GeometryGenerationException("Could not set attributes: " + e.getMessage());
		}
	}
	
	/**
	 * Adds an attribute.
	 * 
	 * @param fieldName The field name.
	 * @param value The name as Object.
	 * @throws GeometryGenerationException If the geometry can not be generated.
	 */
	public void addAttributeField(String fieldName, Object value) throws GeometryGenerationException
	{
		try {
			this.attributes.put(fieldName, value);
		} catch (JSONException e) {
			throw new GeometryGenerationException("Could not add attribute field: " + e.getMessage());
		}
	}
	
	/**
	 * Adds an attribute.
	 * 
	 * @param fieldName The field name.
	 * @param value The name as String.
	 * @throws GeometryGenerationException If the geometry can not be generated.
	 */
	public void addAttributeField(String fieldName, String value) throws GeometryGenerationException
	{
		try {
			this.attributes.put(fieldName, value);
		} catch (JSONException e) {
			throw new GeometryGenerationException("Could not add attribute field: " + e.getMessage());
		}
	}
	
	/**
	 * Adds an attribute.
	 * 
	 * @param fieldName The field name.
	 * @param value The name as JSONObject.
	 * @throws GeometryGenerationException If the geometry can not be generated.
	 */
	public void addAttributeField(String fieldName, JSONObject value) throws GeometryGenerationException
	{
		try {
			this.attributes.put(fieldName, value);
		} catch (JSONException e) {
			throw new GeometryGenerationException("Could not add attribute field: " + e.getMessage());
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


	
	/**
	 * Gets the X value / longitude of the given coordinate.
	 * 
	 * @param jsonArray The coordinate, e.g. vertex.
	 * @return The X value / longitude.
	 */
	public static double getXLongitude(JSONArray jsonArray)
	{
		return (Double)jsonArray.get(0);
	}
	
	/**
	 * Gets the Y value / latitude of the given coordinate.
	 * 
	 * @param jsonArray The coordinate, e.g. vertex.
	 * @return The Y value / latitude.
	 */
	public static double getYLatitude(JSONArray jsonArray)
	{
		return (Double)jsonArray.get(1);
	}
	
}
