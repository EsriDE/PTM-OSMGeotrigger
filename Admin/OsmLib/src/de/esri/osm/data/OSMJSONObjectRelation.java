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
public class OSMJSONObjectRelation extends OSMJSONObject 
{
	private JSONArray members;
	
	public OSMJSONObjectRelation(JSONObject jsonObject)
	{
		super(jsonObject);
		
		this.members = this.jsonObject.getJSONArray("members");
	}
	
	/**
	 * Gets the number of members.
	 * 
	 * @return The number of members.
	 */
	public int getMemberLength()
	{
		return this.members.length();
	}
	
	/**
	 * Gets the member at the given index.
	 * 
	 * @param index The index.
	 * @return The member.
	 */
	public OSMJSONObjectMember getMember(int index)
	{
		JSONObject member = this.members.getJSONObject(index);
		OSMJSONObjectMember osmjsonMember = new OSMJSONObjectMember(member);
		
		return osmjsonMember;
	}
}
