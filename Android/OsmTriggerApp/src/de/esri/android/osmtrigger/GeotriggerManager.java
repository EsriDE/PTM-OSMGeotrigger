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
import android.widget.Toast;


public class GeotriggerManager {
	// define the categories used in your service (insert your own categories)
	// public static final String[] CATEGORIES = new String[]{"FIXME","Natural Water"};
	//public static final String[] CATEGORIES = new String[]{"amenity","footway", "historic", "bus_stop","nutzung","highway"};
	//public static final String[] CATEGORIES = new String[]{"amenity","footway", "historic", "bus_stop"};
	private static final String TAG = "OSM Geotrigger";
	private static String[] categories;
	private Activity activity;
	
	public GeotriggerManager(Activity activity){
		this.activity = activity;
	}
	
	public String[] getCategories(){
		return categories;
	}
	
	public void getTagList(){
    	Log.d(TAG, "Get tag list...");
    	GeotriggerApiClient.runRequest(activity, "tag/list", new GeotriggerApiListener() {
            public void onSuccess(JSONObject data) {
                try {
					JSONArray tags = data.getJSONArray("tags");
					categories = new String[tags.length()];
					for(int i = 0; i < tags.length(); i++){
						JSONObject tag = tags.getJSONObject(i);
						String tagName = tag.getString("name");
						Log.d(TAG, "Tag: " + tagName);
						categories[i] = tagName;
					}
				} catch (JSONException e) {
					Log.e(TAG, "Error reading JSON: " + e.getMessage());
				}
            }

            public void onFailure(Throwable error) {
                Log.d(TAG, "Failed to get tag list.", error);
            }
        });   
	}
	
	/**
	 * Sets the selected search categories by updating the tags registered for the device.
	 */
	public void setSearchCategories(){
		if(categories != null){
			List<String> activeCategories = new ArrayList<String>();
			SharedPreferences preferences = activity.getPreferences(Context.MODE_PRIVATE);		
			for(String category : categories){
				boolean isActive = preferences.getBoolean(category, false);
				if(isActive){
					activeCategories.add(category);
				}
			}
			registerTagsForDevice(activeCategories);			
		}
	}
	
	/**
	 * Register tags (according to the selected categories) for the device.
	 * @param activeCategories The active categories.
	 */
    private void registerTagsForDevice(List<String> activeCategories){
    	Log.d(TAG, "Register tags for device...");
    	JSONObject params = new JSONObject();
    	JSONArray tagArray = new JSONArray();
    	for(String category : activeCategories){
    		tagArray.put(category);
    	}
        try {
            params.put("setTags", tagArray);
        } catch (JSONException e) {
            Log.e(TAG, "Error creating device update parameters.", e);
        }

        GeotriggerApiClient.runRequest(activity, "device/update", params, new GeotriggerApiListener() {
            public void onSuccess(JSONObject data) {
                Log.d(TAG, "Device updated: " + data.toString());
                Toast.makeText(activity, "Registriert beim Geotrigger Service", Toast.LENGTH_LONG).show();
            }

            public void onFailure(Throwable error) {
                Log.d(TAG, "Failed to update device.", error);
            }
        });
    }
    
    /**
     * Create a notification for a trigger. 
     */
    public void createNotification(){
    	((OsmTriggerActivity)activity).showObject();
    }
    
    /**
     * Run a trigger specified by its id.
     * @param triggerId The trigger ID.
     */
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
}
