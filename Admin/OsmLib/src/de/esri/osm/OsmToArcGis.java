package de.esri.osm;

import java.io.File;
import java.util.List;

import de.esri.osm.config.Configuration;
import de.esri.osm.config.ConfigurationReader;
import de.esri.osm.config.ReaderException;
import de.esri.osm.config.Configuration.Query;
import de.esri.osm.config.Configuration.Query.Arcgis;
import de.esri.osm.config.Configuration.Query.Overpass;
import de.esri.osm.data.OSM;



public class OsmToArcGis {
	
	private Configuration configuration;

	public static void main(String[] args) {
		if(args.length > 0){
			
			String xmlConfig = args[0];
			OsmToArcGis osmToArcGis = new OsmToArcGis(xmlConfig);
			osmToArcGis.process();
			
		}
		else
		{
			//TODO Rainald Loggen
		}
	}
	
	public OsmToArcGis(String xmlConfig)
	{
		//Read configuration
		this.configuration = parseConfig(xmlConfig);
	}
	
	private void process()
	{
		//Read OSM over OverpassAPI
		OSM osm = readOSM();
				
		//Create Feature Services
		createFeatureServices(osm);
				
		//Create Trigger
		createTrigger();
	}
	
	/**
	 * TODO
	 * 
	 * @param xmlConfig
	 * @return
	 */
	private Configuration parseConfig(String xmlConfig){
		File file = new File(xmlConfig);
		ConfigurationReader reader = new ConfigurationReader(file);
		this.configuration = null;
		try {
			this.configuration = reader.read();
			List<Query> queries = configuration.getQuery();
			for(int i = 0; i < queries.size(); i++){
				Query query = queries.get(i);
				Overpass overpass = query.getOverpass();
				String overpassUrl = overpass.getUrl();
				Arcgis arcgis = query.getArcgis();
				String featureClass = arcgis.getFeatureClass();
				String type = arcgis.getType();
				
			}
			
		} catch (ReaderException e) {
			e.printStackTrace();
			//TODO Rainald abfangen, richtiges Logging
		}
		
		return configuration;
	}
	
	private OSM readOSM()
	{
		OverpassHandler overpassHandler = new OverpassHandler(configuration);
		return overpassHandler.read();
	}
	
	private void createFeatureServices(OSM osm)
	{
		FeatureClassHandler featureClassHandler = new FeatureClassHandler(configuration, osm);
		featureClassHandler.write();
	}
	
	/**
	 * 
	 */
	private void createTrigger()
	{
		//TODO Eva: Dummy-Feature Services anlegen
		//TODO Rainald, mit configuration
	}
}
