package de.esri.geotrigger.core;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.tasks.query.QueryParameters;
import com.esri.core.tasks.query.QueryTask;

public class Geotrigger {
	private static Logger log = LogManager.getLogger(Geotrigger.class.getName());
	
	public static final String TRIGGER_ID = "triggerid";
	public static final String TRIGGER_IDS = "triggerids";
	public static final String TAGS = "tags";
	public static final String DIRECTION = "direction";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	public static final String RADIUS = "radius";
	public static final String GEOJSON = "geojson";
	public static final String NOTIFICATION_TEXT = "notificationtext";
	public static final String NOTIFICATION_URL = "notificationurl";
	public static final String NOTIFICATION_ICON = "notificationicon";
	public static final String NOTIFICATION_SOUND = "notificationsound";
	public static final String NOTIFICATION_DATA = "notificationdata";
	public static final String CALLBACK_URL = "callbackurl";
	public static final String PROPERTIES = "properties";
	public static final String TRACKING_PROFILE = "trackingprofile";
	public static final String TIMES = "times";
	public static final String RATE_LIMIT = "ratelimit";
	public static final String BOUNDINGBOX_RETURN_FORMAT = "bboxreturnformat";
	public static final String GEO_RETURN_FORMAT = "times";
	public static final String FROM_TIMESTAMP = "fromtimestamp";
	public static final String TO_TIMESTAMP = "totimestamp";
	public static final String CLIENTID = "clientid";
	public static final String CLIENTSECRET = "clientsecret";

	public static void main(String[] args) {
		CommandLineArgs commandLineArgs = new CommandLineArgs(args);
		switch(commandLineArgs.getCommand()){
		case CommandLineArgs.HELP:
			// write help
			break;
		case CommandLineArgs.CREATE_TRIGGER:
			// create Trigger
			createTrigger(commandLineArgs.getParameters());
			break;
		case CommandLineArgs.RUN_TRIGGER:
			// run Trigger
			runTrigger(commandLineArgs.getParameters());
			break;
		case CommandLineArgs.DELETE_TRIGGER:
			// create Trigger
			deleteTrigger(commandLineArgs.getParameters());
			break;
		}
	}
	
	private static void createTrigger(Map<String, String> params){
		log.debug("Creating trigger...");
		String triggerId = params.containsKey(TRIGGER_ID) ? params.get(TRIGGER_ID) : null;
		String tagStr = params.containsKey(TAGS) ? params.get(TAGS) : null;
		String[] tags = tagStr.split(",");
		String direction =  params.containsKey(DIRECTION) ? params.get(DIRECTION) : null;
		String latitudeStr = params.containsKey(LATITUDE) ? params.get(LATITUDE) : null;	
		double latitude = 0.0;
		try{
			latitude = Double.parseDouble(latitudeStr);
		}catch(Exception ex){
			log.error("Error parsing latitude value: "+ex.getMessage());
		}
		String longitudeStr = params.containsKey(LONGITUDE) ? params.get(LONGITUDE) : null;
		double longitude = 0.0;
		try{
			longitude = Double.parseDouble(longitudeStr);
		}catch(Exception ex){
			log.error("Error parsing longitude value: "+ex.getMessage());
		}		
		String radiusStr =  params.containsKey(RADIUS) ? params.get(RADIUS) : null;
		double radius = 0.0;
		try{
			radius = Double.parseDouble(radiusStr);
		}catch(Exception ex){
			log.error("Error parsing radius value: "+ex.getMessage());
		}
		String geoJson = params.containsKey(GEOJSON) ? params.get(GEOJSON) : null;
		String notificationText =  params.containsKey(NOTIFICATION_TEXT) ? params.get(NOTIFICATION_TEXT) : null;
		String notificationUrl =  params.containsKey(NOTIFICATION_URL) ? params.get(NOTIFICATION_URL) : null;
		String notificationIcon =  params.containsKey(NOTIFICATION_ICON) ? params.get(NOTIFICATION_ICON) : null;
		String notificationSound =  params.containsKey(NOTIFICATION_SOUND) ? params.get(NOTIFICATION_SOUND) : null;
		String notificationData =  params.containsKey(NOTIFICATION_DATA) ? params.get(NOTIFICATION_DATA) : null;
		String callBackUrl =  params.containsKey(CALLBACK_URL) ? params.get(CALLBACK_URL) : null;
		String properties =  params.containsKey(PROPERTIES) ? params.get(PROPERTIES) : null;
		String trackingProfile =  params.containsKey(TRACKING_PROFILE) ? params.get(TRACKING_PROFILE) : null;
		String timesStr =  params.containsKey(TIMES) ? params.get(TIMES) : null;
		int times = 0;
		if(timesStr != null){
			try{
				times = Integer.parseInt(timesStr);
			}catch(Exception ex){
				log.error("Error parsing times value: "+ex.getMessage());
			}
		}
		String rateLimitStr = params.containsKey(RATE_LIMIT) ? params.get(RATE_LIMIT) : null;
		int rateLimit = 0;
		if(rateLimitStr != null){
			try{
				rateLimit = Integer.parseInt(rateLimitStr);
			}catch(Exception ex){
				log.error("Error parsing rate limit value: "+ex.getMessage());
			}			
		}
		String boundingBoxReturnFormat =  params.containsKey(BOUNDINGBOX_RETURN_FORMAT) ? params.get(BOUNDINGBOX_RETURN_FORMAT) : null;
		String geoReturnFormat =  params.containsKey(GEO_RETURN_FORMAT) ? params.get(GEO_RETURN_FORMAT) : null;
		String fromTimestampStr =  params.containsKey(FROM_TIMESTAMP) ? params.get(FROM_TIMESTAMP) : null;
		long fromTimestamp = 0;
		if(fromTimestampStr != null){
			try{
				fromTimestamp = Long.parseLong(fromTimestampStr);
			}catch(Exception ex){
				log.error("Error parsing from timestamp value: "+ex.getMessage());
			}					
		}
		String toTimestampStr =  params.containsKey(TO_TIMESTAMP) ? params.get(TO_TIMESTAMP) : null;
		long toTimestamp = 0;
		if(toTimestampStr != null){
			try{
				toTimestamp = Long.parseLong(toTimestampStr);
			}catch(Exception ex){
				log.error("Error parsing from timestamp value: "+ex.getMessage());
			}			
		}
		String clientId =  params.containsKey(CLIENTID) ? params.get(CLIENTID) : null;
		String clientSecret =  params.containsKey(CLIENTSECRET) ? params.get(CLIENTSECRET) : null;
		if(Util.isEmpty(clientId)){
			log.error("Client ID not set.");			
		}else{
			if(Util.isEmpty(clientSecret)){
				log.error("Client secret not set.");
			}else{
				setAppId(clientId, clientSecret);
				TriggerHandler handler = new TriggerHandler();
				if(latitudeStr != null && longitudeStr != null && radiusStr != null){
					handler.createTrigger(triggerId, tags, direction, latitude, longitude, radius, notificationText, notificationUrl, notificationIcon, notificationSound, notificationData, callBackUrl, properties, trackingProfile, times, rateLimit, boundingBoxReturnFormat, geoReturnFormat, fromTimestamp, toTimestamp);			
				}else if(geoJson != null){
					handler.createTrigger(triggerId, tags, direction, geoJson, notificationText, notificationUrl, notificationIcon, notificationSound, notificationData, callBackUrl, properties, trackingProfile, times, rateLimit, boundingBoxReturnFormat, geoReturnFormat, fromTimestamp, toTimestamp);
				}			
			}
		}		
	}
	
