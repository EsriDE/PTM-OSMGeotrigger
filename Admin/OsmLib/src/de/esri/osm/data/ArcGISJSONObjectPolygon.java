package de.esri.osm.data;

import org.json.JSONArray;

/**
 * Represents a JSON feature polygon for ArcGIS.
 * used for ArcGIS REST requests such as in:
 * 
 *  
 * 
 * Example:
 * 
 * {
      "attributes":{
         "KeyValueAll":"{\"osmid\":7920367,\"layer\":\"FIXME_Polygons_Test\",\"tags\":{\"area\":\"yes\",\"natural\":\"water\",\"name\":\"Mittelkanal\",\"source\":\"onsite legend\"}}",
         "OSMID":7920367,
         "natural":"water",
         "name":"Mittelkanal",
         "osmorgurl":"http://www.openstreetmap.org/way/7920367"
      },
      "geometry":{
         "rings":[
            [
               [
                  11.5652244,
                  48.2488739
               ],
               [
                  11.5681771,
                  48.2488363
               ],
               [
                  11.568333,
                  48.2489691
               ],
               [
                  11.568661,
                  48.2489668
               ],
               [
                  11.5688233,
                  48.248834
               ],
               [
                  11.5739877,
                  48.2487939
               ],
               [
                  11.5739963,
                  48.248611
               ],
               [
                  11.5688167,
                  48.2486585
               ],
               [
                  11.5686275,
                  48.2485488
               ],
               [
                  11.5683383,
                  48.248552
               ],
               [
                  11.5681753,
                  48.2486761
               ],
               [
                  11.5652215,
                  48.248698
               ],
               [
                  11.5652244,
                  48.2488739
               ],
               [
                  11.5652244,
                  48.2488739
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
public class ArcGISJSONObjectPolygon extends ArcGISJSONObject 
{
	private JSONArray jsonArrayGeometryContainer;
	
	/**
	 * Constructor.
	 */
	public ArcGISJSONObjectPolygon()
	{
		super();
		
		JSONArray jsonArray = new JSONArray();
		this.jsonArrayGeometryContainer = new JSONArray();
		jsonArray.put(jsonArrayGeometryContainer);

		this.geometry.put("rings", jsonArray);
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
		jsonArrayPointContainer.put(x);
		jsonArrayPointContainer.put(y);
		
		this.jsonArrayGeometryContainer.put(jsonArrayPointContainer);
	}

	/**
	 * Calls this method when last vertex was added.
	 * 
	 * Then the very last vertex is added with the coordinates of the first vertex.
	 */
	public void addingVerticesFinished()
	{
		Object firstVertex = jsonArrayGeometryContainer.get(0);
		this.jsonArrayGeometryContainer.put(firstVertex);
	}
}
