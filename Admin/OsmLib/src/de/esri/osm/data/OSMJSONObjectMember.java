package de.esri.osm.data;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Represents a JSON way for OSM.
 * retrieved by an Overpass request.
 * 
 * Example:
 * 
 * http://www.overpass-api.de/api/interpreter?data=[out:json];relation%2851.3928,7.0827,51.5498,7.3313%29[admin_level=10];out%20geom;
 *
 * 	{
  	"type": "relation",
	  "id": 3190475,
	  "bounds": {
	    "minlat": 51.4188709,
	    "minlon": 7.0564331,
	    "maxlat": 51.4431181,
	    "maxlon": 7.0895834
	  },
	  "members": [
	    {
	      "type": "way",
	      "ref": 237043137,
	      "role": "outer",
	      "geometry": [
	         { "lat": 51.4427076, "lon": 7.0810691 },
	         { "lat": 51.4420324, "lon": 7.0825276 },
					...
	         { "lat": 51.4303262, "lon": 7.0895834 }
	      ]
	    },
	    {
	      "type": "way",
	      "ref": 236046185,
	      "role": "outer",
	      "geometry": [
	         { "lat": 51.4396677, "lon": 7.0680531 },
					...
	         { "lat": 51.4427076, "lon": 7.0810691 }
	      ]
	    },
			...
	    {
	      "type": "way",
	      "ref": 237144990,
	      "role": "outer",
	      "geometry": [
	         { "lat": 51.4188709, "lon": 7.0713332 },
						...
	         { "lat": 51.4303262, "lon": 7.0895834 }
	      ]
	    }
	  ],
	  "tags": {
	    "admin_level": "10",
	    "boundary": "administrative",
	    "name": "Überruhr-Hinsel",
	    "ref": "43",
	    "source": "http://de.wikipedia.org/wiki/Liste_der_Stadtbezirke_und_Stadtteile_von_Essen",
	    "type": "boundary",
	    "wikipedia": "de:Überruhr"
	  }
	}
 * 
 * @author Eva Peters
 *
 */
public class OSMJSONObjectMember extends OSMJSONObjectRaw 
{
	private JSONArray vertices;
	
	public OSMJSONObjectMember(JSONObject jsonObject)
	{
		super(jsonObject);
		this.vertices = this.jsonObject.getJSONArray("geometry");
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
	 */
	public OSMJSONPoint getVertex(int index)
	{
		JSONObject vertex = this.vertices.getJSONObject(index);
		OSMJSONPoint osmjsonPoint = new OSMJSONPoint(vertex);
		
		return osmjsonPoint;
	}
	
	/**
	 * Gets the last vertex.
	 * 
	 * @return The last vertex.
	 */
	public OSMJSONPoint getLastVertex()
	{
		JSONObject vertex = this.vertices.getJSONObject(getGeometryLength() - 1);
		OSMJSONPoint osmjsonPoint = new OSMJSONPoint(vertex);
		
		return osmjsonPoint;
	} 
	
	/**
	 * Reverses the vertices.
	 * 
	 * The original order is overwritten.
	 */
	public void reserveVertices()
	{
		JSONArray verticesTemp = new JSONArray();
		
		for(int i = (getGeometryLength() - 1); i >= 0; i--)
		{
			verticesTemp.put(this.vertices.getJSONObject(i));
		}
		
		this.vertices = verticesTemp;
	}
	
	/**
	 * Gets the ref.
	 * 
	 * @return The ref.
	 */
	public Object getRef()
	{
		return this.jsonObject.get("ref");
	}
	
	/**
	 * Gets the role.
	 * 
	 * @return The role.
	 */
	public Object getRole()
	{
		return this.jsonObject.get("role");
	}
}
