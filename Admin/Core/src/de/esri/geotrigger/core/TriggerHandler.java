package de.esri.geotrigger.core;

import java.net.URLEncoder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TriggerHandler {
	private static Logger log = LogManager.getLogger(TriggerHandler.class.getName());

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
	
	public void createTriggersFromService(String serviceUrl, String user, String password, String clientId, String clientSecret, String triggerId, String[] tags, 
			String direction, double radius, String notificationText, String notificationUrl, String notificationData, String where){	
		try{
			Params.get().setClientId(clientId);
			Params.get().setClientSecret(clientSecret);
			String tokenParam = "";
			if(!Util.isEmpty(user) && !Util.isEmpty(password)){
				// request user token
				String userToken = OAuthUtil.requestUserToken(user, password);
				tokenParam = "token=" + userToken + "&";
			}
			String whereParam = "";
			if(!Util.isEmpty(where)){
				whereParam = "where=" + URLEncoder.encode(where, "UTF-8") + "&";
			}
			String url = serviceUrl + "/query?" + whereParam + "outFields=*&outSR=4326&" + tokenParam + "f=json";
			String response = HttpUtil.getRequest(url);
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
	
	public void deleteTriggersByIds(String[] triggerIds, String clientId, String clientSecret){
		log.debug("Deleting trigger");
		Params.get().setClientId(clientId);
		Params.get().setClientSecret(clientSecret);
		
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
	
	public void deleteTriggersByTags(String[] tags, String clientId, String clientSecret){
		log.debug("Deleting trigger");
		Params.get().setClientId(clientId);
		Params.get().setClientSecret(clientSecret);
		
		JSONObject params = new JSONObject();
		JSONArray ids = new JSONArray();
		JSONObject tagsObject = new JSONObject();
		JSONArray tagsArray = new JSONArray();
		for(String tag : tags){
			tagsArray.put(tag);			
		}
		tagsObject.put("tags", tagsArray);
		ids.put(tagsObject);
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
}
