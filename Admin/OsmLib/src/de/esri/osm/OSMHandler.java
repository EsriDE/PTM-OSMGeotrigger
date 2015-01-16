package de.esri.osm;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.StatusLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.esri.geotrigger.core.HttpUtil;
import de.esri.geotrigger.core.JsonRequestListener;
import de.esri.geotrigger.core.Util;
import de.esri.osm.config.Configuration;
import de.esri.osm.config.Overpass;
import de.esri.osm.config.Query;
import de.esri.osm.data.ArcGISJSONObject;
import de.esri.osm.data.ArcGISRequestHandler;
import de.esri.osm.data.OSMJSON2ArcGISJSONConverter;

public class OSMHandler 
{
	private static Logger log = LogManager.getLogger(OSMHandler.class.getName());
	
	private List<Query> queries ;
	
	private List<String> deletedFeaturesForServices = new ArrayList<String>();
	
	public OSMHandler(Configuration configuration)
	{
		this.queries = configuration.getQuery();
	}
	
	public void start()
	{		
		int i = 1;
		
		for(final Query query : this.queries)
		{
			log.info("Start with query " + i);
			
			final String featureClassUrl = query.getArcgis().getFeatureClass();
			if(deletedFeaturesForServices.contains(featureClassUrl))
			{
				onArcGISFeaturesDeleted(query);
			}
			else
			{
				deleteFeatures(query);
			}
			
			log.info("Query " + i + " finished.");
			
			i++;
		}
	}
	
	private void deleteFeatures(final Query query)
	{				
		String token = requestToken(query);
		final String featureClassUrl = query.getArcgis().getFeatureClass();
		
		log.debug("Start deleting features from feature service " + featureClassUrl);
		
		ArcGISRequestHandler.deleteFeatures(featureClassUrl, token, new JsonRequestListener()
		{
			@Override
			public void onSuccess(JSONObject json) {
				log.debug("Success requesting deleteFeatures: " + json.toString());
				
				if(! deletedFeaturesForServices.contains(featureClassUrl))
				{
					deletedFeaturesForServices.add(featureClassUrl);
				}
				
				onArcGISFeaturesDeleted(query);
			}
			
			@Override
			public void onFailure(Throwable error) {
				log.error("Error requesting deleteFeatures: " + error.getMessage());
			}
			
			@Override
			public void onError(JSONObject jsonObject, StatusLine statusLine) {
				log.error("Error requesting deleteFeatures - code: " + statusLine.getStatusCode() + ", error: " + jsonObject.toString());
			}
		});
	}
	
	private void onArcGISFeaturesDeleted(Query query)
	{
		downloadOSMFeatures(query);		
	}
	
	private void downloadOSMFeatures(Query query)
	{	
		Overpass overpass = query.getOverpass();
		String url = overpass.getUrl();
		
		log.debug("Start downloading OSM features from " + url);
		String response = HttpUtil.getRequest(url);
		//log.debug("OSM features response:\n" + response);
		JSONObject responseJsonOSM = null;
		
		try
		{
			responseJsonOSM = new JSONObject(response);
		}
		catch(Exception ex)
		{
			log.error("Error downloading OSM features: " + ex.getMessage() + "\n" + response);
			return;
		}
		
		JSONArray osmElements = null;
		try {
			osmElements = responseJsonOSM.getJSONArray("elements");
		} catch (JSONException e) {
			log.error("Could not get elements: " + e.getMessage());
		}
		
		onOSMFeaturesDownloaded(osmElements, query);
	}
	
	private void onOSMFeaturesDownloaded(JSONArray osmElements, Query query)
	{
		String token = requestToken(query);	
		
		convert(token, osmElements, query);
	}
	
	/**
	 * Requests a user token.
	 * 
	 * An empty string is return 
	 * - if the user or password is empty.
	 * - if an error occurs.
	 * 
	 * @param query The query.
	 * @return The token or an empty string.
	 */
	private String requestToken(Query query)
	{
		String token = "";
		
		String user = query.getArcgis().getLogin().getUser();
		String password = query.getArcgis().getLogin().getPassword();
		
		if(!Util.isEmpty(user) && !Util.isEmpty(password))
		{
			log.debug("Start requesting token for user " + user);
			token = ArcGISRequestHandler.requestToken(user, password);
		}
		
		return token;
	}

	private void convert(String token, JSONArray osmElements, Query query)
	{
		log.debug("Start converting OSM features to ArcGIS features");
		for(int i = 0; i < osmElements.length(); i++)
		{
			JSONObject osmElement = null;
			try {
				osmElement = osmElements.getJSONObject(i);
			} catch (JSONException e) {
				log.error("Could not get OSM element: " + e.getMessage());
			}
			
			String json;
			try 
			{
				ArcGISJSONObject arcGISJSONObject = OSMJSON2ArcGISJSONConverter.convert(osmElement, query);
				json = arcGISJSONObject.toString();
			} 
			catch (GeometryTypeSupportException e) 
			{
				log.error(e.getMessage() + " This feature is skipped.");
				continue;
			}	
			
			String featureClassUrl = query.getArcgis().getFeatureClass();
			
			log.debug("Start adding features to feature service " + featureClassUrl);
			ArcGISRequestHandler.addFeatures(featureClassUrl, json, token, new JsonRequestListener()
			{
				@Override
				public void onSuccess(JSONObject json) {
					log.debug("Success requesting addFeatures: " + json.toString());
				}
				
				@Override
				public void onFailure(Throwable error) {
					log.error("Error requesting addFeatures: " + error.getMessage());
				}
				
				@Override
				public void onError(JSONObject jsonObject, StatusLine statusLine) {
					log.error("Error requesting addFeatures - code: " + statusLine.getStatusCode() + ", error: " + jsonObject.toString());
				}
			});
		}
	}
}
