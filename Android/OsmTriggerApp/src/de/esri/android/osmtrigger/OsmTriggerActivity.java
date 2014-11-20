package de.esri.android.osmtrigger;

import com.esri.android.geotrigger.GeotriggerBroadcastReceiver;
import com.esri.android.geotrigger.GeotriggerService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Web Map: http://esri-de-dev.maps.arcgis.com/home/item.html?id=26d316dfb7034da1991cc862a51d04e2
 * Feature Service: http://services2.arcgis.com/tISIjAqoejGPFbAF/arcgis/rest/services/FIXME_Points/FeatureServer/0
 */
public class OsmTriggerActivity extends Activity implements GeotriggerBroadcastReceiver.PushMessageListener {
	private static final String TAG = "OSM Geotrigger";
	private static final String AGO_CLIENT_ID = "Ied6w56BNs5jSrQ0";
	private static final String GCM_SENDER_ID = "509218283675";
	private static final String METHOD_NOTIFICATION = "Notification";
	private static final String TAG_MAP_FRAGMENT = "MapFragment";
	private static final String TAG_SEARCH_FRAGMENT = "SearchFragment";
	private static final String TAG_SETTINGS_FRAGMENT = "SettingsFragment";
	private static final String TAG_INFO_FRAGMENT = "InfoFragment";
	private GeotriggerBroadcastReceiver geotriggerBroadcastReceiver;
	private String[] pageNames;
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;
    private ListView drawerList;
    private CharSequence pageTitle;
    private MapFragment mapFragment;
    private SearchFragment searchFragment;
    private SettingsFragment settingsFragment;
    private InfoFragment infoFragment;
    private Fragment currentFragment; 
    private int currentPosition = -1;	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.i(TAG, "OSM Activity, onCreate()");
        
