package de.esri.osm.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents a JSON way for OSM.
 * retrieved by an Overpass request.
 * 
 * Example:
 * 
 * http://www.overpass-api.de/api/interpreter?data=[out:json];way(48.0,11.2,48.7,11.7)[highway=residential][fixme];out%20geom;
 *
 * 	{
	  "type": "way",
	  "id": 170350811,
	  "bounds": {
	    "minlat": 48.6974111,
	    "minlon": 11.4253163,
	    "maxlat": 48.6982465,
	    "maxlon": 11.4264920
	  },
	  "nodes": [
	    320449735,
	    1815016575,
	    1815016577,
	    1815016586,
	    1815016592,
	    1815016613
	  ],
	  "geometry": [
	    { "lat": 48.6974111, "lon": 11.4264920 },
	    { "lat": 48.6976261, "lon": 11.4261808 },
	    { "lat": 48.6977506, "lon": 11.4259651 },
	    { "lat": 48.6978942, "lon": 11.4257225 },
	    { "lat": 48.6980353, "lon": 11.4255391 },
	    { "lat": 48.6982465, "lon": 11.4253163 }
	  ],
	  "tags": {
	    "fixme": "name",
	    "highway": "residential",
	    "maxspeed": "30",
	    "source": "HiRes aerial imagery"
	  }
	}
 * 
 * @author Eva Peters
 *
 */
public class OSMJSONObjectWay extends OSMJSONObject 
{
	private JSONArray vertices;
	
	/**
	 * Constructor.
	 * 
	 * @param jsonObject The OSM JSON way.
	 * @throws GeometryReaderException If the geometry can not be read.
	 */
	public OSMJSONObjectWay(JSONObject jsonObject) throws GeometryReaderException
	{
		super(jsonObject);
		try {
			this.vertices = this.jsonObject.getJSONArray("geometry");
		} catch (JSONException e) {
			throw new GeometryReaderException("Error getting geometry: " + e.getMessage());
		}
	}
	
	/**
	 * Gets the number of vertices.
	 * 
	 * @return The number of vertices.
	 */
	public int getGeometryLength()
	{
		return this.vertices.length();
	}
	
	/**
	 * Gets the vertex at the given index.
	 * 
	 * @param index The index.
	 * @return The vertex.
	 * @throws GeometryReaderException If the geometry can not be read.
	 */
	public OSMJSONPoint getVertex(int index) throws GeometryReaderException
	{
		JSONObject vertex = null;
		try {
			vertex = this.vertices.getJSONObject(index);
		} catch (JSONException e) {
			throw new GeometryReaderException("Error getting index: " + e.getMessage());
		}
		OSMJSONPoint osmjsonPoint = new OSMJSONPoint(vertex);
		
		return osmjsonPoint;
	}
}
