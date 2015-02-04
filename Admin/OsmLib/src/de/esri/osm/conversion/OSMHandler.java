package de.esri.osm.conversion;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.esri.osm.config.Configuration;
import de.esri.osm.config.Query;

/**
 * Handles the conversion process from OSM Overpass to ArcGIS feature services.
 * 
 * @author Eva Peters
 *
 */
public class OSMHandler 
{
	private static Logger log = LogManager.getLogger(OSMHandler.class.getName());
	
	private List<Query> queries ;
		
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
			
			QueryHandler queryHandler = new QueryHandler(query);
			queryHandler.start();
			
			log.info("Query " + i + " finished.");
			
			i++;
		}
	}
}
