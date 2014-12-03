package de.esri.osm;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	
	public void generateTriggers(){
		log.info("Creating triggers...");
		
		List<Query> queries = configuration.getQuery();
		for(Query query : queries){
			Arcgis arcgis = query.getArcgis();
			String featureServiceUrl = arcgis.getFeatureClass();
			String user = arcgis.getLogin().getUser();
			String password = arcgis.getLogin().getPassword();
			String clientId = arcgis.getApp().getClientId();		
			String clientSecret = arcgis.getApp().getClientSecret();
			
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
			triggerHandler.createTriggersFromService(featureServiceUrl, user, password, clientId, clientSecret, triggerId, tags, direction, radius, notificationText, notificationUrl, notificationData, where);
		}
		
		log.info("Triggers created.");
	}
	
	public void deleteTriggers(){
		log.info("Delete triggers...");
		List<Query> queries = configuration.getQuery();
		for(Query query : queries){
			Arcgis arcgis = query.getArcgis();
			String clientId = arcgis.getApp().getClientId();		
			String clientSecret = arcgis.getApp().getClientSecret();
			
			Trigger trigger = query.getTrigger();			
			String tagStr = trigger.getTags();
			String[] tags = tagStr.split(",");
			
			TriggerHandler triggerHandler = new TriggerHandler();
			triggerHandler.deleteTriggersByTags(tags, clientId, clientSecret);
		}
		log.info("Triggers deleted.");
	}
}
