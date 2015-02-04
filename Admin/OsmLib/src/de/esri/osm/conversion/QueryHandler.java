package de.esri.osm.conversion;

import org.apache.http.StatusLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.esri.osm.config.Overpass;
import de.esri.osm.config.Query;
import de.esri.osm.core.ArcGISRequestHandler;
import de.esri.osm.core.HttpUtil;
import de.esri.osm.core.JsonRequestListener;
import de.esri.osm.core.RequestException;
import de.esri.osm.core.TokenListener;
import de.esri.osm.conversion.FeatureHandler;

/**
 * Handles the conversion process from a query.
 * 
 * @author Eva Peters
 *
 */
public class QueryHandler {
	
	private static Logger log = LogManager.getLogger(QueryHandler.class.getName());
	
	private Query query;

	private TokenHandler tokenHandler;
	
	private JSONArray osmElements;
	
	public QueryHandler(Query query)
	{
		this.query = query;
		this.tokenHandler = new TokenHandler(query);
	}
	
	public void start()
	{
		boolean delete = this.query.getArcgis().getDelete();
		if(delete)
		{
			updateToken();
		}
		else
		{
			downloadOSMFeatures();
		}		
	}
	
	/**
	 * Updates the token and continues with the step 'delete features'.
	 */
	private void updateToken()
	{
		log.debug("Start requesting token...");
		this.tokenHandler.generateValidToken(new TokenListener() {
			
			@Override
			public void onSuccess(String message) {
				log.debug(message);
				deleteFeatures();
				
			}
			
			@Override
			public void onError(String message) {
				log.error(message);
				log.error("Query stopped because the authorization had failed.");
			}
		});	
	}
	
	/**
	 * Deletes the features from the feature service and continues with the step 'download OSM features'.
	 */
	private void deleteFeatures()
	{			
		final String featureClassUrl = this.query.getArcgis().getFeatureClass();
		
		log.debug("Start deleting features from feature service " + featureClassUrl);
		
		String token = this.tokenHandler.getToken();
		ArcGISRequestHandler.deleteFeatures(featureClassUrl, token, new JsonRequestListener()
		{
			@Override
			public void onSuccess(JSONObject json) {
				log.debug("Success requesting delete Features: " + json.toString());
				downloadOSMFeatures();
			}
			
			@Override
			public void onFailure(Throwable error) {
				log.error("Error requesting delete Features: " + error.getMessage());
				log.error("Query stopped because the authorization had failed.");
			}
			
			@Override
			public void onError(JSONObject jsonObject, StatusLine statusLine) {
				log.error("Error requesting delete Features - code: " + statusLine.getStatusCode() + ", error: " + jsonObject.toString());
				log.error("Query stopped because the authorization had failed.");
			}
		});
	}
	
	/**
	 * Downloads the OSM features and continues with the conversion.
	 * 
	 * @throws RequestException If the download request failed.
	 */
	private void downloadOSMFeatures()
	{	
		Overpass overpass = this.query.getOverpass();
		String url = overpass.getUrl();
		
		log.debug("Start downloading OSM features from " + url);
		
		String response;
		try {
			response = HttpUtil.getRequest(url);
		} catch (RequestException e) {
			log.error("Query stopped because the download request had failed: " + e.getMessage());
			return;
		}
		
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
		
		try {
			this.osmElements = responseJsonOSM.getJSONArray("elements");
		} catch (JSONException e) {
			log.error("Could not get elements: " + e.getMessage());
			return;
		}
		
		convert();
	}
	
	/**
	 * Converts the OSM data to ArcGIS.
	 */
	private void convert()
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
			
			FeatureHandler featureHandler = new FeatureHandler(osmElement, this.query, this.tokenHandler);
			featureHandler.start();
		}
	}
}
