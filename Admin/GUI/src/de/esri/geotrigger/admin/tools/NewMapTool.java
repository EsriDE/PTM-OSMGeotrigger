package de.esri.geotrigger.admin.tools;

import com.esri.map.JMap;
import com.esri.map.LayerList;

public class NewMapTool {
	private JMap map;
	
	public NewMapTool(){
		
	}
	
	public NewMapTool(JMap map){
		this.map = map;
	}
	
	public void setMap(JMap map){
		this.map = map;
	}
	
	public void createNewMap(){
		//clear existing layers
		LayerList layers = map.getLayers();
		layers.clear();
	}
}
