package de.esri.android.osmtrigger;

public class App {
	private static App instance = null;
	private GeotriggerManager geotriggerManager;
	
	private App(){
	}

	public static App getInstance(){
		if(instance == null){
			instance = new App();
		}
		return instance;
	}

	public GeotriggerManager getGeotriggerManager() {
		return geotriggerManager;
	}

	public void setGeotriggerManager(GeotriggerManager geotriggerManager) {
		this.geotriggerManager = geotriggerManager;
	}
	
}
