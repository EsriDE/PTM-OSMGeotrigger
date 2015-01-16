package de.esri.osm.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Represents a JSON feature polyline for ArcGIS.
 * used for ArcGIS REST requests such as in:
 * 
 *  
 * 
 * Example:
 * 
 * {
      "attributes":{
         "KeyValueAll":"{\"osmid\":18281905,\"layer\":\"FIXME_Polylines_Test\",\"tags\":{\"fixme\":\"check speed limit\",\"maxspeed\":\"30\",\"name\":\"Theodor-Storm-Straﬂe\",\"highway\":\"residential\"}}",
         "fixme":"check speed limit",
         "OSMID":18281905,
         "name":"Theodor-Storm-Straﬂe",
         "highway":"residential",
         "osmorgurl":"http://www.openstreetmap.org/way/18281905"
      },
      "geometry":{
         "paths":[
            [
               [
                  11.4570137,
                  48.1530328
               ],
               [
                  11.4575866,
                  48.1530179
               ]
            ]
         ],
         "spatialReference":{
            "wkid":4326
         }
      }
   }
 * 
 * @author evp
 *
 */
public class ArcGISJSONObjectPolyline extends ArcGISJSONObject 
{
	private static Logger log = LogManager.getLogger(ArcGISJSONObjectPolyline.class.getName());
	private JSONArray jsonArrayGeometryContainer;
	
	/**
	 * Constructor.
	 */
	public ArcGISJSONObjectPolyline()
	{
		super();
		
		JSONArray jsonArray = new JSONArray();
		this.jsonArrayGeometryContainer = new JSONArray();
		jsonArray.put(jsonArrayGeometryContainer);

		try {
			this.geometry.put("paths", jsonArray);
		} catch (JSONException e) {
			log.error("Error setting path: " + e.getMessage());
		}
	}
	
	/**
	 * Adds a vertex.
	 * 
	 * @param x The longitude.
	 * @param y The latitude.
	 */
	public void addVertex(double x, double y)
	{
		JSONArray jsonArrayPointContainer = new JSONArray();
		try {
			jsonArrayPointContainer.put(x);
			jsonArrayPointContainer.put(y);
		} catch (JSONException e) {
			log.error("Error adding vertex: " + e.getMessage());
		}
		
		this.jsonArrayGeometryContainer.put(jsonArrayPointContainer);
	}
}
