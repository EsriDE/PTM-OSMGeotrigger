package de.esri.geotrigger.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.tasks.query.QueryParameters;
import com.esri.core.tasks.query.QueryTask;

// http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer
// http://sampleserver3.arcgisonline.com/ArcGIS/rest/services/Petroleum/KSPetro/MapServer/0
// http://sampleserver3.arcgisonline.com/ArcGIS/rest/services/Petroleum/KSWells/MapServer/1		OBJECTID=205239  253935 	{FIELD_NAME:{{FIELD_NAME}},WELL_TYPE:{{WELL_TYPE}},LEASE_NAME:{{LEASE_NAME}}}
// FIELD_NAME='AETNA'
public class Geotrigger {
	private static Logger log = LogManager.getLogger(Geotrigger.class.getName());

	public static void main(String[] args) {
		
//		Geotrigger gt = new Geotrigger();
//		gt.createTestTrigger();

//		String text = "Willkommen in {{Stadt}}, der Stadt mit {{Einwohnerzahl}} Einwohnern.";
//		String parsedText = Util.parseAttributes(text, null);
//		System.out.println("Parsed text: "+parsedText);
		
		query();
	}
	
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
		
//		UserCredentials credentials = new UserCredentials();
//		credentials.setUserAccount("rsu4devprog", "devprog42195");
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
