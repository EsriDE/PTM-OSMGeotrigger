package de.esri.geotrigger.core;

import java.net.URLEncoder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TriggerHandler {
	private static Logger log = LogManager.getLogger(TriggerHandler.class.getName());
	
	/**
	 * Create a trigger in the ArcGIS geotrigger service. The trigger will be a circle.
	 * @param triggerId The trigger id.
	 * @param tags The tags for the trigger.
	 * @param direction The trigger direction (enter / leave).
	 * @param latitude The latitude value. (WGS84)
	 * @param longitude The longitude value. (WGS84)
	 * @param radius The trigger radius.
	 * @param notificationText The notification text of the trigger.
	 * @param notificationUrl The notification URL.
	 * @param notificationIcon The notification icon.
	 * @param notificationSound The notification sound.
	 * @param notificationData The notification Data.
	 * @param callBackUrl The callback URL.
	 * @param properties The trigger properties.
	 * @param trackingProfile The tracking profile.
	 * @param times The times value.
	 * @param rateLimit The rate limit.
	 * @param boundingBoxReturnFormat The format of the bounding boc return value.
	 * @param geoReturnFormat The format of the geo return value.
	 * @param fromTimestamp The from timestamp value.
	 * @param toTimestamp The to timestamp value.
	 */
	public void createTrigger(String triggerId, String[] tags, String direction, double latitude, double longitude, double radius, String notificationText, 
			String notificationUrl, String notificationIcon, String notificationSound, String notificationData, String callBackUrl, String properties,
			String trackingProfile, int times, int rateLimit, String boundingBoxReturnFormat, String geoReturnFormat, long fromTimestamp, long toTimestamp){
		TriggerBuilder builder = new TriggerBuilder();		
		builder.setTriggerId(triggerId);
		builder.setTags(tags);
		builder.setDirection(direction);
		builder.setGeo(latitude, longitude, radius);		
		if(!Util.isEmpty(notificationText)){
			builder.setNotificationText(notificationText);			
		}
		if(!Util.isEmpty(notificationUrl)){
			builder.setNotificationUrl(notificationUrl);
		}
		if(!Util.isEmpty(notificationIcon)){
			builder.setNotificationIcon(notificationIcon);
		}
		if(!Util.isEmpty(notificationSound)){
			builder.setNotificationSound(notificationSound);
		}
		if(!Util.isEmpty(notificationData)){
			JSONObject notificationDataJson = null;
			try{
				notificationDataJson = new JSONObject(notificationData);
				builder.setNotificationData(notificationDataJson);
			}catch(Exception e){
				log.error("Error parsing JSON for notification data: " + e.getMessage());
			}
		}
		if(!Util.isEmpty(callBackUrl)){
			builder.setCallbackUrl(callBackUrl);
		}
		if(!Util.isEmpty(properties)){
			JSONObject propertiesJson = null;
			try{
				propertiesJson = new JSONObject(properties);
				builder.setProperties(propertiesJson);
			}catch(Exception e){
				log.error("Error parsing JSON for properties: " + e.getMessage());
			}
		}
		if(!Util.isEmpty(trackingProfile)){
			builder.setTrackingProfile(trackingProfile);
		}
		if(times >= 0){
			builder.setTimes(times);
		}
		if(rateLimit >= 0){
			builder.setRateLimit(rateLimit);
		}
		if(!Util.isEmpty(boundingBoxReturnFormat)){
			builder.setBoundingBoxReturnFormat(boundingBoxReturnFormat);
		}
		if(!Util.isEmpty(geoReturnFormat)){
			builder.setGeoReturnFormat(geoReturnFormat);
		}
		if(fromTimestamp > 0){
			builder.setFromTimestamp(fromTimestamp);
		}
		if(toTimestamp > 0){
			builder.setToTimestamp(toTimestamp);
		}
		JSONObject params = builder.build();		
		
        // Send the request to the Geotrigger API.
        GeotriggerApiClient.runRequest("trigger/create", params, new GeotriggerApiListener() {
            @Override
            public void onSuccess(JSONObject data) {
            	log.debug("Trigger created.");
            }

            @Override
            public void onFailure(Throwable e) {
            	log.error("Error creating trigger: "+e.getMessage());
            }
        });
	}
	
	/**
	 * Create a trigger in the ArcGIS geotrigger service. The trigger will be a polygon.
	 * @param triggerId The trigger id.
	 * @param tags The tags for the trigger.
	 * @param direction The trigger direction (enter / leave).
	 * @param geoJson The JSON of the polygon.
	 * @param notificationText The notification text of the trigger.
	 * @param notificationUrl The notification URL.
	 * @param notificationIcon The notification icon.
	 * @param notificationSound The notification sound.
	 * @param notificationData The notification Data.
	 * @param callBackUrl The callback URL.
	 * @param properties The trigger properties.
	 * @param trackingProfile The tracking profile.
	 * @param times The times value.
	 * @param rateLimit The rate limit.
	 * @param boundingBoxReturnFormat The format of the bounding boc return value.
	 * @param geoReturnFormat The format of the geo return value.
	 * @param fromTimestamp The from timestamp value.
	 * @param toTimestamp The to timestamp value.
	 */
	public void createTrigger(String triggerId, String[] tags, String direction, String geoJson, String notificationText, 
			String notificationUrl, String notificationIcon, String notificationSound, String notificationData, String callBackUrl, String properties,
			String trackingProfile, int times, int rateLimit, String boundingBoxReturnFormat, String geoReturnFormat, long fromTimestamp, long toTimestamp){
		TriggerBuilder builder = new TriggerBuilder();		
		builder.setTriggerId(triggerId);
		builder.setTags(tags);
		builder.setDirection(direction);
		JSONObject geoJsonObject = new JSONObject(geoJson);
		builder.setGeoFromEsriJSON(geoJsonObject);
		if(!Util.isEmpty(notificationText)){
			builder.setNotificationText(notificationText);			
		}
		if(!Util.isEmpty(notificationUrl)){
			builder.setNotificationUrl(notificationUrl);
		}
		if(!Util.isEmpty(notificationIcon)){
			builder.setNotificationIcon(notificationIcon);
		}
		if(!Util.isEmpty(notificationSound)){
			builder.setNotificationSound(notificationSound);
		}
		if(!Util.isEmpty(notificationData)){
			JSONObject notificationDataJson = null;
			try{
				notificationDataJson = new JSONObject(notificationData);
				builder.setNotificationData(notificationDataJson);
			}catch(Exception e){
				log.error("Error parsing JSON for notification data: " + e.getMessage());
			}
		}
		if(!Util.isEmpty(callBackUrl)){
			builder.setCallbackUrl(callBackUrl);
		}
		if(!Util.isEmpty(properties)){
			JSONObject propertiesJson = null;
			try{
				propertiesJson = new JSONObject(properties);
				builder.setProperties(propertiesJson);
			}catch(Exception e){
				log.error("Error parsing JSON for properties: " + e.getMessage());
			}
		}
		if(!Util.isEmpty(trackingProfile)){
			builder.setTrackingProfile(trackingProfile);
		}
		if(times >= 0){
			builder.setTimes(times);
		}
		if(rateLimit >= 0){
			builder.setRateLimit(rateLimit);
		}
		if(!Util.isEmpty(boundingBoxReturnFormat)){
			builder.setBoundingBoxReturnFormat(boundingBoxReturnFormat);
		}
		if(!Util.isEmpty(geoReturnFormat)){
			builder.setGeoReturnFormat(geoReturnFormat);
		}
		if(fromTimestamp > 0){
			builder.setFromTimestamp(fromTimestamp);
		}
		if(toTimestamp > 0){
			builder.setToTimestamp(toTimestamp);
		}
		JSONObject params = builder.build();		
		
        // Send the request to the Geotrigger API.
        GeotriggerApiClient.runRequest("trigger/create", params, new GeotriggerApiListener() {
            @Override
            public void onSuccess(JSONObject data) {
            	log.debug("Trigger created.");
            }

            @Override
            public void onFailure(Throwable e) {
            	log.error("Error creating trigger: "+e.getMessage());
            }
        });
	}
	
	/**
	 * Create triggers for the features of an ArcGIS feature service. 
	 * @param serviceUrl The URL of the feature service.
	 * @param user The ArcGIS user name (if required by the service.
	 * @param password The ArcGIS password (if required by the service).
	 * @param triggerId The trigger id.
	 * @param tags The tags for the triggers.
	 * @param direction The trigger direction (enter / leave).
	 * @param radius The radius of the trigger.
	 * @param notificationText The notification text of the trigger.
	 * @param notificationUrl The notification URL.
	 * @param notificationData The notification Data.
	 * @param where A SQL where clause to limit the features/triggers.
	 */
	public void createTriggersFromService(String serviceUrl, String user, String password, String triggerId, String[] tags, 
			String direction, double radius, String notificationText, String notificationUrl, String notificationData, String where){	
		log.debug("Create triggers from service...");
		try{
			String tokenParam = "";
			if(!Util.isEmpty(user) && !Util.isEmpty(password)){
				// request user token
				String userToken = OAuthUtil.requestUserToken(user, password);
				tokenParam = "token=" + userToken + "&";
			}
			String whereParam = "where=1%3D1&";
			if(!Util.isEmpty(where)){
				whereParam = "where=" + URLEncoder.encode(where, "UTF-8") + "&";
			}
			String url = serviceUrl + "/query?" + whereParam + "outFields=*&outSR=4326&" + tokenParam + "f=json";
			log.debug("Request URL: " + url);
			String response = HttpUtil.getRequest(url);
			//log.debug("Response: " + response);
			JSONObject responseJson = new JSONObject(response);
			String geometryType = responseJson.getString("geometryType");
			if(geometryType.equals("esriGeometryPoint")){
				// features
				JSONArray features = responseJson.getJSONArray("features");
				for(int i = 0; i < features.length(); i++){
					JSONObject feature = features.getJSONObject(i);
					JSONObject geometry = feature.getJSONObject("geometry");
					double latitude = geometry.getDouble("y");
					double longitude = geometry.getDouble("x");

					// parse trigger id, notification text, notification data
					String parsedTriggerId = Util.parseAttributes(triggerId, feature);
					String parsedNotificationText = Util.parseAttributes(notificationText, feature);
					String parsedNotificationUrl = Util.parseAttributes(notificationUrl, feature);
					String parsedNotificationData = Util.parseAttributes(notificationData, feature);
					
					createTrigger(parsedTriggerId, tags, direction, latitude, longitude, radius, parsedNotificationText, parsedNotificationUrl, null, null, parsedNotificationData, null, null, null, -1, -1, null, null, -1, -1);
				}
			}else{
				log.info("Only point features are supported.");
			}
		}catch(Exception ex){
			log.error(ex.getMessage());
		}
	}
	
	/**
	 * Delete triggers by ids.
	 * @param triggerIds The trigger ids.
	 */
	public void deleteTriggersByIds(String[] triggerIds){
		log.debug("Deleting trigger");
		
		JSONObject params = new JSONObject();
		JSONArray ids = new JSONArray();
		for(String triggerId : triggerIds){
			ids.put(triggerId);			
		}
        try {
            params.put("triggerIds", ids);
        } catch (JSONException e) {
        	log.error("Error setting trigger ids: "+e.getMessage());
        }

        GeotriggerApiClient.runRequest("trigger/delete", params, new GeotriggerApiListener() {
            public void onSuccess(JSONObject data) {
            	log.debug(data.toString());
            }

            public void onFailure(Throwable error) {
            	log.error("Error creating trigger: "+error.getMessage());
            }
        });
	}
	
	/**
	 * Delete triggers by tags.
	 * @param tags The tags of the triggers..
	 */
	public void deleteTriggersByTags(String[] tags){
		log.debug("Deleting trigger");
		
		JSONObject params = new JSONObject();
		JSONArray tagsArray = new JSONArray();
		for(String tag : tags){
			tagsArray.put(tag);			
		}
        try {
            params.put("tags", tagsArray);
        } catch (JSONException e) {
        	log.error("Error setting trigger ids: "+e.getMessage());
        }

        GeotriggerApiClient.runRequest("trigger/delete", params, new GeotriggerApiListener() {
            public void onSuccess(JSONObject data) {
            	log.debug(data.toString());
            }

            public void onFailure(Throwable error) {
            	log.error("Error creating trigger: "+error.getMessage());
            }
        });
	}
}
