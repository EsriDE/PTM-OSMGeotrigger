package de.esri.geotrigger.core;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TriggerBuilder {
	private static Logger log = LogManager.getLogger(TriggerBuilder.class.getName());
	public static final String DIRECTION_ENTER = "enter";
	public static final String DIRECTION_LEAVE = "leave";
	private static String ACTION_NOTIFICATION = "notification";
	private static String ACTION_CALLBACK_URL = "callbackUrl";
	private static String ACTION_TRACKING_PROFILE = "trackingProfile";
    private static String TRIGGER_ID = "triggerId";
    private static String TRIGGER_TAGS = "setTags";
    private static String TRIGGER_PROPERTIES = "properties";
	private static String TRIGGER_ACTION = "action";
	private static String TRIGGER_CONDITION = "condition";
	private static String TRIGGER_TIMES = "times";
	private static String TRIGGER_RATE_LIMIT = "rateLimit";
    private static String TRIGGER_BOUNDING_BOX = "boundingBox";
    private static String TRIGGER_GEO_FORMAT = "geoFormat";
	private static String CONDITION_DIRECTION = "direction";
	private static String CONDITION_GEO = "geo";
    private static String CONDITION_FROM_TIMESTAMP = "fromTimestamp";
    private static String CONDITION_TO_TIMESTAMP = "toTimestamp";
    private static String GEO_LATITUDE = "latitude";
    private static String GEO_LONGITUDE = "longitude";
    private static String GEO_DISTANCE = "distance";
    private static String NOTIFICATION_TEXT = "text";
    private static String NOTIFICATION_URL = "url";
    private static String NOTIFICATION_SOUND = "sound";
    private static String NOTIFICATION_DATA = "data";
    private static String NOTIFICATION_ICON = "icon";
    public static String FORMAT_GEOJSON = "geojson";
    public static String FORMAT_ESRIJSON = "esrijson";
    private JSONObject trigger;
    private JSONObject condition;
    private JSONObject action;
    private JSONObject notification;
    
    public TriggerBuilder(){
        trigger = new JSONObject();
        condition = new JSONObject();
        action = new JSONObject();
        notification = new JSONObject();
    }
    
    public TriggerBuilder setTriggerId(String id){
        return put(trigger, TRIGGER_ID, id);
    }

    public TriggerBuilder setTags(String[] tags){
        JSONArray tagArray = new JSONArray();
        String[] array = tags;
        int len = array.length;
        for(int i = 0; i < len; i++){
            String t = array[i];
            tagArray.put(t);
        }
        return put(trigger, TRIGGER_TAGS, tagArray);
    }
    
    public TriggerBuilder setDirection(String direction){
        return put(condition, CONDITION_DIRECTION, direction);
    }
    
    public TriggerBuilder setGeo(double latitude, double longitude, double distance){
        JSONObject geo = new JSONObject();
        try{
            geo.put(GEO_LATITUDE, latitude);
            geo.put(GEO_LONGITUDE, longitude);
            geo.put(GEO_DISTANCE, distance);
        }catch(JSONException e){
        	log.error("Error setting 'geo' for point and distance. " + e.getMessage());
        }
        return put(condition, CONDITION_GEO, geo);
    }
    
    public TriggerBuilder setGeoFromGeoJSON(JSONObject geoJson){
        JSONObject geo = new JSONObject();
        try{
            geo.put(FORMAT_GEOJSON, geoJson);
        }catch(JSONException e){
        	log.error("Error setting 'geo' from GeoJSON. " + e.getMessage());
        }
        return put(condition, CONDITION_GEO, geo);
    }
    
    public TriggerBuilder setGeoFromEsriJSON(JSONObject esriJson){
        JSONObject geo = new JSONObject();
        try{
            geo.put(FORMAT_ESRIJSON, esriJson);
        }catch(JSONException e){
        	log.error("Error setting 'geo' from Esri JSON. " + e.getMessage());
        }
        return put(condition, CONDITION_GEO, geo);
    }    
    
    public TriggerBuilder setNotificationText(String text){
        return put(notification, NOTIFICATION_TEXT, text);
    }

    public TriggerBuilder setNotificationUrl(String url){
        return put(notification, NOTIFICATION_URL, url);
    }

    public TriggerBuilder setNotificationUrl(URL url){
        return put(notification, NOTIFICATION_URL, url.toString());
    }

    public TriggerBuilder setNotificationSound(String sound){
        return put(notification, NOTIFICATION_SOUND, sound);
    }

    public TriggerBuilder setNotificationIcon(String icon){
        return put(notification, NOTIFICATION_ICON, icon);
    }

    public TriggerBuilder setNotificationData(JSONObject data){
        return put(notification, NOTIFICATION_DATA, data);
    }
    
    public TriggerBuilder setCallbackUrl(String url){
        return put(condition, ACTION_CALLBACK_URL, url);
    }

    public TriggerBuilder setCallbackUrl(URL url){
        return put(condition, ACTION_CALLBACK_URL, url.toString());
    }
    
    public TriggerBuilder setProperties(JSONObject properties){
        return put(trigger, TRIGGER_PROPERTIES, properties);
    }
    
    public TriggerBuilder setTrackingProfile(String profile){
        return put(action, ACTION_TRACKING_PROFILE, profile);
    }

    public TriggerBuilder setTimes(int times){
        return put(trigger, TRIGGER_TIMES, Integer.valueOf(times));
    }

    public TriggerBuilder setRateLimit(int seconds){
        return put(trigger, TRIGGER_RATE_LIMIT, Integer.valueOf(seconds));
    }
    
    public TriggerBuilder setBoundingBoxReturnFormat(String format){
        return put(trigger, TRIGGER_BOUNDING_BOX, format);
    }
    
    public TriggerBuilder setGeoReturnFormat(String format){
        return put(trigger, TRIGGER_GEO_FORMAT, format);
    }
    
    public TriggerBuilder setFromTimestamp(Date date){
    	String iso8601 = formatTimestamp(date);
        return put(condition, CONDITION_FROM_TIMESTAMP, iso8601);
    }

    public TriggerBuilder setFromTimestamp(long millis){
    	return setFromTimestamp(new Date(millis));
    }

    public TriggerBuilder setToTimestamp(Date date){
        String iso8601 = formatTimestamp(date);
        return put(condition, CONDITION_TO_TIMESTAMP, iso8601);
    }

    public TriggerBuilder setToTimestamp(long millis){
        return setToTimestamp(new Date(millis));
    }
    
    public JSONObject build(){
        try{
            action.put(ACTION_NOTIFICATION, notification);
            trigger.put(TRIGGER_ACTION, action);
            trigger.put(TRIGGER_CONDITION, condition);
        }
        catch(JSONException e){
        	log.error("Could not build the JSONObject. " + e.getMessage());
        }
        return trigger;
    }
    
    private TriggerBuilder put(JSONObject json, String key, Object val){
        try{
            json.put(key, val);
        }
        catch(JSONException e){
        	log.error("Error building geotrigger json. Invalid value for key: " + key + ". " + e.getMessage());
        }
        return this;
    }
    
    public static String formatTimestamp(Date date){
        if(date != null){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
            return dateFormat.format(date);
        } else{
            return null;
        }
    }
}
