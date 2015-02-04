package de.esri.osm.conversion;

import org.apache.http.StatusLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import de.esri.osm.config.Query;
import de.esri.osm.core.ArcGISRequestHandler;
import de.esri.osm.core.JsonRequestListener;
import de.esri.osm.core.TokenListener;
import de.esri.osm.data.ArcGISJSONObject;
import de.esri.osm.data.GeometryGenerationException;
import de.esri.osm.data.GeometryReaderException;
import de.esri.osm.data.GeometryTypeSupportException;
import de.esri.osm.data.OSMJSON2ArcGISJSONConverter;

/**
 * Handles the conversion process from a single OSM Overpass object to an ArcGIS feature service.
 * 
 * @author Eva Peters
 *
 */
public class FeatureHandler {
	
	private static Logger log = LogManager.getLogger(FeatureHandler.class.getName());
	
	private JSONObject osmElement;
	
	private String json;
	
	private Query query;
	
	private TokenHandler tokenHandler;
	
	public FeatureHandler(JSONObject osmElement, Query query, TokenHandler tokenHandler)
	{
		this.osmElement = osmElement;
		this.query = query;
		this.tokenHandler = tokenHandler;
	}
	
	/**
	 * Starts the process of conversion and adding the feature to the feature service.
	 */
	public void start()
	{
		convert();
	}
	
	/**
	 * Converts OSM JSON to ArcGIS JSON and continues to update the token.
	 */
	private void convert()
	{
		try 
		{
			ArcGISJSONObject arcGISJSONObject = OSMJSON2ArcGISJSONConverter.convert(this.osmElement, this.query);
			this.json = arcGISJSONObject.toString();
			
			updateToken();
		} 
		catch (GeometryTypeSupportException e) 
		{
			log.error(e.getMessage() + " This feature is skipped.");
		} catch (GeometryGenerationException e) 
		{
			log.error(e.getMessage() + " This feature is skipped.");
		} catch (GeometryReaderException e) 
		{
			log.error(e.getMessage() + " This feature is skipped.");
		}
	}
	
	/**
	 * Updates the token and continues with adding the feature.
	 */
	private void updateToken()
	{
		log.debug("Check token for add features");
		tokenHandler.generateValidToken(new TokenListener() {
			
			@Override
			public void onSuccess(String message) {
				log.debug(message);
				
				addFeature();
			}
			
			@Override
			public void onError(String message) {
				log.error(message);
			}
		});
	}
	
	/**
	 * Adds the feature to the feature class.
	 */
	private void addFeature()
	{
		String featureClassUrl = query.getArcgis().getFeatureClass();
		String token = tokenHandler.getToken();
		
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