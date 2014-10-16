package de.esri.geotrigger.core;

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
	
	public void deleteTrigger(String[] triggerIds, String[] tags){
		log.debug("Deleting trigger");
		JSONObject params = new JSONObject();
		JSONArray ids = new JSONArray();
		if(triggerIds != null && triggerIds.length > 0){
			// delete by trigger ids
			for(String triggerId : triggerIds){
				ids.put(triggerId);			
			}
		}else if(tags != null && tags.length > 0){
			// delete by tags
			JSONObject tagsObject = new JSONObject();
			JSONArray tagsArray = new JSONArray();
			for(String tag : tags){
				tagsArray.put(tag);			
			}
			tagsObject.put("tags", tagsArray);
			ids.put(tagsObject);
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
}
