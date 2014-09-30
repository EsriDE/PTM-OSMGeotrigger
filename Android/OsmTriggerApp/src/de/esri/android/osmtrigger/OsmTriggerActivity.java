package de.esri.android.osmtrigger;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * http://esri-de-dev.maps.arcgis.com/home/item.html?id=26d316dfb7034da1991cc862a51d04e2
 *
 */
public class OsmTriggerActivity extends Activity {
	private static final String TAG = "OSM Geotrigger";
//	private static final String AGO_CLIENT_ID = "Ied6w56BNs5jSrQ0";
//	private static final String GCM_SENDER_ID = "509218283675";
//	private static final String[] TAGS = new String[] {"osm", "geotrigger"};
	private static final String TAG_MAP_FRAGMENT = "MapFragment";
	private static final String TAG_SEARCH_FRAGMENT = "SearchFragment";
	private static final String TAG_SETTINGS_FRAGMENT = "SettingsFragment";
	private static final String TAG_INFO_FRAGMENT = "InfoFragment";
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
        
        //geotriggerBroadcastReceiver = new GeotriggerBroadcastReceiver();
        
        if (savedInstanceState == null) {
            selectItem(0);
        }
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
}