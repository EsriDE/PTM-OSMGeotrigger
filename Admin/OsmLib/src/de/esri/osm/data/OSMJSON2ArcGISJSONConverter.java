package de.esri.osm.data;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.esri.osm.config.Field;
import de.esri.osm.config.Matching;
import de.esri.osm.config.Query;

/**
 * Converts an OSM feature (JSON) to an ArcGIS feature (JSON).
 * 
 * @author Eva Peters
 *
 */
public class OSMJSON2ArcGISJSONConverter 
{	
	private static JSONObject osmRawObject;
	
	private static OSMJSONObject osmjsonObject;
	
	private static ArcGISJSONObject arcGISJSONObject;
	
	private static String osmType;
	
	private static String arcGISType;
	
	private static Object osmid; 

	private static Query configurationQuery;
	
	/**
	 * Converts an OSM feature to an ArcGIS feature.
	 * 
	 * @param osmElement The OSM feature.
	 * @param query The configuration query.
	 * @return The ArcGIS feature.
	 * @throws GeometryTypeSupportException If the combination of geometry types (OSM and ArcGIS) are not supported.
	 */
	
	/**
	 * Converts an OSM feature to an ArcGIS feature.
	 * 
	 * @param osmObject The OSM feature as JSON.
	 * @param query The configuration query.
	 * @return The ArcGIS feature.
	 * @throws GeometryTypeSupportException If the combination of geometry types (OSM and ArcGIS) are not supported.
	 * @throws GeometryGenerationException If the geometry geometry can not be generated.
	 * @throws GeometryReaderException If the geometry can not be read.
	 */
	public static ArcGISJSONObject convert(JSONObject osmObject, Query query) throws GeometryTypeSupportException, GeometryGenerationException, GeometryReaderException
	{
		osmRawObject = osmObject;
		osmType = (String) OSMJSONObject.getType(osmObject);
		arcGISType = query.getArcgis().getType();
		configurationQuery = query;
				
		createGeometry();
		
		osmid = osmjsonObject.getId();
		
		addAttributeFieldOsmurl();
		addAttributeFieldOsmid();
		addAttributeFieldData();
		addAttributeFieldsTags();
		
		return arcGISJSONObject;
	}
	
	/**
	 * Create the geometry.
	 * 
	 * @throws GeometryTypeSupportException If the geometry types of OSM and ArcGIS are not supported.
	 * @throws GeometryGenerationException If the geometry geometry can not be generated.
	 * @throws GeometryReaderException If the geometry can not be read.
	 */
	private static void createGeometry() throws GeometryTypeSupportException, GeometryGenerationException, GeometryReaderException
	{
		if(osmType.equals("node") && arcGISType.equals("point"))
		{
			createGeometryFromNode();
		}
		else if (osmType.equals("way"))
		{
			createGeometryFromWay();	
		}
		else if (osmType.equals("relation"))
		{
			createGeometryFromRelation();
		}
		else
		{
			throw new GeometryTypeSupportException("The combination of the OSM geometry type '" + osmType + "' and the ArcGIS geometry type '" + arcGISType + "' is not supported.");
		}
	}
	
	/**
	 * Creates the geometry form the OSM type 'node'.
	 * 
	 * @throws GeometryGenerationException If the geometry geometry can not be generated.
	 * @throws GeometryReaderException If the geometry can not be read.
	 */
	private static void createGeometryFromNode() throws GeometryGenerationException, GeometryReaderException
	{
		osmjsonObject = new OSMJSONObjectNode(osmRawObject);
		
		createPoint();
	}
	
	/**
	 * Creates the geometry from the OSM type 'way'.
	 * 
	 * @throws GeometryTypeSupportException If the geometry types of OSM and ArcGIS are not supported.
	 * @throws GeometryGenerationException If the geometry geometry can not be generated.
	 * @throws GeometryReaderException If the geometry can not be read.
	 */
	private static void createGeometryFromWay() throws GeometryTypeSupportException, GeometryGenerationException, GeometryReaderException
	{
		osmjsonObject = new OSMJSONObjectWay(osmRawObject);
		
		if(arcGISType.equals("polyline"))
		{
			createPolylineFromWay();
		}
		else if(arcGISType.equals("polygon"))
		{
			createPolygonFromWay();
		}
		else
		{
			throw new GeometryTypeSupportException("The combination of the OSM geometry type '" + osmType + "' and the ArcGIS geometry type '" + arcGISType + "' is not supported.");
		}
	}
	