        pageTitle = getTitle();
        pageNames = getResources().getStringArray(R.array.page_names);
        
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, pageNames));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
		drawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(pageTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(getTitle());
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        
        geotriggerBroadcastReceiver = new GeotriggerBroadcastReceiver();
        
        if (savedInstanceState == null) {
            selectItem(0);
        }
    }
    
    @Override
    public void onStart() {
        super.onStart();
    	GeotriggerService.setLoggingLevel(Log.DEBUG);
    	GeotriggerService.setPushNotificationHandlingEnabled(this, false);
    	GeotriggerService.init(this, AGO_CLIENT_ID, GCM_SENDER_ID, GeotriggerService.TRACKING_PROFILE_ADAPTIVE);      
    }    

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(geotriggerBroadcastReceiver);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(geotriggerBroadcastReceiver, GeotriggerBroadcastReceiver.getDefaultIntentFilter());
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Log.i(TAG, "OSM Activity, onOptionsItemSelected()");
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
//        case R.id.action_websearch:
//            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }    

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
    
    private void selectItem(int position) {
    	Log.i(TAG, "OSM Activity, selectItem()");
    	if(position != currentPosition){
        	FragmentManager fragmentManager = getFragmentManager();
        	if(currentFragment != null){
        		fragmentManager.beginTransaction().detach(currentFragment).commit();
        	}
        	
        	switch(position){
        	case 0: // map
        		mapFragment = (MapFragment) fragmentManager.findFragmentByTag(TAG_MAP_FRAGMENT);
        		if(mapFragment == null){
        			mapFragment = new MapFragment();
        			fragmentManager.beginTransaction().add(R.id.content_frame, mapFragment, TAG_MAP_FRAGMENT).commit();
        		}else if(mapFragment.isDetached()){
        			fragmentManager.beginTransaction().attach(mapFragment).commit();
        		}
        		currentFragment = mapFragment;
        		currentPosition = 0;
        		break;
        	case 1: // search
        		searchFragment = (SearchFragment) fragmentManager.findFragmentByTag(TAG_SEARCH_FRAGMENT);
        		if(searchFragment == null){
        			searchFragment = new SearchFragment();
        			fragmentManager.beginTransaction().add(R.id.content_frame, searchFragment, TAG_SEARCH_FRAGMENT).commit();
        		}else if(searchFragment.isDetached()){
        			fragmentManager.beginTransaction().attach(searchFragment).commit();
        		}
        		currentFragment = searchFragment;
        		currentPosition = 1;
        		break;
        	case 2: // settings
        		settingsFragment = (SettingsFragment)fragmentManager.findFragmentByTag(TAG_SETTINGS_FRAGMENT);
        		if(settingsFragment == null){
        			settingsFragment = new SettingsFragment();
        			fragmentManager.beginTransaction().add(R.id.content_frame, settingsFragment, TAG_SETTINGS_FRAGMENT).commit();
        		}else if(settingsFragment.isDetached()){
        			fragmentManager.beginTransaction().attach(settingsFragment).commit();
        		}
        		currentFragment = settingsFragment;
        		currentPosition = 2;
        		break;
        	case 3: // info
        		infoFragment = (InfoFragment) fragmentManager.findFragmentByTag(TAG_INFO_FRAGMENT);
        		if(infoFragment == null){
        			infoFragment = new InfoFragment();
        			fragmentManager.beginTransaction().add(R.id.content_frame, infoFragment, TAG_INFO_FRAGMENT).commit();
        		}else if(infoFragment.isDetached()){
        			fragmentManager.beginTransaction().attach(infoFragment).commit();
        		}
        		currentFragment = infoFragment;
        		currentPosition = 3;
        		break;
        	}
        	
        	drawerList.setItemChecked(position, true);
        	setTitle(pageNames[position]);
    	}
    	drawerLayout.closeDrawer(drawerList);
    } 
    
    @Override
    public void setTitle(CharSequence pageTitle) {
        this.pageTitle = pageTitle;
        getActionBar().setTitle(pageTitle);
    }  
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

	@Override
	public void onPushMessage(Bundle data) {
		//Toast.makeText(this, "Message received from geotrigger service!", Toast.LENGTH_LONG).show();
		selectItem(0);
		
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		boolean showNotifications = settings.getBoolean("send_notifications", false);
		if(showNotifications){
			String notificationText = data.getString("text");
			
			Intent intent = new Intent(this, OsmTriggerActivity.class);
			intent.putExtra("Method", METHOD_NOTIFICATION);
			intent.putExtra("Data", data);
			PendingIntent pendingIntent = PendingIntent.getActivity(this,  0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			
			NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("OSM Trigger")
				.setContentText(notificationText)
				.setContentIntent(pendingIntent);
			
			Notification notification = notificationBuilder.build();
			
			NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			notificationManager.notify(1, notification);
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		String method = intent.getStringExtra("Method");
		if(method != null && method.equals(METHOD_NOTIFICATION)){
			selectItem(0);
			Bundle data = intent.getBundleExtra("Data");
			showNotificationDetails(data);
		}
	}
	
	// ### Delete
	public void showObject(){
		String text = "FIXME: Türkenstraße";
		String url = "http://www.openstreetmap.org/node/296231135";
		String triggerdata = "{ \"layer\": \"FIXME_Points\", \"tags\": { \"OBJECTID\": \"11\", \"bus_routes\": \"154; 153\", \"fixme\": \"genaue Position\", \"highway\": \"bus_stop\", \"name\": \"TÃ¼rkenstraÃŸe\", \"operator\": \"MVG\", \"public_transport\": \"stop_position\", \"wheelchair\": \"yes\" } }";
		Bundle data = new Bundle();
		data.putString("text", text);
		data.putString("url", url);
		data.putString("data", triggerdata);
		onPushMessage(data);
	}
	
	private void showNotificationDetails(Bundle data){
		// show the details of the geotrigger like text and data
		String text = data.getString("text");
		Log.d(TAG, "Text: "+text);
		String url = data.getString("url");
		Log.d(TAG, "URL: "+url);
		String triggerData = data.getString("data");
		Log.d(TAG, "Data: "+triggerData);
		mapFragment.showFeature(text, url, triggerData);
//		String msg = text + "\n" + triggerData;
//		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
}