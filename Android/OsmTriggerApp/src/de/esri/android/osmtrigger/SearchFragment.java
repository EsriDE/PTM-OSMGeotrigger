package de.esri.android.osmtrigger;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class SearchFragment extends Fragment implements OnClickListener{
	private ListView categoryList;
	private int count = 0;

	public SearchFragment(){
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.search_fragment, container, false);
		categoryList = (ListView)view.findViewById(R.id.category_list);
		CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(), R.layout.category_list_item, GeotriggerManager.CATEGORIES);
		categoryList.setAdapter(categoryAdapter);
		Button searchButton = (Button)view.findViewById(R.id.search_button);
		searchButton.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View view) {
		search();
	}
	
	private void search(){
		if(count == 0){
			GeotriggerManager geotriggerManager = new GeotriggerManager(getActivity());
			geotriggerManager.setSearchCategories();
			count++;
		}else if(count == 1){
			GeotriggerManager geotriggerManager = new GeotriggerManager(getActivity());
			geotriggerManager.runTrigger("11"); //TODO Webinar Workaround, weil Geotrigger Service nicht funktioniert.
		}
	}
}