	/**
	 * Creates the geometry form the OSM type 'relation'.
	 * 
	 * @throws GeometryTypeSupportException If the geometry types of OSM and ArcGIS are not supported.
	 * @throws GeometryGenerationException If the geometry geometry can not be generated.
	 * @throws GeometryReaderException If the geometry can not be read.
	 */
	private static void createGeometryFromRelation() throws GeometryTypeSupportException, GeometryGenerationException, GeometryReaderException
	{
		osmjsonObject = new OSMJSONObjectRelation(osmRawObject);
		if(arcGISType.equals("polyline"))
		{
			createPolylineFromRelation();
		}
		else if(arcGISType.equals("polygon"))
		{
			createPolygonFromRelation();
		}
		else
		{
			throw new GeometryTypeSupportException("The combination of the OSM geometry type '" + osmType + "' and the ArcGIS geometry type '" + arcGISType + "' is not supported.");
		}
		
	}
		
	/**
	 * Create a point from a node.
	 * @throws GeometryGenerationException If the geometry geometry can not be generated.
	 * @throws GeometryReaderException If the geometry can not be read.
	 */
	private static void createPoint() throws GeometryGenerationException, GeometryReaderException
	{
		OSMJSONObjectNode osmjsonObjectPoint = (OSMJSONObjectNode) osmjsonObject;
		double x = osmjsonObjectPoint.getPointGeometry().getLongitude();
		double y = osmjsonObjectPoint.getPointGeometry().getLatitude();
		arcGISJSONObject = new ArcGISJSONObjectPoint(x, y);
	}
	
	/**
	 * Creates a polyline from the OSM type 'way'.
	 * 
	 * @throws GeometryGenerationException If the geometry geometry can not be generated.
	 * @throws GeometryReaderException If the geometry can not be read.
	 */
	private static void createPolylineFromWay() throws GeometryGenerationException, GeometryReaderException
	{
		OSMJSONObjectWay osmjsonObjectWay = (OSMJSONObjectWay) osmjsonObject;
		ArcGISJSONObjectPolyline arcGISJSONObjectPolyline= new ArcGISJSONObjectPolyline();
		
		int length = osmjsonObjectWay.getGeometryLength();
		for(int i = 0; i < length; i++)
		{
			OSMJSONPoint osmjsonPoint = osmjsonObjectWay.getVertex(i);
			double x = osmjsonPoint.getLongitude();
			double y = osmjsonPoint.getLatitude();
			arcGISJSONObjectPolyline.addVertex(x, y);
		}
		
		arcGISJSONObject = arcGISJSONObjectPolyline; 
	}
	
	/**
	 * Creates a polygon from the OSM type 'relation'.
	 * 
	 * @throws GeometryGenerationException If the geometry geometry can not be generated.
	 * @throws GeometryReaderException If the geometry can not be read.
	 */
	private static void createPolylineFromRelation() throws GeometryGenerationException, GeometryReaderException
	{
		OSMJSONObjectRelation osmjsonObjectRelation = (OSMJSONObjectRelation) osmjsonObject;
		ArcGISJSONObjectPolyline arcGISJSONObjectPolyline= new ArcGISJSONObjectPolyline();
		
		int length = osmjsonObjectRelation.getMemberLength();
		for(int i = 0; i < (length - 1); i++)
		{
			OSMJSONObjectMember objectMemberA = osmjsonObjectRelation.getMember(i);
			OSMJSONObjectMember objectMemberB = osmjsonObjectRelation.getMember(i + 1);
			
			if(i == 0)
			{
				boolean reverse = checkIfMemberAShouldBeReversed(objectMemberA, objectMemberB);				
				boolean forward = (! reverse);
				addMemberToPolyline(objectMemberA, arcGISJSONObjectPolyline, false, forward);
				
			}
			
			JSONArray jsonArrayLastVertex = arcGISJSONObjectPolyline.getLastVertex();
			
			boolean reverse = checkIfMemberBShouldBeReversed(jsonArrayLastVertex, objectMemberB);
			boolean forward = (! reverse);
			addMemberToPolyline(objectMemberB, arcGISJSONObjectPolyline, true, forward);
		}
				
		arcGISJSONObject = arcGISJSONObjectPolyline; 
	}
	
