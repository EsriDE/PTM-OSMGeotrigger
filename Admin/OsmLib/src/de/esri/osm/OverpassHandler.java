package de.esri.osm;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.esri.osm.config.Configuration;
import de.esri.osm.data.NoOSMNodeException;
import de.esri.osm.data.NoOSMWayException;
import de.esri.osm.data.OSM;
import de.esri.osm.data.OSMNode;
import de.esri.osm.data.OSMWay;

public class OverpassHandler {
	private static Logger log = LogManager.getLogger(OverpassHandler.class.getName());
	private Configuration configuration;
	
	public OverpassHandler(Configuration configuration)
	{
		this.configuration = configuration;
	}
	
	public OSM read()
	{
		//TODO EVP: Hier weitermachen
		try {
			//TODO: Query aus configuration lesen
			String urlString = "http://www.overpass-api.de/api/xapi?way[bbox=7.1,51.2,7.2,51.3][fixme=*]";
			Document xmlDocument = getXML(urlString);
			OSM osm = getOSM(xmlDocument);
			System.out.println(osm);
		} catch (IOException | SAXException | ParserConfigurationException e) {
			log.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * Downloads a file.
	 * 
	 * @param urlString The URL.
	 * @return The document.
	 * @throws IOException If the file could be downloaded.
	 * @throws ParserConfigurationException If the document could not be built.
	 * @throws SAXException If the document could be parsed.
	 */
	private Document getXML(String urlString) throws IOException, ParserConfigurationException, SAXException 
	{
		URL url = new URL(urlString);
		
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		InputStream inputStream = connection.getInputStream();
		
		Document document = documentBuilder.parse(inputStream);
		
		return document;
	}
	
	public OSM getOSM(Document xmlDocument) {
		List<OSMNode> osmNodes = new ArrayList<OSMNode>();
		List<OSMWay> osmWays = new ArrayList<OSMWay>();

		try {
		
			Node osmRoot = xmlDocument.getFirstChild();
			NodeList osmXMLNodes = osmRoot.getChildNodes();
			for (int i = 0; i < osmXMLNodes.getLength(); i++) 
			{
				Node item = osmXMLNodes.item(i);
				String name = item.getNodeName();
				if (name.equals("node")) 
				{
					OSMNode osmNode = convertNode(item);
					osmNodes.add(osmNode);
				}
				else if (name.equals("way")) 
				{
					OSMWay osmWay = convertWay(item);
					osmWays.add(osmWay);
				}
				
			}

		} catch (NoOSMNodeException e) {
			log.error(e.getMessage());
		} catch (NoOSMWayException e) {
			log.error(e.getMessage());
		}
		
		OSM osm = new OSM(osmNodes, osmWays);
		
		return osm;
	}
	
	/**
	 * Converts an OSM node in the format XML into an {@link OSMNode} object.
	 * 
	 * @param node The OSM node in the format XML.
	 * @return The OSM node.
	 * @throws NoOSMNodeException If the given XML node is not an OSM node.
	 */
	private OSMNode convertNode(Node node) throws NoOSMNodeException
	{
		String name = node.getNodeName();
		
		if (! name.equals("node")) 
		{
			throw new NoOSMNodeException("The given XML node with the name '" + name + "' is not an OSM node.");
		}
		
		NamedNodeMap xmlAttributes = node.getAttributes();
		
		String id =  xmlAttributes.getNamedItem("id").getNodeValue();
		String latitude = xmlAttributes.getNamedItem("lat").getNodeValue();
		String longitude = xmlAttributes.getNamedItem("lon").getNodeValue();
		
		NodeList xmlChildren = node.getChildNodes();
		Map<String, String> tags = new HashMap<String, String>();
		for (int i = 0; i < xmlChildren.getLength(); i++) 
		{
			Node tagItem = xmlChildren.item(i);
			NamedNodeMap tagAttributes = tagItem.getAttributes();
			if (tagAttributes != null) 
			{
				String tagKey = tagAttributes.getNamedItem("k").getNodeValue();
				String tagValue = tagAttributes.getNamedItem("v").getNodeValue();
				
				tags.put(tagKey, tagValue);
			}
		}
		
		OSMNode osmNode = new OSMNode(id, latitude, longitude, tags);
		
		return osmNode;
	}
	
	/**
	 * Converts an OSM way in the format XML into an {@link OSMWay} object.
	 * 
	 * @param node The OSM way in the format XML.
	 * @return The OSM way.
	 * @throws NoOSMWayException If the given XML node is not an OSM way.
	 */
	private OSMWay convertWay(Node node) throws NoOSMWayException
	{
		String name = node.getNodeName();
		
		if (! name.equals("way")) 
		{
			throw new NoOSMWayException("The given XML way with the name '" + name + "' is not an OSM node.");
		}
		
		NamedNodeMap xmlAttributes = node.getAttributes();
		
		String id =  xmlAttributes.getNamedItem("id").getNodeValue();
		
		NodeList xmlChildren = node.getChildNodes();
		List<String> nodes = new ArrayList<String>();
		Map<String, String> tags = new HashMap<String, String>();
		
		for (int i = 0; i < xmlChildren.getLength(); i++) 
		{
			Node tagItem = xmlChildren.item(i);
			String childName = tagItem.getNodeName();
			
			if(childName.equals("nd"))
			{
				NamedNodeMap nodeAttributes = tagItem.getAttributes();
				if (nodeAttributes != null) 
				{
					String nodeRef = nodeAttributes.getNamedItem("ref").getNodeValue();
					nodes.add(nodeRef);
				}
			}
			else if(childName.equals("tag"))
			{
				NamedNodeMap tagAttributes = tagItem.getAttributes();
				if (tagAttributes != null) 
				{
					String tagKey = tagAttributes.getNamedItem("k").getNodeValue();
					String tagValue = tagAttributes.getNamedItem("v").getNodeValue();
					
					tags.put(tagKey, tagValue);
				}
			}
		}
		
		OSMWay osmWay = new OSMWay(id, nodes, tags);
		
		return osmWay;
	}
}
