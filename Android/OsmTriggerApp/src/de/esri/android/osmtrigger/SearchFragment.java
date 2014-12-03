package de.esri.android.osmtrigger;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class SearchFragment extends Fragment{
	private ListView categoryList;

	public SearchFragment(){
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.search_fragment, container, false);
		categoryList = (ListView)view.findViewById(R.id.category_list);
		CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(), R.layout.category_list_item, GeotriggerManager.CATEGORIES);
		categoryList.setAdapter(categoryAdapter);
		Button searchButton = (Button)view.findViewById(R.id.search_button);
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				search();
			}
		});
		Button runTriggerButton = (Button)view.findViewById(R.id.run_trigger_button);
		if(runTriggerButton != null){
			runTriggerButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					runTrigger();
				}
			});
		}
		Button createNotificationButton = (Button)view.findViewById(R.id.create_notification_button);
		if(createNotificationButton != null){
			createNotificationButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					createNotification();
				}
			});			
		}
		return view;
	}
	
	private void search(){
		GeotriggerManager geotriggerManager = new GeotriggerManager(getActivity());
		geotriggerManager.setSearchCategories();
	}
	
	private void runTrigger(){
		GeotriggerManager geotriggerManager = new GeotriggerManager(getActivity());
		// run a trigger by its trigger id (insert proper id).
		geotriggerManager.runTrigger("296231135");
	}
	
	private void createNotification(){
		GeotriggerManager geotriggerManager = new GeotriggerManager(getActivity());
		geotriggerManager.createNotification();
	}
}
