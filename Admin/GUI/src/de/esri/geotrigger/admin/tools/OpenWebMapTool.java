package de.esri.geotrigger.admin.tools;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.esri.core.io.UserCredentials;
import com.esri.core.portal.Portal;
import com.esri.core.portal.WebMap;
import com.esri.map.JMap;

public class OpenWebMapTool {
	private JMap map;
	private String user;
	private String password;
	private String webMapId;
	
	public OpenWebMapTool(){
		
	}
	
	public OpenWebMapTool(JMap map){
		this.map = map;
	}
	
	public void setMap(JMap map){
		this.map = map;
	}
	
	public void showOpenWebMapDialog(){
		JFrame applicationFrame = (JFrame) SwingUtilities.getWindowAncestor(map);
		WebMapDialog webMapDialog = new WebMapDialog(applicationFrame);
		webMapDialog.setVisible(true);		
	}
	
	public void openWebMap(String user, String password, String portalUrl, String webMapId){
		UserCredentials credentials = new UserCredentials();
		credentials.setUserAccount(user, password);
		Portal portal = new Portal(portalUrl, credentials);		
		WebMap webMap = null;
		try{
			webMap = WebMap.newInstance(webMapId, portal);
			map.loadWebMap(webMap);
		}catch(Exception e){
			
		}
	}
	
	private class WebMapDialog extends JDialog implements ActionListener{
		private JButton openButton = new JButton("Open");
		private JButton cancelButton = new JButton("Cancel");
		private JTextField userTextField;
		private JPasswordField passwordTextField;
		private JTextField portalUrlTextField;
		private JTextField webMapTextField;
		
		public WebMapDialog(Window parent){
			super(parent);
			setTitle("Open Web Map");
			setModal(true);
			
			JPanel webMapPanel = new JPanel();
			webMapPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.anchor = GridBagConstraints.WEST;
			gbc.gridwidth = 2;
			gbc.insets = new Insets(10, 10, 3, 10);					
			JLabel portalUrlLabel = new JLabel("Portal URL:");
			webMapPanel.add(portalUrlLabel, gbc);
			gbc.gridy = 1;
			gbc.insets = new Insets(3, 10, 5, 10);
			portalUrlTextField = new JTextField(22);
			webMapPanel.add(portalUrlTextField, gbc);
			gbc.gridy = 2;
			gbc.insets = new Insets(5, 10, 3, 10);
			JLabel webMapLabel = new JLabel("Web Map ID:");
			webMapPanel.add(webMapLabel, gbc);
			gbc.gridy = 3;
			gbc.insets = new Insets(3, 10, 10, 10);
			webMapTextField = new JTextField(22);
			webMapPanel.add(webMapTextField, gbc);
			add(webMapPanel, BorderLayout.NORTH);
			
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
			openButton.addActionListener(this);
			buttonPanel.add(openButton);
			cancelButton.addActionListener(this);
			buttonPanel.add(cancelButton);
			add(buttonPanel, BorderLayout.SOUTH);
			pack();
			setLocationRelativeTo(parent);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Object src = e.getSource();
			if(src.equals(openButton)){
				String user = userTextField.getText();
				String password = String.valueOf(passwordTextField.getPassword());
				String portalUrl = portalUrlTextField.getText();
				String webMapId = webMapTextField.getText();
				openWebMap(user, password, portalUrl, webMapId);
				this.dispose();
			}else if(src.equals(cancelButton)){
				this.dispose();
			}
		}
	}
}
