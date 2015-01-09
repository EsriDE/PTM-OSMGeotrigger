package de.esri.osm;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.esri.osm.config.Configuration;
import de.esri.osm.config.ConfigurationReader;
import de.esri.osm.config.ReaderException;



public class OsmToArcGis {
	private static Logger log = LogManager.getLogger(OsmToArcGis.class.getName());
	
	private Configuration configuration;

	/**
	 * Start method of this command line tool.
	 * @param args The command line arguments. Pass the configuration file as a parameter.
	 */
	public static void main(String[] args) {
		if(args.length > 0){
			
			String xmlConfig = args[0];
			File configFile = new File(xmlConfig);			
			if(configFile.exists()){
				OsmToArcGis osmToArcGis = new OsmToArcGis(xmlConfig);
				osmToArcGis.process();
			}else{
				log.error("The configuration file does not exist.");
			}
		}
		else
		{
			log.error("No configuration file specified.");
		}
	}
	
	public OsmToArcGis(String xmlConfig)
	{
		//Read configuration
		this.configuration = parseConfig(xmlConfig);
	}
	
	/**
	 * Read OSM data.
	 * Fill feature services with OSM data.
	 * Create triggers from feature services.
	 */
	private void process()
	{
		//Read OSM over OverpassAPI and fill feature services
		fillFeatureServices();
				
		//Create Trigger
		createTrigger();
	}
	
	/**
	 * Parse the xml configuration file.
	 * @param xmlConfig
	 * @return
	 */
	private Configuration parseConfig(String xmlConfig){
		log.debug("Parsing configuration file...");
		File file = new File(xmlConfig);
		ConfigurationReader reader = new ConfigurationReader(file);
		this.configuration = null;
		try {
			this.configuration = reader.read();
			
		} catch (ReaderException e) {
			log.error("Error parsing configuration file: " + e.getMessage());
		}
		
		return configuration;
	}
	
	/**
	 * Fills features services with OSM data.
	 */
	private void fillFeatureServices()
	{
		OSMHandler osmHandler = new OSMHandler(configuration);
		osmHandler.start();
	}
	
	/**
	 * Create triggers as defined in the configuration file.
	 */
	private void createTrigger()
	{
		TriggerGenerator triggerGenerator = new TriggerGenerator(configuration);
		// delete old triggers
		triggerGenerator.deleteTriggers();
		// create new triggers
		triggerGenerator.generateTriggers();
	}
}
