package de.esri.geotrigger.admin.tools;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.esri.map.JMap;

import de.esri.geotrigger.core.Params;

public class SetAppIdTool {
	private JMap map;
	
	public SetAppIdTool(){
	}
	
	public SetAppIdTool(JMap map){
		this.map = map;
	}
	
	public void setMap(JMap map){
		this.map = map;
	}
	
	public void showAppIdDialog(){
		JFrame applicationFrame = (JFrame) SwingUtilities.getWindowAncestor(map);
		AppIdDialog appIdDialog = new AppIdDialog(applicationFrame);
		appIdDialog.setVisible(true);	
	}
	
	private class AppIdDialog extends JDialog implements ActionListener{
		private JButton okButton = new JButton("OK");
		private JButton cancelButton = new JButton("Cancel");
		private JTextField clientIdTextField;
		private JTextField clientSecretTextField;
		
		public AppIdDialog(Window parent){
			super(parent);
			setTitle("Set App ID");
			setModal(true);
			
			JPanel contentPanel = new JPanel();
			contentPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.anchor = GridBagConstraints.WEST;
			gbc.gridwidth = 2;
			gbc.insets = new Insets(10, 10, 10, 10);
			JLabel loginLabel = new JLabel("Set the IDs of you ArcGIS App");
			contentPanel.add(loginLabel, gbc);
			gbc.gridy = 1;
			gbc.gridwidth = 1;
			gbc.insets = new Insets(5, 10, 3, 5);
			JLabel clientIdLabel = new JLabel("Client ID");
			contentPanel.add(clientIdLabel, gbc);
			gbc.gridx = 1;
			gbc.insets = new Insets(5, 5, 3, 10);
			clientIdTextField = new JTextField(15);
			contentPanel.add(clientIdTextField, gbc);
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.insets = new Insets(3, 10, 15, 5);
			JLabel clientSecretLabel = new JLabel("Client Secret");
			contentPanel.add(clientSecretLabel, gbc);
			gbc.gridx = 1;
			gbc.insets = new Insets(3, 5, 15, 10);
			clientSecretTextField = new JTextField(15);
			contentPanel.add(clientSecretTextField, gbc);		
			add(contentPanel, BorderLayout.CENTER);
						
			JPanel buttonPanel = new JPanel();
			okButton.addActionListener(this);
			buttonPanel.add(okButton);
			cancelButton.addActionListener(this);
			buttonPanel.add(cancelButton);
			add(buttonPanel, BorderLayout.SOUTH);
			
			pack();
			setLocationRelativeTo(parent);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Object src = e.getSource();
			if(src.equals(okButton)){
				setAppId();
				this.dispose();
			}else if(src.equals(cancelButton)){
				this.dispose();
			}
		}
		
		private void setAppId(){
			String clientId = clientIdTextField.getText();
			String clientSecret = clientSecretTextField.getText();
			Params.get().setClientId(clientId);
			Params.get().setClientSecret(clientSecret);
			
			try {
				FileOutputStream out = new FileOutputStream("App.properties");
				Properties properties = new Properties();
				properties.setProperty("clientid", clientId);
				properties.setProperty("clientsecret", clientSecret);
				properties.store(out, null);
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void loadAppId(){
		try {
			File propFile = new File("App.properties");
			if(!propFile.exists()){
				propFile.createNewFile();
			}
			
			FileInputStream in = new FileInputStream(propFile);
			Properties properties = new Properties();
			properties.load(in);
			String clientId = properties.getProperty("clientid");
			if(clientId != null){
				Params.get().setClientId(clientId);
			}
			String clientSecret = properties.getProperty("clientsecret");
			if(clientSecret != null){
				Params.get().setClientSecret(clientSecret);				
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}
