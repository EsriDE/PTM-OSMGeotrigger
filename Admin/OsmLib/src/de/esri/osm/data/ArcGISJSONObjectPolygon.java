package de.esri.osm.data;

import org.json.JSONArray;
import org.json.JSONException;

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
         "data":"{\"osmid\":7920367,\"layer\":\"FIXME_Polygons_Test\",\"tags\":{\"area\":\"yes\",\"natural\":\"water\",\"name\":\"Mittelkanal\",\"source\":\"onsite legend\"}}",
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
 * @author Eva Peters
 *
 */
public class ArcGISJSONObjectPolygon extends ArcGISJSONObject 
{
	private JSONArray jsonArrayGeometryContainer;
	
	/**
	 * Constructor.
	 * 
	 * @throws GeometryGenerationException If the geometry geometry can not be generated.
	 */
	public ArcGISJSONObjectPolygon() throws GeometryGenerationException
	{
		super();
		
		JSONArray jsonArray = new JSONArray();
		this.jsonArrayGeometryContainer = new JSONArray();
		jsonArray.put(jsonArrayGeometryContainer);

		try {
			this.geometry.put("rings", jsonArray);
		} catch (JSONException e) {
			throw new GeometryGenerationException("Error setting ring: " + e.getMessage());
		}
	}
	
	/**
	 * Adds a vertex.
	 * 
	 * @param x The longitude.
	 * @param y The latitude.
	 * @throws GeometryGenerationException If the geometry geometry can not be generated.
	 */
	public void addVertex(double x, double y) throws GeometryGenerationException
	{
		JSONArray jsonArrayPointContainer = new JSONArray();
		try {
			jsonArrayPointContainer.put(x);
			jsonArrayPointContainer.put(y);
		} catch (JSONException e) {
			throw new GeometryGenerationException("Error adding vertex: " + e.getMessage());
		}
		
		this.jsonArrayGeometryContainer.put(jsonArrayPointContainer);
	}
	
	/**
	 * Gets the last vertex.
	 * 
	 * @return The last vertex.
	 */
	public JSONArray getLastVertex()
	{
		int length = this.jsonArrayGeometryContainer.length();
		return this.jsonArrayGeometryContainer.getJSONArray(length - 1);
	}

	/**
	 * Calls this method when last vertex was added.
	 * 
	 * Then the very last vertex is added with the coordinates of the first vertex.
	 * @throws GeometryGenerationException If the geometry geometry can not be generated.
	 */
	public void addingVerticesFinished() throws GeometryGenerationException
	{
		Object firstVertex = null;
		try {
			firstVertex = jsonArrayGeometryContainer.get(0);
		} catch (JSONException e) {
			throw new GeometryGenerationException("Error getting first vertex: " + e.getMessage());
		}
		this.jsonArrayGeometryContainer.put(firstVertex);
	}
}
