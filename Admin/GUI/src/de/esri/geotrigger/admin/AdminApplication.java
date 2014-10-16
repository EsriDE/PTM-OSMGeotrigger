package de.esri.geotrigger.admin;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.esri.runtime.ArcGISRuntime;
import com.esri.core.io.UserCredentials;
import com.esri.core.portal.Portal;
import com.esri.core.portal.WebMap;
import com.esri.map.JMap;
import com.esri.map.MapOptions;
import com.esri.map.MapOptions.MapType;

import de.esri.geotrigger.admin.tools.AddLayerTool;
import de.esri.geotrigger.admin.tools.CreateMultipleTriggersTool;
import de.esri.geotrigger.admin.tools.CreateTriggerTool;
import de.esri.geotrigger.admin.tools.NewMapTool;
import de.esri.geotrigger.admin.tools.OpenWebMapTool;
import de.esri.geotrigger.admin.tools.SetAppIdTool;

/**
 * Web Map:
 * http://esri-de-dev.maps.arcgis.com/home/item.html?id=26d316dfb7034da1991cc862a51d04e2
 * 
 * http://esri-de-dev.maps.arcgis.com/home/item.html?id=bf5dbd7d583543f795fde3e6731d48c6
 *
 * - Logging
 * - Lokalisierung
 * - Lizensierung
 */
public class AdminApplication implements ActionListener{
//	private String portalUrl = "http://esri-de-dev.maps.arcgis.com";
//	private String webMapId = "bf5dbd7d583543f795fde3e6731d48c6";
	private static JFrame window;
	private JMap map;
	private JMenuItem appIdMenuItem;
	private JMenuItem exitMenuItem;
	private JMenuItem newMapMenuItem;
	private JMenuItem openWebMapMenuItem;
	private JMenuItem addLayerMenuItem;
	private JMenuItem createTriggerMenuItem;
	private JMenuItem createMultipleTriggersMenuItem;
	private SetAppIdTool appIdTool;
	private NewMapTool newMapTool;
	private OpenWebMapTool openWebMapTool;
	private AddLayerTool addLayerTool;
	private CreateTriggerTool createTriggerTool;
	private CreateMultipleTriggersTool createMultipleTriggersTool;

	public AdminApplication() {
		window = new JFrame();
		window.setSize(800, 600);
		window.setTitle("Geotrigger Administrator");
		window.setLocationRelativeTo(null); // center on screen
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.getContentPane().setLayout(new BorderLayout(0, 0));

		// dispose map just before application window is closed.
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				super.windowClosing(windowEvent);
				map.dispose();
			}
		});
		// menu bar
		JMenuBar menuBar = new JMenuBar();
		window.setJMenuBar(menuBar);
		// app menu
		JMenu appMenu = new JMenu("App");		
		menuBar.add(appMenu);
		appIdMenuItem = new JMenuItem("Set App ID...");
		appIdMenuItem.addActionListener(this);	
		appMenu.add(appIdMenuItem);
		exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(this);
		appMenu.add(exitMenuItem);
		// map menu
		JMenu mapMenu = new JMenu("Map");		
		menuBar.add(mapMenu);		
		newMapMenuItem = new JMenuItem("New Map");
		newMapMenuItem.addActionListener(this);
		mapMenu.add(newMapMenuItem);
		openWebMapMenuItem = new JMenuItem("Open Web Map...");
		openWebMapMenuItem.addActionListener(this);
		mapMenu.add(openWebMapMenuItem);
		addLayerMenuItem = new JMenuItem("Add Layer...");
		addLayerMenuItem.addActionListener(this);
		mapMenu.add(addLayerMenuItem);
		// trigger menu
		JMenu triggerMenu = new JMenu("Trigger");
		menuBar.add(triggerMenu);
		createTriggerMenuItem = new JMenuItem("Create Trigger...");
		createTriggerMenuItem.addActionListener(this);
		triggerMenu.add(createTriggerMenuItem);
		createMultipleTriggersMenuItem = new JMenuItem("Create multiple Triggers...");
		createMultipleTriggersMenuItem.addActionListener(this);
		triggerMenu.add(createMultipleTriggersMenuItem);
		

	    // Before this application is deployed please register the application on http://developers.esri.com 
	    // and set the Client ID in the application as shown below.  This will license your application 
	    // to use Basic level functionality.
	    // 
	    // If you require to license your application for Standard level functionality, please refer to the 
	    // documentation on http://developers.esri.com
	    //
	    // ArcGISRuntime.setClientID("your client id");
	
	    //map options allow for a common base map to be chosen
	    //MapOptions mapOptions = new MapOptions(MapType.TOPO);
    
//		UserCredentials credentials = new UserCredentials();
//		credentials.setUserAccount("rsu4devprog", "devprog42195");
//		Portal portal = new Portal(portalUrl, credentials);
//		
//		WebMap webMap = null;
//		try{
//			webMap = WebMap.newInstance(webMapId, portal);
//		}catch(Exception e){
//			
//		}

		//create a map using the map options
		map = new JMap();
//		map.loadWebMap(webMap);
		window.getContentPane().add(map, BorderLayout.CENTER);


		// The code below shows how to add a tiled layer if you don't use MapOptions
		//ArcGISTiledMapServiceLayer tiledLayer = new ArcGISTiledMapServiceLayer(
		// "http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer");
		// map.getLayers().add(tiledLayer);
		
		// tools
		appIdTool = new SetAppIdTool(map);
		appIdTool.loadAppId();
		newMapTool = new NewMapTool(map);
		openWebMapTool = new OpenWebMapTool(map);
		addLayerTool = new AddLayerTool(map);
		createTriggerTool = new CreateTriggerTool(map);
//		JToolBar triggerToolBar = createTriggerTool.getTriggerToolBar();
//		window.getContentPane().add(triggerToolBar, BorderLayout.PAGE_START);
		createMultipleTriggersTool = new CreateMultipleTriggersTool(map);
  }

  /**
   * @param args
   */
	public static void main(String[] args) {
//		System.setProperty("http.proxyHost", "127.0.0.1");
//		System.setProperty("http.proxyPort", "8888");
		
		EventQueue.invokeLater(new Runnable() {

		@Override
		public void run() {
			try {
				AdminApplication application = new AdminApplication();
				application.window.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		});
	}

  	@Override
  	public void actionPerformed(ActionEvent e) {
  		Object src = e.getSource();
  		if(src.equals(appIdMenuItem)){
  			appIdTool.showAppIdDialog();
  		}else if(src.equals(exitMenuItem)){
  			System.exit(0);
  		}else if(src.equals(newMapMenuItem)){
  			newMapTool.createNewMap();
  		}else if(src.equals(openWebMapMenuItem)){
  			openWebMapTool.showOpenWebMapDialog();
  		}else if(src.equals(addLayerMenuItem)){
  			addLayerTool.showAddLayerDialog();
  		}else if(src.equals(createTriggerMenuItem)){
  			//createTriggerTool.start();
  			//createTriggerTool.getTriggerToolBar().setVisible(true);
  		}else if(src.equals(createMultipleTriggersMenuItem)){
  			createMultipleTriggersTool.showCreateTriggersDialog();
  		}
  	}
  	
  	public static JFrame getFrame(){
  		return window;
  	}
}
