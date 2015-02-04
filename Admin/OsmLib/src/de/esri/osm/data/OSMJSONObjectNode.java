package de.esri.osm.data;

import org.json.JSONObject;

/**
 * Represents a JSON node for OSM.
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
public class OSMJSONObjectNode extends OSMJSONObject 
{
	private OSMJSONPoint osmjsonPoint;
	
	public OSMJSONObjectNode(JSONObject jsonObject)
	{
		super(jsonObject);
		this.osmjsonPoint = new OSMJSONPoint(jsonObject);
	}
	
	/**
	 * Gets the point geometry.
	 * 
	 * @return The point geometry.
	 */
	public OSMJSONPoint getPointGeometry()
	{
		return this.osmjsonPoint;
	}
}
