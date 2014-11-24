package de.esri.android.osmtrigger;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.esri.android.geotrigger.GeotriggerApiClient;
import com.esri.android.geotrigger.GeotriggerApiListener;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class GeotriggerManager {
	public static final String[] CATEGORIES = new String[]{"FIXME","Natural Water"};
	private static final String TAG = "OSM Geotrigger";
	private Activity activity;
	
	public GeotriggerManager(Activity activity){
		this.activity = activity;
	}
	
	public void setSearchCategories(){
		List<String> activeCategories = new ArrayList<String>();
		SharedPreferences preferences = activity.getPreferences(Context.MODE_PRIVATE);		
		for(String category : CATEGORIES){
			boolean isActive = preferences.getBoolean(category, false);
			if(isActive){
				activeCategories.add(category);
			}
		}
		registerTagsForDevice(activeCategories);
	}
	
	//TODO Webinar
    private void registerTagsForDevice(List<String> activeCategories){
    	Log.d(TAG, "Register tags for device...");
    	JSONObject params = new JSONObject();
    	JSONArray tagArray = new JSONArray();
    	for(String category : activeCategories){
    		tagArray.put(category);
    	}
        try {
            params.put("setTags", tagArray); 	//TODO Webinar Alte Tags werden durch neue Tags ersetzt anders als bei addTag
        } catch (JSONException e) {
            Log.e(TAG, "Error creating device update parameters.", e);
        }

        GeotriggerApiClient.runRequest(activity, "device/update", params, new GeotriggerApiListener() {
            public void onSuccess(JSONObject data) {
                Log.d(TAG, "Device updated: " + data.toString());
            }

            public void onFailure(Throwable error) {
                Log.d(TAG, "Failed to update device.", error);
            }
        });
    }
    
    // TODO Rainald löschen
    //TODO Webinar Workaround, wenn Geotrigger Service nicht funktioniert: Notification wird angezeigt
    public void createNotification(String triggerId){
    	((OsmTriggerActivity)activity).showObject();
    }
    
    //TODO Webinar Workaround, um Geotrigger auszulösen, ohne vor Ort zu sein.
    public void runTrigger(String triggerId){
    	Log.d(TAG, "Run trigger...");
    	JSONObject params = new JSONObject();
        try {
            params.put("triggerIds", triggerId);
        } catch (JSONException e) {
            Log.e(TAG, "Error testing trigger with trigger/run.", e);
        }

        GeotriggerApiClient.runRequest(activity, "trigger/run", params, new GeotriggerApiListener() {
            public void onSuccess(JSONObject data) {
                Log.d(TAG, "Trigger run successful: " + data.toString());
            }

            public void onFailure(Throwable error) {
                Log.d(TAG, "Failed to run trigger.", error);
            }
        });
    }
    
    //TODO Rainald löschen
    public void sendNotification(){
    	Log.d(TAG, "Sending notification...");
    	JSONObject params = new JSONObject();
    	try {
    	    params.put("text", "Push notification from Geotrigger Service!");
    	    params.put("url", "http://developers.arcgis.com");
    	} catch (JSONException e) {
    	    Log.e(TAG, "Error creating device/notify params", e);
    	}

    	GeotriggerApiClient.runRequest(activity, "device/notify", params, new GeotriggerApiListener() {
    	    public void onSuccess(JSONObject json) {
    	        Log.i(TAG, "device/notify success: " + json);
    	    }

    	    public void onFailure(Throwable error) {
    	        Log.e(TAG, "device/notify failure", error);
    	    }
    	});
    }
}
