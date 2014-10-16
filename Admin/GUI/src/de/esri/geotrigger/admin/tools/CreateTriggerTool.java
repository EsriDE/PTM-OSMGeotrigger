package de.esri.geotrigger.admin.tools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.SpatialReference;
import com.esri.map.JMap;
import com.esri.map.MapOverlay;

import de.esri.geotrigger.core.TriggerBuilder;
import de.esri.geotrigger.core.TriggerHandler;

public class CreateTriggerTool extends MouseAdapter{	
	private static final String TRIGGER_TYPE_CIRCLE = "circle";
	private static final String TRIGGER_TYPE_POLYGON = "polygon";
	private static Logger log = LogManager.getLogger(CreateTriggerTool.class.getName());
	private String triggerType;
	private JMap map;	
	private TriggerOverlay triggerOverlay;
	private com.esri.core.geometry.Polygon triggerPolygon;
	private com.esri.core.geometry.Point triggerCenterPoint;
	private double triggerRadius;
	private JToolBar triggerToolBar = new JToolBar("Trigger");

	public CreateTriggerTool(){
		initToolBar();
	}
	
	public CreateTriggerTool(JMap map){
		this.map = map;
		initToolBar();
		addTriggerOverlay();
	}
	
	public void setMap(JMap map){
		this.map = map;
		addTriggerOverlay();
	}
	
	public JToolBar getTriggerToolBar(){
		return triggerToolBar;
	}
	
	private void initToolBar(){
		JButton b = new JButton("Add");
		triggerToolBar.add(b);
		triggerToolBar.setVisible(false);
	}
	
	private void addTriggerOverlay(){
		if(triggerOverlay == null){
			triggerOverlay = new TriggerOverlay();
			triggerOverlay.setActive(false);
			map.addMapOverlay(triggerOverlay);	
		}
	}
	
	public void start(){
		if(triggerOverlay != null){
			triggerOverlay.setActive(true);
		}
	}
	
	public void stop(){
		if(triggerOverlay != null){
			triggerOverlay.setActive(false);
		}
	}
	
	public void showCreateTriggersDialog(){
		JFrame applicationFrame = (JFrame) SwingUtilities.getWindowAncestor(map);
		CreateTriggersDialog createTriggersDialog = new CreateTriggersDialog(applicationFrame);
		createTriggersDialog.setVisible(true);	
	}

	private class TriggerOverlay extends MapOverlay{
		private List<Point> points = new ArrayList<Point>();
		private Point centerPoint;
		private Point lastPoint;
		private boolean isCircle;
		
		@Override
		public void onMouseClicked(MouseEvent event) {
			//System.out.println("Mouse clicked.");
			if(!isCircle){
				if(event.getClickCount() == 2 && !event.isConsumed()){
					event.consume();
					// Polygon finished
					triggerType = TRIGGER_TYPE_POLYGON;
					triggerPolygon = new com.esri.core.geometry.Polygon();
					triggerPolygon.startPath(map.toMapPoint(points.get(0).x, points.get(0).y));
					for(int i = 1; i < points.size(); i++){
						triggerPolygon.lineTo(map.toMapPoint(points.get(i).x, points.get(i).y));
					}
					showCreateTriggersDialog();
					reset();
				}else{
					repaint();
				}
			}
		}

		@Override
		public void onMousePressed(MouseEvent event) {
			//System.out.println("Mouse pressed.");
			Point point = new Point(event.getX(), event.getY()); 
			if(centerPoint == null){
				repaint();
				centerPoint = point;
			}
			points.add(point);
		}

		@Override
		public void onMouseReleased(MouseEvent event) {
			//System.out.println("Mouse released.");
			if(isCircle){
				triggerType = TRIGGER_TYPE_CIRCLE;
				triggerCenterPoint = map.toMapPoint(centerPoint.x, centerPoint.y); 
				com.esri.core.geometry.Point triggerBorderPoint = map.toMapPoint(lastPoint.x, lastPoint.y); 
//				System.out.println("SRS: "+map.getSpatialReference().getID());
//				System.out.println("SRS: "+map.getSpatialReference().getText());
//				System.out.println("Center: x: "+triggerCenterPoint.getX()+",y: "+triggerCenterPoint.getY());
//				System.out.println("Border: x: "+triggerBorderPoint.getX()+",y: "+triggerBorderPoint.getY());
				triggerRadius = GeometryEngine.distance(triggerCenterPoint, triggerBorderPoint, map.getSpatialReference());
//				System.out.println("Radius: "+triggerRadius);							
				showCreateTriggersDialog();
				reset();
			}
		}

