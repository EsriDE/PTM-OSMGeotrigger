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

/**
 * The TriggerBuilder class is used to create a JSON object with all the values of a trigger.
 */
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
    
    /**
     * Constructor of the TriggerBuilder.
     */
    public TriggerBuilder(){
        trigger = new JSONObject();
        condition = new JSONObject();
        action = new JSONObject();
        notification = new JSONObject();
    }
    
    /**
     * Set the trigger id.
     * @param id The trigger id.
     * @return The instance of this TriggerBuilder.
     */
    public TriggerBuilder setTriggerId(String id){
        return put(trigger, TRIGGER_ID, id);
    }

    /**
     * Set the array of tags for the trigger.
     * @param tags The array of tags.
     * @return The instance of this TriggerBuilder.
     */
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
    
    /**
     * Set the direction of the trigger.
     * @param direction The direction of the trigger.
     * @return The instance of this TriggerBuilder.
     */
    public TriggerBuilder setDirection(String direction){
        return put(condition, CONDITION_DIRECTION, direction);
    }
    
    /**
     * Set the geometry for the trigger. The geometry is a circle.
     * @param latitude The latitude value.
     * @param longitude The longitude value.
     * @param distance The radius.
     * @return The instance of this TriggerBuilder.
     */
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
    
    /**
     * Set the geo JSON for the geometry of the trigger. The geometry is a polygon.
     * @param geoJson The geo JSON for the geometry of the trigger. 
     * @return The instance of this TriggerBuilder.
     */
    public TriggerBuilder setGeoFromGeoJSON(JSONObject geoJson){
        JSONObject geo = new JSONObject();
        try{
            geo.put(FORMAT_GEOJSON, geoJson);
        }catch(JSONException e){
        	log.error("Error setting 'geo' from GeoJSON. " + e.getMessage());
        }
        return put(condition, CONDITION_GEO, geo);
    }
    
    /**
     * Set the Esri JSON for the geometry of the trigger. The geometry is a polygon.
     * @param esriJson The Esri JSON for the geometry of the trigger.
     * @return The instance of this TriggerBuilder.
     */
    public TriggerBuilder setGeoFromEsriJSON(JSONObject esriJson){
        JSONObject geo = new JSONObject();
        try{
            geo.put(FORMAT_ESRIJSON, esriJson);
        }catch(JSONException e){
        	log.error("Error setting 'geo' from Esri JSON. " + e.getMessage());
        }
        return put(condition, CONDITION_GEO, geo);
    }    
    
    /**
     * Set the notification text.
     * @param text The notification text.
     * @return The instance of this TriggerBuilder.
     */
    public TriggerBuilder setNotificationText(String text){
        return put(notification, NOTIFICATION_TEXT, text);
    }

    /**
     * Set the notification url.
     * @param url The notification url.
     * @return The instance of this TriggerBuilder.
     */
    public TriggerBuilder setNotificationUrl(String url){
        return put(notification, NOTIFICATION_URL, url);
    }
 
    /**
     * Set the notification url.
     * @param url The notification url.
     * @return The instance of this TriggerBuilder.
     */
    public TriggerBuilder setNotificationUrl(URL url){
        return put(notification, NOTIFICATION_URL, url.toString());
    }

    /**
     * Set the notification sound.
     * @param sound The notification sound.
     * @return The instance of this TriggerBuilder.
     */
    public TriggerBuilder setNotificationSound(String sound){
        return put(notification, NOTIFICATION_SOUND, sound);
    }

    /**
     * Set the notification icon.
     * @param icon The notification icon.
     * @return The instance of this TriggerBuilder.
     */
    public TriggerBuilder setNotificationIcon(String icon){
        return put(notification, NOTIFICATION_ICON, icon);
    }

    /**
     * Set the notification data.
     * @param data The notification data as a JSON object.
     * @return The instance of this TriggerBuilder.
     */
    public TriggerBuilder setNotificationData(JSONObject data){
        return put(notification, NOTIFICATION_DATA, data);
    }
    
    /**
     * Set the callback url.
     * @param url The callback url.
     * @return The instance of this TriggerBuilder.
     */
    public TriggerBuilder setCallbackUrl(String url){
        return put(condition, ACTION_CALLBACK_URL, url);
    }

    /**
     * Set the callback url.
     * @param url The callback url.
     * @return The instance of this TriggerBuilder.
     */
    public TriggerBuilder setCallbackUrl(URL url){
        return put(condition, ACTION_CALLBACK_URL, url.toString());
    }
    
    /**
     * Set the properties.
     * @param properties The properties.
     * @return The instance of this TriggerBuilder.
     */
    public TriggerBuilder setProperties(JSONObject properties){
        return put(trigger, TRIGGER_PROPERTIES, properties);
    }
    
    /**
     * Set the tracking profile.
     * @param profile The tracking profile.
     * @return The instance of this TriggerBuilder.
     */
    public TriggerBuilder setTrackingProfile(String profile){
        return put(action, ACTION_TRACKING_PROFILE, profile);
    }

    /**
     * Set the times value.
     * @param times The times value.
     * @return The instance of this TriggerBuilder.
     */
    public TriggerBuilder setTimes(int times){
        return put(trigger, TRIGGER_TIMES, Integer.valueOf(times));
    }

    /**
     * Set the rate limit (in seconds).
     * @param seconds The rate limit (in seconds).
     * @return The instance of this TriggerBuilder.
     */
    public TriggerBuilder setRateLimit(int seconds){
        return put(trigger, TRIGGER_RATE_LIMIT, Integer.valueOf(seconds));
    }
    
    /**
     * Set the bounding box return format.
     * @param format The bounding box return format.
     * @return The instance of this TriggerBuilder.
     */
    public TriggerBuilder setBoundingBoxReturnFormat(String format){
        return put(trigger, TRIGGER_BOUNDING_BOX, format);
    }
    
    /**
     * Set the geo return format.
     * @param format The geo return format.
     * @return The instance of this TriggerBuilder.
     */
    public TriggerBuilder setGeoReturnFormat(String format){
        return put(trigger, TRIGGER_GEO_FORMAT, format);
    }
    
    /**
     * Set the from time stamp value (as date).
     * @param date The from time stamp value as date.
     * @return The instance of this TriggerBuilder.
     */
    public TriggerBuilder setFromTimestamp(Date date){
    	String iso8601 = formatTimestamp(date);
        return put(condition, CONDITION_FROM_TIMESTAMP, iso8601);
    }

    /**
     * Set the from time stamp value (in milliseconds).
     * @param millis The from time stamp in milliseconds.
     * @return The instance of this TriggerBuilder.
     */
    public TriggerBuilder setFromTimestamp(long millis){
    	return setFromTimestamp(new Date(millis));
    }

    /**
     * Set the to time stamp value (as date).
     * @param date The to time stamp value as date.
     * @return The instance of this TriggerBuilder.
     */
    public TriggerBuilder setToTimestamp(Date date){
        String iso8601 = formatTimestamp(date);
        return put(condition, CONDITION_TO_TIMESTAMP, iso8601);
    }

    /**
     * Set the to time stamp value (in milliseconds).
     * @param millis The to time stamp in milliseconds.
     * @return The instance of this TriggerBuilder.
     */
    public TriggerBuilder setToTimestamp(long millis){
        return setToTimestamp(new Date(millis));
    }
    
    /**
     * Build the JSON object for the trigger.
     * @return The JSON object for the trigger.
     */
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
