package de.esri.geotrigger.admin.tools;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.ags.query.Query;
import com.esri.core.tasks.query.QueryParameters;
import com.esri.core.tasks.query.QueryTask;
import com.esri.map.ArcGISFeatureLayer;
import com.esri.map.JMap;
import com.esri.map.Layer;
import com.esri.map.LayerList;

import de.esri.geotrigger.core.TriggerBuilder;
import de.esri.geotrigger.core.TriggerHandler;
import de.esri.geotrigger.core.Util;

public class CreateMultipleTriggersTool {
	private JMap map;

	
	public CreateMultipleTriggersTool(){
		
	}
	
	public CreateMultipleTriggersTool(JMap map){
		this.map = map;
	}
	
	public void setMap(JMap map){
		this.map = map;
	}
	
	public void showCreateTriggersDialog(){
		JFrame applicationFrame = (JFrame) SwingUtilities.getWindowAncestor(map);
		CreateTriggersDialog createTriggersDialog = new CreateTriggersDialog(applicationFrame);
		createTriggersDialog.setVisible(true);	
	}
	
	private Vector<TriggerLayer> getTriggerLayers(){	
		Vector<TriggerLayer> triggerLayers = new Vector<TriggerLayer>();
		LayerList mapLayers = map.getLayers();
		for(int i = 0; i < mapLayers.size(); i++){
			Layer mapLayer = mapLayers.get(i);
			if(mapLayer instanceof ArcGISFeatureLayer){
				ArcGISFeatureLayer featureLayer = (ArcGISFeatureLayer)mapLayer;
				TriggerLayer triggerLayer = new TriggerLayer();
				triggerLayer.setName(mapLayer.getName());
				triggerLayer.setFeatureLayer(featureLayer);
				triggerLayers.add(triggerLayer);
			}
		}		
		return triggerLayers;
	}
	
	private class TriggerLayer{
		private String name;
		private ArcGISFeatureLayer featureLayer;
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}

		public ArcGISFeatureLayer getFeatureLayer() {
			return featureLayer;
		}

		public void setFeatureLayer(ArcGISFeatureLayer featureLayer) {
			this.featureLayer = featureLayer;
		}
		