	private static void runTrigger(Map<String, String> params){
		
	}
	
	private static void deleteTrigger(Map<String, String> params){
		String triggerIdsStr = params.containsKey(TRIGGER_IDS) ? params.get(TRIGGER_IDS) : null;
		String[] triggerIds = triggerIdsStr.split(",");
		String tagStr = params.containsKey(TAGS) ? params.get(TAGS) : null;
		String[] tags = tagStr.split(",");
		
		String clientId =  params.containsKey(CLIENTID) ? params.get(CLIENTID) : null;
		String clientSecret =  params.containsKey(CLIENTSECRET) ? params.get(CLIENTSECRET) : null;
		if(Util.isEmpty(clientId)){
			log.error("Client ID not set.");			
		}else{
			if(Util.isEmpty(clientSecret)){
				log.error("Client secret not set.");
			}else{
				setAppId(clientId, clientSecret);
				TriggerHandler handler = new TriggerHandler();
				handler.deleteTrigger(triggerIds, tags);
			}
		}
	}
	
	private static void setAppId(String clientId, String clientSecret){
		Params.get().setClientId(clientId);
		Params.get().setClientSecret(clientSecret);
	}
	
	
	
	
	// ##################
	
	private static void query(){
		System.setProperty("http.proxyHost", "127.0.0.1");
		System.setProperty("http.proxyPort", "8888");
		String layerUrl = "http://sampleserver3.arcgisonline.com/ArcGIS/rest/services/Petroleum/KSWells/MapServer/1";
		String where = "OBJECTID=205239";
		QueryParameters queryParams = new QueryParameters();
		String[] outfields = new String[]{"FIELD_NAME","WELL_TYPE","LEASE_NAME"};
		//queryParams.setOutFields(new String[] {"*"});
		queryParams.setOutFields(outfields);		
		queryParams.setOutSpatialReference(SpatialReference.create(SpatialReference.WKID_WGS84));
		queryParams.setWhere(where);
		
		QueryTask task = new QueryTask(layerUrl);
		task.execute(queryParams, new CallbackListener<FeatureResult>() {
			
			@Override
			public void onCallback(FeatureResult result) {
				long count = result.featureCount();
				System.out.println("Count: "+count);
				for (Object record : result) {
					Feature feature = (Feature)record;
					Object id = feature.getAttributeValue("OBJECTID");
					System.out.println("OID: "+id);
				}
			}
			
			@Override
			public void onError(Throwable e) {
				System.out.println("Error: "+e.getMessage());
			}
		});
		
	}
	
	private void createTestTrigger(){
		System.out.println("Creating geotrigger");
		
		JSONObject params = new TriggerBuilder()
		.setTriggerId("Trigger 4711")
		.setTags(new String[]{"London"})
		.setGeo(48.418, 11.6067, 100.0)
		.setDirection(TriggerBuilder.DIRECTION_ENTER)
		.setNotificationText("Besuchen Sie unser Kaffeehaus in London")
		.build();
		
		GeotriggerApiClient.runRequest("trigger/create", params, new GeotriggerApiListener() {
            @Override
            public void onSuccess(JSONObject data) {
                String res = data.toString();
                System.out.println("Response: " + res);
                
            }

            @Override
            public void onFailure(Throwable e) {
                
            }
        });
		
		System.out.println("Program end.");
	}
}
