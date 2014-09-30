package de.esri.geotrigger.admin.tools;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.esri.map.ArcGISDynamicMapServiceLayer;
import com.esri.map.ArcGISFeatureLayer;
import com.esri.map.ArcGISTiledMapServiceLayer;
import com.esri.map.JMap;

public class AddLayerTool {
	private static final String TILED_LAYER = "Tiled Layer";
	private static final String DYNAMIC_LAYER = "Dynamic Layer";
	private static final String FEATURE_LAYER = "Feature Layer";
	private JMap map;
	
	public AddLayerTool(){
		
	}
	
	public AddLayerTool(JMap map){
		this.map = map;
	}
	
	public void setMap(JMap map){
		this.map = map;
	}
	
	public void showAddLayerDialog(){
		JFrame applicationFrame = (JFrame) SwingUtilities.getWindowAncestor(map);
		AddLayerDialog addLayerDialog = new AddLayerDialog(applicationFrame);
		addLayerDialog.setVisible(true);	
	}
	
	public void addLayer(String url, String layerType, String user, String password){
		if(layerType == TILED_LAYER){
			ArcGISTiledMapServiceLayer tiledLayer = new ArcGISTiledMapServiceLayer(url);
			map.getLayers().add(tiledLayer);
		}else if(layerType == DYNAMIC_LAYER){
			ArcGISDynamicMapServiceLayer dynamicLayer = new ArcGISDynamicMapServiceLayer(url);
			map.getLayers().add(dynamicLayer);
		}else if(layerType == FEATURE_LAYER){
			ArcGISFeatureLayer featureLayer = new ArcGISFeatureLayer(url);
			map.getLayers().add(featureLayer);
		}
	}
	
	private class AddLayerDialog extends JDialog implements ActionListener{
		private String[] layerTypes = {TILED_LAYER, DYNAMIC_LAYER, FEATURE_LAYER};
		private JButton addLayerButton = new JButton("Add Layer");
		private JButton cancelButton = new JButton("Cancel");
		private JTextField userTextField;
		private JPasswordField passwordTextField;
		private JTextField urlTextField;
		private JComboBox layerTypeComboBox;
		
		public AddLayerDialog(Window parent){
			super(parent);
			setTitle("Add Layer");
			setModal(true);
			
			JPanel layerPanel = new JPanel();
			layerPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.anchor = GridBagConstraints.WEST;
			gbc.gridwidth = 2;
			gbc.insets = new Insets(10, 10, 3, 10);
			JLabel urlLabel = new JLabel("Layer URL:");
			layerPanel.add(urlLabel, gbc);
			gbc.gridy = 1;
			gbc.insets = new Insets(3, 10, 5, 10);
			urlTextField = new JTextField(22);
			layerPanel.add(urlTextField, gbc);
			gbc.gridy = 2;
			gbc.insets = new Insets(5, 10, 3, 10);
			JLabel layerTypeLabel = new JLabel("Layer Type:");
			layerPanel.add(layerTypeLabel, gbc);
			gbc.gridy = 3;
			gbc.insets = new Insets(3, 10, 10, 10);
			layerTypeComboBox = new JComboBox(layerTypes);
			//layerTypeComboBox.addActionListener(this);
			layerPanel.add(layerTypeComboBox, gbc);
			add(layerPanel, BorderLayout.NORTH);
			
			JPanel loginPanel = new JPanel();
			loginPanel.setLayout(new GridBagLayout());
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.insets = new Insets(10, 10, 5, 10);
			JLabel loginLabel = new JLabel("ArcGIS Login (optional):");
			loginPanel.add(loginLabel, gbc);
			gbc.gridwidth = 1;
			gbc.gridy = 1;
			gbc.insets = new Insets(5, 10, 5, 10);
			JLabel userLabel = new JLabel("User");
			loginPanel.add(userLabel, gbc);			
			gbc.gridx = 1;
			userTextField = new JTextField(15);
			loginPanel.add(userTextField, gbc);	
			gbc.gridy = 2;
			gbc.gridx = 0;
			gbc.insets = new Insets(5, 10, 15, 10);
			JLabel passwordLabel = new JLabel("Password");			
			loginPanel.add(passwordLabel, gbc);
			gbc.gridx = 1;
			passwordTextField = new JPasswordField(15);
			loginPanel.add(passwordTextField, gbc);			
			add(loginPanel, BorderLayout.CENTER);
						
			JPanel buttonPanel = new JPanel();
			addLayerButton.addActionListener(this);
			buttonPanel.add(addLayerButton);
			cancelButton.addActionListener(this);
			buttonPanel.add(cancelButton);
			add(buttonPanel, BorderLayout.SOUTH);
			
			pack();
			setLocationRelativeTo(parent);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Object src = e.getSource();
			if(src.equals(addLayerButton)){
				String url = urlTextField.getText();
				String layerType = (String)layerTypeComboBox.getSelectedItem();
				String user = userTextField.getText();
				String password = String.valueOf(passwordTextField.getPassword());
				addLayer(url, layerType, user, password);
				this.dispose();
			}else if(src.equals(cancelButton)){
				this.dispose();
			}
		}
	}
}
