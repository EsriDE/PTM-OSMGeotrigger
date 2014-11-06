package de.esri.osm.data;

import java.util.List;

public class OSM {
	private List<OSMNode> osmNodes;
	
	private List<OSMWay> osmWays;
	
	/**
	 * Constructor.
	 * 
	 * @param osmNodes The OSM nodes.
	 * @param osmWays The OSM ways.
	 */
	public OSM(List<OSMNode> osmNodes, List<OSMWay> osmWays)
	{
		this.osmNodes = osmNodes;
		this.osmWays = osmWays;
	}
	
	/**
	 * Gets the OSM nodes.
	 * 
	 * @return The OSM nodes.
	 */
	public List<OSMNode> getOsmNodes() {
		return osmNodes;
	}
	
	/**
	 * Gets the OSM ways.
	 * 
	 * @return The OSM ways.
	 */
	public List<OSMWay> getOsmWays() {
		return osmWays;
	}
}
