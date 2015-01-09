package de.esri.osm.data;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import de.esri.osm.GeometryTypeSupportException;
import de.esri.osm.config.Field;
import de.esri.osm.config.Fields;
import de.esri.osm.config.Query;

/**
 * Converts an OSM feature (JSON) to an ArcGIS feature (JSON).
 * 
 * @author evp
 *
 */
public class OSMJSON2ArcGISJSONConverter 
{
	private static JSONObject osmRawObject;
	
	private static OSMJSONObject osmjsonObject;
	
	private static ArcGISJSONObject arcGISJSONObject;
	
	private static String osmType;
	
	private static String arcGISType;
	
	private static Object osmid; //2

	private static Query configurationQuery; //2
	
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
	 */
	public static ArcGISJSONObject convert(JSONObject osmObject, Query query) throws GeometryTypeSupportException
	{
		osmRawObject = osmObject;
		osmType = (String) OSMJSONObject.getType(osmObject);
		arcGISType = query.getArcgis().getType();
		configurationQuery = query;
				
		createGeometry();
		
		osmid = osmjsonObject.getId();
		
		addAttributeFieldOsmorgurl();
		addAttributeFieldOsmid();
		addAttributeFieldData();
		addAttributeFieldsTags();
		
		return arcGISJSONObject;
	}
	
	private static void createGeometry() throws GeometryTypeSupportException
	{
		if(osmType.equals("node") && arcGISType.equals("point"))
		{
			createGeometryFromNode();
		}
		else if (osmType.equals("way"))
		{
			createGeometryFromWay();	
		}
		else
		{
			throw new GeometryTypeSupportException("The combination of the OSM geometry type '" + osmType + "' and the ArcGIS geometry type '" + arcGISType + "' is not supported.");
		}
	}
	
	private static void createGeometryFromNode()
	{
		osmjsonObject = new OSMJSONObjectNode(osmRawObject);
		
		createPoint();
	}
	
	private static void createGeometryFromWay() throws GeometryTypeSupportException
	{
		osmjsonObject = new OSMJSONObjectWay(osmRawObject);
		
		if(arcGISType.equals("polyline"))
		{
			createPolyline();
		}
		else if(arcGISType.equals("polygon"))
		{
			createPolygon();
		}
		else
		{
			throw new GeometryTypeSupportException("The combination of the OSM geometry type '" + osmType + "' and the ArcGIS geometry type '" + arcGISType + "' is not supported.");
		}
	}
		
	private static void createPoint()
	{
		OSMJSONObjectNode osmjsonObjectPoint = (OSMJSONObjectNode) osmjsonObject;
		double x = osmjsonObjectPoint.getPointGeometry().getLongitude();
		double y = osmjsonObjectPoint.getPointGeometry().getLatitude();
		arcGISJSONObject = new ArcGISJSONObjectPoint(x, y);
	}
	
	private static void createPolyline()
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
	
	private static void createPolygon()
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
	
	private static void addAttributeFieldOsmorgurl()
	{
		arcGISJSONObject.addAttributeField("osmorgurl", "http://www.openstreetmap.org/" + osmType + "/" + osmid);
	}
	
	private static void addAttributeFieldOsmid()
	{
		arcGISJSONObject.addAttributeField("OSMID", osmid);
	}
	
	private static void addAttributeFieldData()
	{
		JSONObject data = new JSONObject();
		
		//Feature Class name
		String featureClassUrl = configurationQuery.getArcgis().getFeatureClass();
		String featureClassName = getFeatureLayerName(featureClassUrl);
		data.put("layer", featureClassName);
		
		//OSM ID
		data.put("osmid", osmid);
		
		//Tags
		JSONObject tags = osmjsonObject.getTags();
		data.put("tags", tags);
		
		//to String
		String dataString = data.toString();
		dataString = dataString.replaceAll("\"", "\\\"");
		
		arcGISJSONObject.addAttributeField("KeyValueAll", dataString);
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
	
	private static void addAttributeFieldsTags()
	{
		//Fill the specified ArcGIS fields with OSM tags
		Fields fieldObject = configurationQuery.getMatching().getFields();
		List<Field> fields = fieldObject.getField();
		for(Field field : fields)
		{
			String osmTagName = field.getTag();
			String osmTagValue = null;
			try
			{
				osmTagValue = osmjsonObject.getTag(osmTagName);
			}
			catch(JSONException ex)
			{
				continue;
			}
			
			String arcGISFieldName = field.getName();
			
			arcGISJSONObject.addAttributeField(arcGISFieldName, osmTagValue);
		}
	}
}
