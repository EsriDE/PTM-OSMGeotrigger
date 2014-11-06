package de.esri.osm.data;

import java.util.Map;

/**
 * An OSM node.
 * 
 * @author evp
 *
 */
public class OSMNode {
	
	private String id;
	
	private String lat;

	private String lon;
	
	private final Map<String, String> tags;
	
	/**
	 * Constructor.
	 * 
	 * @param id The ID.
	 * @param latitude The latitude.
	 * @param longitude The longitude.
	 * @param tags The tags (key-value pairs).
	 */
	public OSMNode(String id, String latitude, String longitude,Map<String, String>tags)
	{
		this.id = id;
		this.lat = latitude;
		this.lon = longitude;
		this.tags = tags;
	}
	
	/**
	 * Gets the object ID.
	 * 
	 * @return The ID.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the latitude.
	 * 
	 * @return The latitude.
	 */
	public String getLat() {
		return lat;
	}

	/**
	 * Gets the longitude.
	 * 
	 * @return The longitude.
	 */
	public String getLon() {
		return lon;
	}
	
	/**
	 * Gets the tags (key-value pairs).
	 * 
	 * @return The tags.
	 */
	public Map<String, String> getTags() {
		return tags;
	}
}