		@Override
		public void onMouseDragged(MouseEvent event) {
			//System.out.println("Mouse dragged.");
			if(points.size() == 1){
				isCircle = true;
				lastPoint = new Point(event.getX(), event.getY()); 
				repaint();
			}			
		}
		
		@Override
		public void onPaint(Graphics graphics) {
			//System.out.println("onPaint.");
			if(isCircle){
				drawCircle(graphics);
			}else{
				drawPolygon(graphics);
			}
			super.onPaint(graphics);
		}
		
		private void drawCircle(Graphics graphics){
			graphics.setColor(Color.RED);
			int x0 = centerPoint.x;
			int y0 = centerPoint.y;
			int x1 = lastPoint.x;
			int y1 = lastPoint.y;
			int dx = x1 - x0;
			int dy = y1 - y0;
			int r = (int)Math.round(Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)));
			graphics.drawOval(x0 - r, y0 - r, 2 * r, 2 * r);
		}
		
		private void drawPolygon(Graphics graphics){
			graphics.setColor(Color.RED);
			Polygon polygon = new Polygon();
			for(int i = 0; i < points.size(); i++){				
				Point point = points.get(i);
				polygon.addPoint(point.x, point.y);
			}
			graphics.drawPolygon(polygon);
		}
		
		private void reset(){
			centerPoint = null;
			lastPoint = null;
			points.clear();
			isCircle = false;
		}
		
		public void clear(){
			reset();
			repaint();
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
			setTitle("Create Trigger");
			setModal(true);		
			
			JPanel triggerPanel = new JPanel();
			triggerPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.anchor = GridBagConstraints.WEST;
			gbc.gridwidth = 2;
			gbc.insets = new Insets(8, 6, 8, 6);
			JLabel triggerLabel = new JLabel("Trigger Definition");
			triggerPanel.add(triggerLabel, gbc);
			gbc.gridwidth = 1;
			gbc.gridy = 1;
			gbc.insets = new Insets(5, 6, 5, 6);
			JLabel directionLabel = new JLabel("Direction");
			triggerPanel.add(directionLabel, gbc);
			gbc.gridx = 1;
			String[] directions = {TriggerBuilder.DIRECTION_ENTER,TriggerBuilder.DIRECTION_LEAVE};
			directionComboBox = new JComboBox(directions);
			triggerPanel.add(directionComboBox, gbc);
			gbc.gridx = 0;
			gbc.gridy = 2;
			JLabel triggerIdLabel = new JLabel("Trigger ID");
			triggerPanel.add(triggerIdLabel, gbc);
			gbc.gridx = 1;
			triggerIdTextField = new JTextField(10);
			triggerPanel.add(triggerIdTextField, gbc);			
			gbc.gridx = 0;
			gbc.gridy = 3;
			JLabel tagsLabel = new JLabel("Tags");
			triggerPanel.add(tagsLabel, gbc);
			gbc.gridx = 1;
			tagsTextField = new JTextField(19);
			triggerPanel.add(tagsTextField, gbc);
			// notification
			gbc.gridwidth = 2;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 0;
			gbc.gridy = 4;			
			JPanel notificationPanel = new JPanel();
			notificationPanel.setLayout(new GridBagLayout());
			GridBagConstraints ngbc = new GridBagConstraints();
			ngbc.anchor = GridBagConstraints.WEST;
			ngbc.insets = new Insets(10, 6, 3, 6);
			JLabel notificationTextLabel = new JLabel("Text");
			notificationPanel.add(notificationTextLabel, ngbc);
			ngbc.gridx = 1;			
			notificationTextField = new JTextField(21);
			notificationPanel.add(notificationTextField, ngbc);
			ngbc.gridx = 0;
			ngbc.gridy = 1;
			ngbc.insets = new Insets(3, 6, 3, 6);
			JLabel notificationUrlTextLabel = new JLabel("Url");
			notificationPanel.add(notificationUrlTextLabel, ngbc);
			ngbc.gridx = 1;
			notificationUrlTextField = new JTextField(21);
			notificationPanel.add(notificationUrlTextField, ngbc);
			ngbc.gridx = 0;
			ngbc.gridy = 2;
			ngbc.insets = new Insets(3, 6, 10, 6);
			JLabel notificationDataTextLabel = new JLabel("Data");
			notificationPanel.add(notificationDataTextLabel, ngbc);
			ngbc.gridx = 1;
			notificationDataTextField = new JTextField(21);
			notificationPanel.add(notificationDataTextField, ngbc);	
			notificationPanel.setBorder(BorderFactory.createTitledBorder("Notification"));
			triggerPanel.add(notificationPanel, gbc);					
			// end notification
			gbc.fill = GridBagConstraints.NONE;
			gbc.gridwidth = 1;
			gbc.gridx = 0;
			gbc.gridy = 5;	
			JLabel callbackUrlLabel = new JLabel("Callback URL");
			triggerPanel.add(callbackUrlLabel, gbc);
			gbc.gridx = 1;
			callbackUrlTextField = new JTextField(19);
			triggerPanel.add(callbackUrlTextField, gbc);
			gbc.gridx = 0;
			gbc.gridy = 6;	
			JLabel propertiesLabel = new JLabel("Properties");
			triggerPanel.add(propertiesLabel, gbc);
			gbc.gridx = 1;
			propertiesTextField = new JTextField(19);
			triggerPanel.add(propertiesTextField, gbc);
			gbc.gridx = 0;
			gbc.gridy = 7;	
			gbc.insets = new Insets(5, 6, 10, 6);
			JLabel trackingProfileLabel = new JLabel("Tracking Profile");
			triggerPanel.add(trackingProfileLabel, gbc);
			gbc.gridx = 1;
			String[] trackingProfiles = {" - ", "adaptive", "fine", "rough", "off"};
			trackingProfileComboBox = new JComboBox(trackingProfiles);
			triggerPanel.add(trackingProfileComboBox, gbc);
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
				createTrigger();
				this.dispose();
			}else if(src.equals(cancelButton)){
				this.dispose();
			}
			triggerOverlay.clear();
		}
		
		private void createTrigger(){
			String triggerId = triggerIdTextField.getText();
			String direction = (String) directionComboBox.getSelectedItem();
			String tagStr = tagsTextField.getText();
			String[] tags = tagStr.split(",");
			String notificationText = notificationTextField.getText();
			String notificationUrl = notificationUrlTextField.getText();
			String notificationData = notificationDataTextField.getText();
			String callBackUrl = callbackUrlTextField.getText();
			String properties = propertiesTextField.getText();
			String trackingProfile = (String) trackingProfileComboBox.getSelectedItem();
			if(trackingProfile.contains("-")){
				trackingProfile = "";
			}
			
			TriggerHandler triggerhandler = new TriggerHandler();
			if(triggerType == TRIGGER_TYPE_CIRCLE){
				com.esri.core.geometry.Point wgsCenterPoint = (com.esri.core.geometry.Point)GeometryEngine.project(triggerCenterPoint, map.getSpatialReference(), SpatialReference.create(SpatialReference.WKID_WGS84));
				double latitude = wgsCenterPoint.getY();
				double longitude = wgsCenterPoint.getX();
				triggerhandler.createTrigger(triggerId, tags, direction, latitude, longitude, triggerRadius, notificationText, notificationUrl, null, null, notificationData, callBackUrl, properties, trackingProfile, -1, -1, null, null, -1, -1);									
			}else if(triggerType == TRIGGER_TYPE_POLYGON){
				Geometry wgsPolygon = GeometryEngine.project(triggerPolygon, map.getSpatialReference(), SpatialReference.create(SpatialReference.WKID_WGS84));
				String geoJson = GeometryEngine.geometryToJson(SpatialReference.create(SpatialReference.WKID_WGS84), wgsPolygon);
				System.out.println("geoJson: "+geoJson);
				triggerhandler.createTrigger(triggerId, tags, direction, geoJson, notificationText, notificationUrl, null, null, notificationData, callBackUrl, properties, trackingProfile, -1, -1, null, null, -1, -1);
			}
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(map), "Trigger created.");
		}
	}
}
