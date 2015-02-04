package de.esri.osm.conversion;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.esri.osm.config.Configuration;
import de.esri.osm.config.ConfigurationReader;
import de.esri.osm.config.XMLReaderException;



/**
 * Starts the conversion process from OSM Overpass to ArcGIS feature services.
 * 
 * @author Eva Peters
 *
 */
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
				OsmToArcGis osmToArcGis = null;
				try {
					osmToArcGis = new OsmToArcGis(xmlConfig);
					osmToArcGis.process();
				} catch (XMLReaderException e) {
					log.error(e);
				}
			}else{
				log.error("The configuration file does not exist.");
			}
		}
		else
		{
			log.error("No configuration file specified.");
		}
	}
	
	/**
	 * Constructor.
	 * 
	 * @param xmlConfig The XML configuration.
	 * @throws XMLReaderException If the reading process of the XML file failed.
	 */
	public OsmToArcGis(String xmlConfig) throws XMLReaderException
	{
		//Read configuration
		this.configuration = parseConfig(xmlConfig);
	}
	
	/**
	 * Parse the xml configuration file.
	 * @param xmlConfig The XML configuration.
	 * @return The configuration.
	 * @throws XMLReaderException If the reading process of the XML file failed.
	 */
	private Configuration parseConfig(String xmlConfig) throws XMLReaderException{
		log.debug("Parsing configuration file...");
		File file = new File(xmlConfig);
		ConfigurationReader reader = new ConfigurationReader(file);
		this.configuration = null;
		try {
			this.configuration = reader.read();
			
		} catch (XMLReaderException e) {
			throw new XMLReaderException("Error parsing configuration file: " + e.getMessage());
		}
		
		return configuration;
	}

	/**
	 * Read OSM data.
	 * Fill feature services with OSM data.
	 */
	private void process()
	{
		//Read OSM over OverpassAPI and fill feature services
		OSMHandler osmHandler = new OSMHandler(configuration);
		osmHandler.start();
	}
}
