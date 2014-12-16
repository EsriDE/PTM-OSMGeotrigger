package de.esri.geotrigger.core;

import java.util.Map;

import org.json.JSONObject;

import com.esri.core.map.Feature;

public class Util {
	

	/**
	 * Parse the attributes of a feature. The attribute name(s) are substituted by the attribute values.
	 * @param text The text with the attribute(s) to substitute.
	 * @param feature The feature.
	 * @return The parsed text.
	 */
	public static String parseAttributes(String text, Feature feature){
		String temp = text;
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
				Object attributeValue = attributes.get(attributeName);
				if(attributeValue != null){
					valueStr = attributeValue.toString();
				}
				temp = temp.substring(0, start) + valueStr + temp.substring(end + 2);
			}
		}
		return temp;
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
