package de.esri.osm.data;

import java.util.List;
import java.util.Map;

/**
 * An OSM way.
 * 
 * @author evp
 *
 */
public class OSMWay {
	
	private String id;
	
	private List<String> nodeIds;
	
	private final Map<String, String> tags;
	
	/**
	 * Constructor.
	 * 
	 * @param id The ID.
	 * @param nodeIds The referenced node IDs.
	 * @param tags The tags (key-value pairs).
	 */
	public OSMWay(String id, List<String> nodeIds, Map<String, String>tags)
	{
		this.id = id;
		this.nodeIds = nodeIds;
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
	 * Gets the referenced node IDs.
	 * 
	 * @return The referenced node IDs.
	 */
	public List<String> getNodeIds() {
		return nodeIds;
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
