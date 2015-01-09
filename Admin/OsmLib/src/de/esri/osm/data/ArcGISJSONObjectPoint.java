package de.esri.osm.data;

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
	/**
	 * Constructor.
	 * 
	 * @param x The longitude.
	 * @param y The latitude.
	 */
	public ArcGISJSONObjectPoint(double x, double y)
	{
		super();

		this.geometry.put("x", x);
		this.geometry.put("y", y);
	}
}
