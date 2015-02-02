package de.esri.geotrigger.core;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import de.esri.geotrigger.config.Arcgis;
import de.esri.geotrigger.config.Configuration;
import de.esri.geotrigger.config.ConfigurationReader;
import de.esri.geotrigger.config.Notification;
import de.esri.geotrigger.config.Query;
import de.esri.geotrigger.config.ReaderException;
import de.esri.geotrigger.config.Trigger;

/**
 * Command line tool to create triggers and perform other geotrigger tasks.
 */
public class Geotrigger {
	private static Logger log = LogManager.getLogger(Geotrigger.class.getName());
	
	public static final String TRIGGER_ID = "triggerid";
	public static final String TRIGGER_IDS = "triggerids";
	public static final String DEVICE_IDS = "deviceids";
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
	public static final String CONFIGFILE = "configfile";

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
		case CommandLineArgs.CREATE_TRIGGER_FROM_SERVICE:
			// create Trigger from feature service
			createTriggerFromService(commandLineArgs.getParameters());
			break;	
		case CommandLineArgs.RUN_TRIGGER:
			// run Trigger
			runTrigger(commandLineArgs.getParameters());
			break;
		case CommandLineArgs.DELETE_TRIGGERS:
			// delete Trigger
			deleteTriggers(commandLineArgs.getParameters());
			break;
		case CommandLineArgs.DELETE_TAGS:
			// delete tags
			deleteTags(commandLineArgs.getParameters());
			break;
		}
	}
	
	/**
	 * Create a geotrigger.
	 * @param params The parameters for the trigger.
	 */
	private static void createTrigger(Map<String, String> params){
		log.info("Creating trigger...");
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
	
	/**
	 * Create geotriggers from a feature service.
	 * @param params The parameters for the trigger.
	 */
	private static void createTriggerFromService(Map<String, String> params){
		log.info("Creating triggers from service...");
		String configXml = params.containsKey(CONFIGFILE) ? params.get(CONFIGFILE) : null;
		log.debug("Config file: "+configXml);
		if(!Util.isEmpty(configXml)){
			File configFile = new File(configXml);			
			if(configFile.exists()){
				ConfigurationReader reader = new ConfigurationReader(configFile);
				try {
					Configuration configuration = reader.read();
					// delete the old triggers
					deleteTriggers(configuration);
					// create new triggers
					generateTriggers(configuration);
				} catch (ReaderException e) {
					log.error("Error parsing configuration file: " + e.getMessage());
				}
			}else{
				log.error("The configuration file does not exist.");
			}
		}
	}
	
	/**
	 * Delete old triggers by the specified tags.
	 */
	private static void deleteTriggers(Configuration configuration){
		List<Query> queries = configuration.getQuery();
		for(Query query : queries){
			Arcgis arcgis = query.getArcgis();
			String clientId = arcgis.getApp().getClientId();		
			String clientSecret = arcgis.getApp().getClientSecret();
			setAppId(clientId, clientSecret);
			
			Trigger trigger = query.getTrigger();			
			String tagStr = trigger.getTags();
			String[] tags = tagStr.split(",");
			
			TriggerHandler triggerHandler = new TriggerHandler();
			triggerHandler.deleteTriggersByTags(tags);
		}
	}
	
	/**
	 * Generate triggers for the features in the services as defined in the configuration.
	 */
	public static void generateTriggers(Configuration configuration){
		List<Query> queries = configuration.getQuery();
		for(Query query : queries){
			Arcgis arcgis = query.getArcgis();
			String featureServiceUrl = arcgis.getFeatureClass();
			String user = arcgis.getLogin().getUser();
			String password = arcgis.getLogin().getPassword();
			String clientId = arcgis.getApp().getClientId();		
			String clientSecret = arcgis.getApp().getClientSecret();
			setAppId(clientId, clientSecret);
			
			Trigger trigger = query.getTrigger();
			String triggerId = trigger.getTriggerID();			
			String tagStr = trigger.getTags();
			String[] tags = tagStr.split(",");
			String direction = trigger.getDirection();
			float radius = trigger.getRadius();
			Notification notification = trigger.getNotification();
			String notificationText = notification.getText();
			String notificationUrl = notification.getUrl();
			String notificationData = notification.getData();
			String where = trigger.getWhere();
			
			TriggerHandler triggerHandler = new TriggerHandler();
			triggerHandler.createTriggersFromService(featureServiceUrl, user, password, triggerId, tags, direction, radius, notificationText, notificationUrl, notificationData, where);
		}
	}	
	
	/**
	 * Delete triggers by tags.
	 * @param params The parameters for the triggers.
	 */
	private static void deleteTriggers(Map<String, String> params){
		log.info("Deleating trigger...");
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
				handler.deleteTriggersByTags(tags);
			}
		}
	}
	
	/**
	 * Run a trigger.
	 * @param params The parameters for the trigger.
	 */
	private static void runTrigger(Map<String, String> params){
		log.info("Run trigger...");
		
		String triggerIdsStr = params.containsKey(TRIGGER_IDS) ? params.get(TRIGGER_IDS) : null;
		String[] triggerIds = triggerIdsStr.split(",");
		String deviceIdsStr = params.containsKey(DEVICE_IDS) ? params.get(DEVICE_IDS) : null;
		String[] deviceIds = deviceIdsStr.split(",");
		
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
				handler.runTrigger(triggerIds, deviceIds);
			}
		}
	}
	
	/**
	 * Delete tags.
	 * @param params The parameters for the trigger.
	 */
	private static void deleteTags(Map<String, String> params){
		log.info("Deleating tags...");
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
				handler.deleteTags(tags);
			}
		}
	}
	
	private static void setAppId(String clientId, String clientSecret){
		Params.get().setClientId(clientId);
		Params.get().setClientSecret(clientSecret);
	}
}
