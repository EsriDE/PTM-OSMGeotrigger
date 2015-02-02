package de.esri.geotrigger.core;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Feature;

public class Util {
	private static Logger log = LogManager.getLogger(Util.class.getName());

	/**
	 * Parse the attributes of a feature. The attribute name(s) are substituted by the attribute values.
	 * @param text The text with the attribute(s) to substitute.
	 * @param feature The feature.
	 * @return The parsed text.
	 */
	public static String parseAttributes(String text, Feature feature){
		String temp = text;
		try{
			if(temp.contains("{{{Attributes}}}")){
				StringBuilder json = new StringBuilder();
				json.append("{");
				Map<String, Object> attributes = feature.getAttributes();
				int attrCount = 0;
				for(Map.Entry<String, Object> entry : attributes.entrySet()){
					json.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\"");
					attrCount++;
					if(attrCount < attributes.size()){
						json.append(",");
					}
				}
				json.append("}");
				temp = json.toString();
			}else{
				while(temp.contains("{{")){
					int start = temp.indexOf("{{");
					int end = temp.indexOf("}}");
					String attributeName = temp.substring(start + 2, end);
					String valueStr = "";
					Object attributeValue = feature.getAttributeValue(attributeName);
					if(attributeValue != null){
						valueStr = attributeValue.toString();
					}
					temp = temp.substring(0, start) + valueStr + temp.substring(end + 2);
				}
			}
		}catch(Exception e){
			log.error("Error parsing attributes: "+e.getMessage());
		}
		return temp;
	}
	
	/**
	 * Parse the attributes of a feature. The attribute name(s) are substituted by the attribute values.
	 * @param text The text with the attribute(s) to substitute.
	 * @param feature The feature as a JSON object.
	 * @return The parsed text.
	 */
	public static String parseAttributes(String text, JSONObject feature){
		String temp = text;
		try{
			JSONObject attributes = feature.getJSONObject("attributes");
			if(temp.contains("{{{Attributes}}}")){
				StringBuilder json = new StringBuilder();
				json.append("{");			
				String[] attributeNames = JSONObject.getNames(attributes);
				int attrCount = 0;
				for(String attributeName : attributeNames){
					Object attributeValue = attributes.get(attributeName);
					json.append("\"").append(attributeName).append("\":\"").append(attributeValue).append("\"");
					attrCount++;
					if(attrCount < attributeNames.length){
						json.append(",");
					}
				}
				json.append("}");
				temp = json.toString();
			}else{
				while(temp.contains("{{")){
					int start = temp.indexOf("{{");
					int end = temp.indexOf("}}");
					String attributeName = temp.substring(start + 2, end);
					String valueStr = "";
					Object attributeValue = attributes.opt(attributeName);
					if(attributeValue != null){
						valueStr = attributeValue.toString();
						if(valueStr.equals("null")){
							 valueStr = "";
						}
					}
					temp = temp.substring(0, start) + valueStr + temp.substring(end + 2);
				}
			}
		}catch(Exception e){
			log.error("Error parsing attributes: "+e.getMessage());
		}
		return temp;
	}
	
	/**
	 * Create a buffer for a line.
	 * @param lineJsonString A JSON string defining the line.
	 * @param distance The buffer distance.
	 * @return A JSON string with the buffer polygon.
	 */
	public static String bufferLine(String lineJsonString, double distance){
		String geoJson = null;
		try{
			Polyline polyline = new Polyline();
			JSONObject lineJson = new JSONObject(lineJsonString);
			JSONArray paths = lineJson.getJSONArray("paths");
			JSONArray line = paths.getJSONArray(0);
			for(int k = 0; k < line.length(); k++){
				JSONArray point = line.getJSONArray(k);
				double x = point.getDouble(0);
				double y = point.getDouble(1);
				if(k == 0){
					polyline.startPath(x, y);
				}else{
					polyline.lineTo(x, y);
				}
			}
			SpatialReference srsWGS84 = SpatialReference.create(SpatialReference.WKID_WGS84);
			SpatialReference srsWebMercator = SpatialReference.create(SpatialReference.WKID_WGS84_WEB_MERCATOR);
			Geometry mercatorPolyline = GeometryEngine.project(polyline, srsWGS84, srsWebMercator);
			Polygon mercatorPolygon = GeometryEngine.buffer(mercatorPolyline, srsWebMercator, distance, srsWebMercator.getUnit());
			Polygon polygon = (Polygon) GeometryEngine.project(mercatorPolygon, srsWebMercator, srsWGS84);
			geoJson = GeometryEngine.geometryToJson(srsWGS84, polygon);			
		}catch(Exception e){
			log.error("Error creating line buffer: "+e.getMessage());
		}
		return geoJson;
	}
	
	/**
	 * Create a buffer for a polygon.
	 * @param polygonJsonString A JSON string defining the polygon.
	 * @param distance The buffer distance.
	 * @return A JSON string with the buffer polygon.
	 */
	public static String bufferPolygon(String polygonJsonString, double distance){
		String geoJson = null;
		try{
			Polygon polygon = new Polygon();
			JSONObject polygonJson = new JSONObject(polygonJsonString);
			JSONArray paths = polygonJson.getJSONArray("rings");
			JSONArray path = paths.getJSONArray(0);
			for(int k = 0; k < path.length(); k++){
				JSONArray point = path.getJSONArray(k);
				double x = point.getDouble(0);
				double y = point.getDouble(1);
				if(k == 0){
					polygon.startPath(x, y);
				}else{
					polygon.lineTo(x, y);
				}
			}
			SpatialReference srsWGS84 = SpatialReference.create(SpatialReference.WKID_WGS84);
			SpatialReference srsWebMercator = SpatialReference.create(SpatialReference.WKID_WGS84_WEB_MERCATOR);
			Geometry mercatorPolygon = GeometryEngine.project(polygon, srsWGS84, srsWebMercator);
			Polygon bufferPolygon = GeometryEngine.buffer(mercatorPolygon, srsWebMercator, distance, srsWebMercator.getUnit());
			Polygon wgsPolygon = (Polygon) GeometryEngine.project(bufferPolygon, srsWebMercator, srsWGS84);
			geoJson = GeometryEngine.geometryToJson(srsWGS84, wgsPolygon);			
		}catch(Exception e){
			log.error("Error creating polygon buffer: "+e.getMessage());
		}
		return geoJson;
	}
	
	/**
	 * Checks if a string is null or an empty string.
	 * @param text The string.
	 * @return A boolean value indicating whether a string is null or an empty string.
	 */
	public static boolean isEmpty(String text){
		boolean isEmpty = false;
		if(text == null || text.equals("")){
			isEmpty = true;
		}		
		return isEmpty;
	}
}