	/**
	 * Creates a polygon from a way.
	 * @throws GeometryGenerationException If the geometry geometry can not be generated.
	 * @throws GeometryReaderException If the geometry can not be read.
	 */
	private static void createPolygonFromWay() throws GeometryGenerationException, GeometryReaderException
	{
		OSMJSONObjectWay osmjsonObjectWay = (OSMJSONObjectWay) osmjsonObject;
		ArcGISJSONObjectPolygon arcGISJSONObjectPolygon= new ArcGISJSONObjectPolygon();
		 
		int length = osmjsonObjectWay.getGeometryLength();
		for(int i = 0; i < length; i++)
		{
			OSMJSONPoint osmjsonPoint = osmjsonObjectWay.getVertex(i);
			double x = osmjsonPoint.getLongitude();
			double y = osmjsonPoint.getLatitude();
			arcGISJSONObjectPolygon.addVertex(x, y);
		}
		
		arcGISJSONObjectPolygon.addingVerticesFinished();
		
		arcGISJSONObject = arcGISJSONObjectPolygon;
	}
	
	/**
	 * Creates a polygon from a relation.
	 * 
	 * Only simple relations without holes (donuts) are allowed.
	 * @throws GeometryGenerationException If the geometry geometry can not be generated.
	 * @throws GeometryReaderException If the geometry can not be read.
	 */
	private static void createPolygonFromRelation() throws GeometryGenerationException, GeometryReaderException
	{
		OSMJSONObjectRelation osmjsonObjectRelation = (OSMJSONObjectRelation) osmjsonObject;
		ArcGISJSONObjectPolygon arcGISJSONObjectPolygon= new ArcGISJSONObjectPolygon();
		
		int length = osmjsonObjectRelation.getMemberLength();
		for(int i = 0; i < (length - 1); i++)
		{
			OSMJSONObjectMember objectMemberA = osmjsonObjectRelation.getMember(i);
			OSMJSONObjectMember objectMemberB = osmjsonObjectRelation.getMember(i + 1);
			
			if(i == 0)
			{
				boolean reverse = checkIfMemberAShouldBeReversed(objectMemberA, objectMemberB);				
				boolean forward = (! reverse);
				addMemberToPolygon(objectMemberA, arcGISJSONObjectPolygon, false, forward);
				
			}
			
			JSONArray jsonArrayLastVertex = arcGISJSONObjectPolygon.getLastVertex();
			
			boolean reverse = checkIfMemberBShouldBeReversed(jsonArrayLastVertex, objectMemberB);
			boolean forward = (! reverse);
			addMemberToPolygon(objectMemberB, arcGISJSONObjectPolygon, true, forward);
		}
		
		arcGISJSONObjectPolygon.addingVerticesFinished();
				
		arcGISJSONObject = arcGISJSONObjectPolygon; 
	}

	/**
	 * Adds the vertices of the given member to the polyline by reference.
	 * 
	 * @param objectMember The member.
	 * @param arcGISJSONObjectPolyline The polyline.
	 * @param skipFirst True if the first vertex should be skipped (after reversing). Otherwise false.
	 * @param forward True if the vertices should be read forward. Otherwise false.
	 * @throws GeometryGenerationException If the geometry geometry can not be generated.
	 * @throws GeometryReaderException If the geometry can not be read.
	 */
	private static void addMemberToPolyline(OSMJSONObjectMember objectMember, ArcGISJSONObjectPolyline arcGISJSONObjectPolyline, boolean skipFirst, boolean forward) throws GeometryGenerationException, GeometryReaderException
	{
		int length = objectMember.getGeometryLength();
				
		if (forward)
		{
			int start = 0;
			if (skipFirst)
			{
				start++;
			}
			
			for(int j = start; j < length; j++)
			{
				OSMJSONPoint osmjsonPoint = objectMember.getVertex(j);
				double x = osmjsonPoint.getLongitude();
				double y = osmjsonPoint.getLatitude();
				arcGISJSONObjectPolyline.addVertex(x, y);
			}
		}
		else
		{
			int start = length - 1;
			if (skipFirst)
			{
				start--;
			}
			
			for(int j = start; j >= 0; j--)
			{
				OSMJSONPoint osmjsonPoint = objectMember.getVertex(j);
				double x = osmjsonPoint.getLongitude();
				double y = osmjsonPoint.getLatitude();
				arcGISJSONObjectPolyline.addVertex(x, y);
			}
		}
	}
	
