package de.esri.osm;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.esri.geotrigger.core.Params;
import de.esri.geotrigger.core.TriggerHandler;
import de.esri.osm.config.Arcgis;
import de.esri.osm.config.Configuration;
import de.esri.osm.config.Notification;
import de.esri.osm.config.Query;
import de.esri.osm.config.Trigger;
import de.esri.osm.data.OSM;
import de.esri.osm.data.OSMNode;

public class TriggerGenerator {
	private static Logger log = LogManager.getLogger(TriggerGenerator.class.getName());
	private Configuration configuration;

	public TriggerGenerator(Configuration configuration){
		this.configuration = configuration;
	}
	
	/**
	 * Generate triggers for the features in the services as defined in the configuration.
	 */
	public void generateTriggers(){
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
	 * Delete the old triggers by the specified tags.
	 */
	public void deleteTriggers(){
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
	
	private void setAppId(String clientId, String clientSecret){
		Params.get().setClientId(clientId);
		Params.get().setClientSecret(clientSecret);
	}
}
