package de.esri.android.osmtrigger;


import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment that displays the map.
 */
public class MapFragment extends Fragment{
	private static final String TAG = "OSM Geotrigger";
	private MapView mapView;
	private String mapState;
	private final String MAP_STATE = "MapState";

	
	public MapFragment(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "MapFragment, onCreate()");
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.i(TAG, "MapFragment, onCreateView()");
		View view = inflater.inflate(R.layout.map_fragment, container, false);
		mapView = (MapView)view.findViewById(R.id.map);
		mapView.setEsriLogoVisible(true);
		
		ArcGISTiledMapServiceLayer baseMapLayer = new ArcGISTiledMapServiceLayer("http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer");
		mapView.addLayer(baseMapLayer);	
		
	    if (savedInstanceState != null) {
	        mapState = savedInstanceState.getString(MAP_STATE);
	    }	
		if (mapState != null) {
			mapView.restoreState(mapState);
		}
		
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	    if (mapState != null) {
	        outState.putString(MAP_STATE, mapState);
	    }
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mapView.unpause();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mapState = mapView.retainState();
		mapView.pause();
	}		
}