	/**
	 * Adds the vertices of the given member to the polygon by reference.
	 * 
	 * @param objectMember The member.
	 * @param arcGISJSONObjectPolygon The polygon.
	 * @param skipFirst True if the first vertex should be skipped (after reversing). Otherwise false.
	 * @param forward True if the vertices should be read forward. Otherwise false.
	 * @throws GeometryGenerationException If the geometry geometry can not be generated.
	 * @throws GeometryReaderException If the geometry can not be read.
	 */
	private static void addMemberToPolygon(OSMJSONObjectMember objectMember, ArcGISJSONObjectPolygon arcGISJSONObjectPolygon, boolean skipFirst, boolean forward) throws GeometryGenerationException, GeometryReaderException
	{
		int length = objectMember.getGeometryLength();
		
		if (forward)
		{
			int start = 0;
			if (skipFirst)
			{
				start++;
			}
			
			for(int j = start; j < length; j++)
			{
				OSMJSONPoint osmjsonPoint = objectMember.getVertex(j);
				double x = osmjsonPoint.getLongitude();
				double y = osmjsonPoint.getLatitude();
				arcGISJSONObjectPolygon.addVertex(x, y);
			}
		}
		else
		{
			int start = length - 1;
			if (skipFirst)
			{
				start--;
			}
			
			for(int j = start; j >= 0; j--)
			{
				OSMJSONPoint osmjsonPoint = objectMember.getVertex(j);
				double x = osmjsonPoint.getLongitude();
				double y = osmjsonPoint.getLatitude();
				arcGISJSONObjectPolygon.addVertex(x, y);
			}
		}
	}
	
