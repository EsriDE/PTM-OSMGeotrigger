package de.esri.osm;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.esri.osm.config.Configuration;
import de.esri.osm.data.OSM;
import de.esri.osm.data.OSMNode;

public class TriggerGenerator {
	private static Logger log = LogManager.getLogger(TriggerGenerator.class.getName());
	private Configuration configuration;

	public TriggerGenerator(Configuration configuration){
		this.configuration = configuration;
	}
	
	public void generateTriggers(OSM osm){
		log.info("Creating triggers...");
		
		List<OSMNode> osmNodes = osm.getOsmNodes();		
		// loop through the osm nodes and create a trigger for each node
		for(OSMNode osmNode : osmNodes){
			
		}
		
		log.info("Triggers created.");
	}
}