		public String toString(){
			return name;
		}
	}
	
	private class CreateTriggersDialog extends JDialog implements ActionListener{
		private JButton createTriggersButton = new JButton("Create");
		private JButton cancelButton = new JButton("Cancel");
		private JComboBox layerComboBox;
		private JComboBox directionComboBox;
		private JTextField triggerIdTextField;
		private JTextField radiusTextField;
		private JTextField tagsTextField;
		private JTextField notificationTextField;
		private JTextField notificationUrlTextField;
		private JTextField notificationDataTextField;
		private JTextField callbackUrlTextField;
		private JTextField propertiesTextField;
		private JComboBox trackingProfileComboBox;
		private JTextField whereTextField;
		
		public CreateTriggersDialog(JFrame parent){
			super(parent);
			setTitle("Create Triggers for Feature Layer");
			setModal(true);		
			
			JPanel triggerPanel = new JPanel();
			triggerPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.anchor = GridBagConstraints.WEST;
			gbc.gridwidth = 2;
			gbc.insets = new Insets(10, 10, 3, 10);
			JLabel headerLabel = new JLabel("Create Trigger for Features in Layer:");
			triggerPanel.add(headerLabel, gbc);
			gbc.gridwidth = 1;
			gbc.gridy = 1;
			gbc.insets = new Insets(3, 10, 15, 10);
			JLabel layerLabel = new JLabel("Feature Layer");
			triggerPanel.add(layerLabel, gbc);			
			gbc.gridx = 1;
			Vector<TriggerLayer> layers = getTriggerLayers();
			layerComboBox = new JComboBox(layers);
			triggerPanel.add(layerComboBox, gbc);							
			gbc.gridwidth = 2;
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.insets = new Insets(5, 10, 5, 10);
			JLabel triggerLabel = new JLabel("Trigger Definition");
			triggerPanel.add(triggerLabel, gbc);
			gbc.gridwidth = 1;
			gbc.gridy = 3;
			JLabel directionLabel = new JLabel("Direction");
			triggerPanel.add(directionLabel, gbc);
			gbc.gridx = 1;
			String[] directions = {TriggerBuilder.DIRECTION_ENTER,TriggerBuilder.DIRECTION_LEAVE};
			directionComboBox = new JComboBox(directions);
			triggerPanel.add(directionComboBox, gbc);
			gbc.gridx = 0;
			gbc.gridy = 4;
			JLabel triggerIdLabel = new JLabel("Trigger ID");
			triggerPanel.add(triggerIdLabel, gbc);
			gbc.gridx = 1;
			triggerIdTextField = new JTextField(10);
			triggerIdTextField.setText("{{OBJECTID}}");
			triggerPanel.add(triggerIdTextField, gbc);			
			gbc.gridx = 0;
			gbc.gridy = 5;
			JLabel radiusLabel = new JLabel("Radius (m)");
			triggerPanel.add(radiusLabel, gbc);
			gbc.gridx = 1;
			radiusTextField = new JTextField(10);
			triggerPanel.add(radiusTextField, gbc);
			gbc.gridx = 0;
			gbc.gridy = 6;
			JLabel tagsLabel = new JLabel("Tags");
			triggerPanel.add(tagsLabel, gbc);
			gbc.gridx = 1;
			tagsTextField = new JTextField(20);
			triggerPanel.add(tagsTextField, gbc);
			// notification
			gbc.gridwidth = 2;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 0;
			gbc.gridy = 7;			
			JPanel notificationPanel = new JPanel();
			notificationPanel.setLayout(new GridBagLayout());
			GridBagConstraints ngbc = new GridBagConstraints();
			ngbc.anchor = GridBagConstraints.WEST;
			ngbc.insets = new Insets(10, 10, 3, 10);
			JLabel notificationTextLabel = new JLabel("Text");
			notificationPanel.add(notificationTextLabel, ngbc);
			ngbc.gridx = 1;			
			notificationTextField = new JTextField(20);
			notificationPanel.add(notificationTextField, ngbc);
			ngbc.gridx = 0;
			ngbc.gridy = 1;
			ngbc.insets = new Insets(3, 10, 3, 10);
			JLabel notificationUrlTextLabel = new JLabel("Url");
			notificationPanel.add(notificationUrlTextLabel, ngbc);
			ngbc.gridx = 1;
			notificationUrlTextField = new JTextField(20);
			notificationPanel.add(notificationUrlTextField, ngbc);
			ngbc.gridx = 0;
			ngbc.gridy = 2;
			ngbc.insets = new Insets(3, 10, 10, 10);
			JLabel notificationDataTextLabel = new JLabel("Data");
			notificationPanel.add(notificationDataTextLabel, ngbc);
			ngbc.gridx = 1;
			notificationDataTextField = new JTextField(20);
			notificationPanel.add(notificationDataTextField, ngbc);	
			notificationPanel.setBorder(BorderFactory.createTitledBorder("Notification"));
			triggerPanel.add(notificationPanel, gbc);					
			// end notification
			gbc.fill = GridBagConstraints.NONE;
			gbc.gridx = 0;
			gbc.gridy = 8;	
			JLabel callbackUrlLabel = new JLabel("Callback URL");
			triggerPanel.add(callbackUrlLabel, gbc);
			gbc.gridx = 1;
			callbackUrlTextField = new JTextField(20);
			triggerPanel.add(callbackUrlTextField, gbc);
			gbc.gridx = 0;
			gbc.gridy = 9;	
			JLabel propertiesLabel = new JLabel("Properties");
			triggerPanel.add(propertiesLabel, gbc);
			gbc.gridx = 1;
			propertiesTextField = new JTextField(20);
			triggerPanel.add(propertiesTextField, gbc);
			gbc.gridx = 0;
			gbc.gridy = 10;	
			gbc.insets = new Insets(5, 10, 15, 10);
			JLabel trackingProfileLabel = new JLabel("Tracking Profile");
			triggerPanel.add(trackingProfileLabel, gbc);
			gbc.gridx = 1;
			String[] trackingProfiles = {" - ", "adaptive", "fine", "rough", "off"};
			trackingProfileComboBox = new JComboBox(trackingProfiles);
			triggerPanel.add(trackingProfileComboBox, gbc);
			gbc.gridx = 0;
			gbc.gridy = 11;	
			JLabel wherelLabel = new JLabel("Where Clause");
			triggerPanel.add(wherelLabel, gbc);
			gbc.gridx = 1;
			whereTextField = new JTextField(20);
			triggerPanel.add(whereTextField, gbc);
			add(triggerPanel, BorderLayout.CENTER);
			
			JPanel buttonPanel = new JPanel();
			createTriggersButton.addActionListener(this);
			buttonPanel.add(createTriggersButton);
			cancelButton.addActionListener(this);
			buttonPanel.add(cancelButton);
			add(buttonPanel, BorderLayout.SOUTH);			
			
			pack();
			setResizable(false);
			setLocationRelativeTo(parent);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Object src = e.getSource();
			if(src.equals(createTriggersButton)){
				queryFeatures();
				this.dispose();
			}else if(src.equals(cancelButton)){
				this.dispose();
			}
		}
		
		private void queryFeatures(){
			TriggerLayer triggerLayer = (TriggerLayer) layerComboBox.getSelectedItem();
			ArcGISFeatureLayer featureLayer = triggerLayer.getFeatureLayer();
			String where = whereTextField.getText();
			if(Util.isEmpty(where)){
				where = "1=1";
			}
			
			Query query = new Query();
			query.setOutFields(new String[] {"*"});
			query.setOutSpatialReference(SpatialReference.create(SpatialReference.WKID_WGS84));
			query.setWhere(where);
			featureLayer.queryFeatures(query, new CallbackListener<FeatureSet>() {
				
				@Override
				public void onCallback(FeatureSet featureSet) {
					createTrigger(featureSet);
				}
				
				@Override
				public void onError(Throwable e) {
					System.out.println("Error: "+e.getMessage());
				}
			});
			
			
//			// query features
//			QueryParameters queryParams = new QueryParameters();
//			queryParams.setOutFields(new String[] {"*"});
//			queryParams.setOutSpatialReference(SpatialReference.create(4326));
//			queryParams.setWhere(where);
//			
////			UserCredentials credentials = new UserCredentials();
////			credentials.setUserAccount("", "");
//			QueryTask task = new QueryTask(layerUrl);
//			task.execute(queryParams, new CallbackListener<FeatureResult>() {
//				
//				@Override
//				public void onCallback(FeatureResult result) {
//					createTrigger(result);
//				}
//				
//				@Override
//				public void onError(Throwable e) {
//					System.out.println("Error: "+e.getMessage());
//				}
//			});
			
		}
		
		private void createTrigger(FeatureSet featureSet){
			String triggerIdStr = triggerIdTextField.getText();
			String direction = (String) directionComboBox.getSelectedItem();
			String radiusStr = radiusTextField.getText();
			double radius = 250.0;
			try{
				radius = Double.parseDouble(radiusStr);				
			}catch(Exception e){
				
			}
			String tagStr = tagsTextField.getText();
			String[] tags = tagStr.split(",");
			String notificationText = notificationTextField.getText();
			String notificationUrl = notificationUrlTextField.getText();
			String notificationDataStr = notificationDataTextField.getText();
			String callBackUrl = callbackUrlTextField.getText();
			String propertiesStr = propertiesTextField.getText();
			String trackingProfile = (String) trackingProfileComboBox.getSelectedItem();
			if(trackingProfile.contains("-")){
				trackingProfile = "";
			}
			
			Graphic[] features = featureSet.getGraphics();
			for(Graphic feature : features){
				String triggerId = Util.parseAttributes(triggerIdStr, feature);
				String notificationData = Util.parseAttributes(notificationDataStr, feature);
				String properties = Util.parseAttributes(propertiesStr, feature);
				Geometry geometry = feature.getGeometry();
				Geometry projected = GeometryEngine.project(geometry, featureSet.getSpatialReference(), SpatialReference.create(SpatialReference.WKID_WGS84));
				double latitude = 0.0;
				double longitude = 0.0;
				Geometry.Type geometryType = geometry.getType();
				if(geometryType == Geometry.Type.POINT){
					Point midPoint = (Point)projected;
					longitude = midPoint.getX();
					latitude = midPoint.getY();
				}else if(geometryType == Geometry.Type.POLYLINE){
					// for now not supported
					return;
				}else if(geometryType == Geometry.Type.POLYGON){
					// for now not supported
					return;
				}
				TriggerHandler triggerhandler = new TriggerHandler();
				triggerhandler.createTrigger(triggerId, tags, direction, latitude, longitude, radius, notificationText, notificationUrl, null, null, notificationData, callBackUrl, properties, trackingProfile, -1, -1, null, null, -1, -1);					
				JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(map), "Triggers created.");
			}
			
//			Iterator<Object> i = result.iterator();
//			while(i.hasNext()){
//				Object o = i.next();
//				if(o instanceof Feature){
//					Feature feature = (Feature)o;
//					Map<String, Object> attrs = feature.getAttributes();
////					for (Object key : attrs.keySet()) {
////						System.out.println("Key : " + key.toString() + " Value : "+ attrs.get(key));
////					}
//					String triggerId = Util.parseAttributes(triggerIdStr, feature);
//					String notificationData = Util.parseAttributes(notificationDataStr, feature);
//					String properties = Util.parseAttributes(propertiesStr, feature);
//					Geometry geometry = feature.getGeometry();
//					double latitude = 0.0;
//					double longitude = 0.0;
//					Geometry.Type geometryType = geometry.getType();
//					if(geometryType == Geometry.Type.POINT){
//						Point midPoint = (Point)geometry;
//						longitude = midPoint.getX();
//						latitude = midPoint.getY();
//					}else if(geometryType == Geometry.Type.POLYLINE){
//						// for now not supported
//						return;
//					}else if(geometryType == Geometry.Type.POLYGON){
//						// for now not supported
//						return;
//					}
//					TriggerHandler triggerhandler = new TriggerHandler();
//					triggerhandler.createTrigger(triggerId, tags, direction, latitude, longitude, radius, notificationText, notificationUrl, null, null, notificationData, callBackUrl, properties, trackingProfile, -1, -1, null, null, -1, -1);					
//				}
//			}
		}
	}
}
