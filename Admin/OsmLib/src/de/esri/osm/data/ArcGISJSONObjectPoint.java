package de.esri.osm.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;

/**
 * Represents a JSON feature point for ArcGIS.
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
public class ArcGISJSONObjectPoint extends ArcGISJSONObject 
{
	private static Logger log = LogManager.getLogger(ArcGISJSONObjectPoint.class.getName());
	
	/**
	 * Constructor.
	 * 
	 * @param x The longitude.
	 * @param y The latitude.
	 */
	public ArcGISJSONObjectPoint(double x, double y)
	{
		super();

		try {
			this.geometry.put("x", x);
			this.geometry.put("y", y);
		} catch (JSONException e) {
			log.error("Could not set coordinates: " + e.getMessage());
		}
		
	}
}
