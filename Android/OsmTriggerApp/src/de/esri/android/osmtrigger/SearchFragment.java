package de.esri.android.osmtrigger;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class SearchFragment extends Fragment implements OnClickListener{
	private ListView categoryList;
	private String[] categories = new String[]{"Fix-me","Blitzer","Tankstellen","Briefkästen"};

	public SearchFragment(){
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.search_fragment, container, false);
		categoryList = (ListView)view.findViewById(R.id.category_list);
		CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(), R.layout.category_list_item, categories);
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
		Toast.makeText(getActivity(), "Starte Suche", Toast.LENGTH_LONG).show();
		
	}
}