	/**
	 * Check if the vertex order of the member A  should be reversed.
	 * 
	 * Returns true
	 * if the first vertex of A matches the first vertex of B
	 * or if the first vertex of A matches the last vertex of B
	 * 
	 * @param objectMemberA The member A.
	 * @param objectMemberB The member B.
	 * @throws GeometryReaderException If the geometry can not be read.
	 */
	private static boolean checkIfMemberAShouldBeReversed(OSMJSONObjectMember objectMemberA, OSMJSONObjectMember objectMemberB) throws GeometryReaderException
	{
		OSMJSONPoint vertexAFirst = objectMemberA.getVertex(0);
		
		OSMJSONPoint vertexBFirst = objectMemberB.getVertex(0);
		OSMJSONPoint vertexBLast = objectMemberB.getLastVertex();
		
		if(isMatching(vertexAFirst, vertexBFirst) || isMatching(vertexAFirst, vertexBLast))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Check if the vertex order of the member B should be reversed.
	 * 
	 * Return true
	 * if the given vertex matches the last vertex of B.
	 * 
	 * @param jsonArrayVertex The vertex.
	 * @param objectMemberB The member B.
	 * @throws GeometryReaderException If the geometry can not be read.
	 */
	private static boolean checkIfMemberBShouldBeReversed(JSONArray jsonArrayVertex, OSMJSONObjectMember objectMemberB) throws GeometryReaderException
	{
		OSMJSONPoint vertexBLast = objectMemberB.getLastVertex();
		
		if(isMatching(jsonArrayVertex, vertexBLast))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if both vertices match.
	 * 
	 * @param jsonArrayVertex The first vertex.
	 * @param vertexB The second vertex.
	 * @return True if both vertices match.
	 * @throws GeometryReaderException If the geometry can not be read.
	 */
	private static boolean isMatching(JSONArray jsonArrayVertex, OSMJSONPoint vertexB) throws GeometryReaderException
	{
		double latitudeA = ArcGISJSONObject.getYLatitude(jsonArrayVertex);
		double longitudeA = ArcGISJSONObject.getXLongitude(jsonArrayVertex);
		double latitudeB = vertexB.getLatitude();
		double longitudeB = vertexB.getLongitude();
		
		return isMatching(latitudeA, longitudeA, latitudeB, longitudeB);
	}
	
	/**
	 * Checks if both vertices match.
	 * 
	 * @param vertexA The first vertex.
	 * @param vertexB The second vertex.
	 * @return True if both vertices match.
	 * @throws GeometryReaderException If the geometry can not be read.
	 */
	private static boolean isMatching(OSMJSONPoint vertexA, OSMJSONPoint vertexB) throws GeometryReaderException
	{
		double latitudeA = vertexA.getLatitude();
		double longitudeA = vertexA.getLongitude();
		double latitudeB = vertexB.getLatitude();
		double longitudeB = vertexB.getLongitude();
		
		return isMatching(latitudeA, longitudeA, latitudeB, longitudeB);
	}
	
	/**
	 * Checks of latitude AND longitude values match.
	 * 
	 * @param latitudeA The latitude A.
	 * @param longitudeA The longitude A.
	 * @param latitudeB The latitude B.
	 * @param longitudeB The longitude B.
	 * @return
	 */
	private static boolean isMatching(double latitudeA, double longitudeA, double latitudeB, double longitudeB)
	{
		if(latitudeA == latitudeB
			&& longitudeA == longitudeB)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Adds the data attribute field value 'osmurl'.
	 * 
	 * @throws GeometryGenerationException If the geometry can not be generated.
	 */
	private static void addAttributeFieldOsmurl() throws GeometryGenerationException
	{
		String osmurlField = configurationQuery.getFields().getOsmurl();
		arcGISJSONObject.addAttributeField(osmurlField, "http://www.openstreetmap.org/" + osmType + "/" + osmid);
	}
	
	/**
	 * Adds the data attribute field value 'OSMID'.
	 * 
	 * @throws GeometryGenerationException If the geometry can not be generated.
	 */
	private static void addAttributeFieldOsmid() throws GeometryGenerationException
	{
		String osmidField = configurationQuery.getFields().getOsmid();
		arcGISJSONObject.addAttributeField(osmidField, osmid);
	}
	
	/**
	 * Adds the data attribute field.
	 * 
	 * @throws GeometryGenerationException If the geometry can not be generated.
	 * @throws GeometryReaderException If the geometry can not be read.
	 */
	private static void addAttributeFieldData() throws GeometryGenerationException, GeometryReaderException
	{
		JSONObject data = new JSONObject();
		
		//Feature Class name
		String featureClassUrl = configurationQuery.getArcgis().getFeatureClass();
		String featureClassName = getFeatureLayerName(featureClassUrl);
		try {
			data.put("layer", featureClassName);
			
			//OSM ID
			data.put("osmid", osmid);
			
			//Tags
			JSONObject tags = osmjsonObject.getTags();
			data.put("tags", tags);
			
		} catch (JSONException e) {
			throw new GeometryGenerationException("Error adding attribute field data: " + e.getMessage());
		}
		
		//to String
		String dataString = data.toString();
		dataString = dataString.replaceAll("\"", "\\\"");
		
		String dataField = configurationQuery.getFields().getData();
		arcGISJSONObject.addAttributeField(dataField, dataString);
	}
	
	private static String getFeatureLayerName(String featureClassUrl)
	{
		int layerNameIndex = -1;
		
		String[] parts = featureClassUrl.split("/");
		for(int i = 0; i < parts.length; i++)
		{
			if(parts[i].equals("services"))
			{
				layerNameIndex = i + 1;
				break;
			}
		}
		
		return parts[layerNameIndex];
	}
	
	/**
	 * Adds data attribute field values for all OSM tags.
	 * 
	 * @throws GeometryGenerationException If the geometry can not be generated.
	 * @throws GeometryReaderException If the geometry can not be read.
	 */
	private static void addAttributeFieldsTags() throws GeometryGenerationException, GeometryReaderException
	{
		//Fill the specified ArcGIS fields with OSM tags
		Matching fieldObject = configurationQuery.getFields().getMatching();
		List<Field> fields = fieldObject.getField();
		for(Field field : fields)
		{
			String osmTagName = field.getOsm();
			String osmTagValue = null;
			try
			{
				osmTagValue = osmjsonObject.getTag(osmTagName);
			}
			catch(JSONException ex)
			{
				continue;
			}
			
			String arcGISFieldName = field.getArcgis();
			
			arcGISJSONObject.addAttributeField(arcGISFieldName, osmTagValue);
		}
	}
}
