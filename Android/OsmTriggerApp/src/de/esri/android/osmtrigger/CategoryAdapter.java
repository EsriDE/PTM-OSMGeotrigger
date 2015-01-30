package de.esri.android.osmtrigger;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Shows a list of the possible categories in the search page.
 * The user can activate or deactivate categories 
 */
public class CategoryAdapter extends ArrayAdapter<String> implements OnCheckedChangeListener{
	private Context context;
	private int layoutResourceId;
	private String[] categories;

	public CategoryAdapter(Context context, int resourceId, String[] categories){
		super(context, resourceId, categories);
		this.context = context;
		this.layoutResourceId = resourceId;
		this.categories = categories;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		View row = inflater.inflate(layoutResourceId, parent, false);
		TextView categoryText = (TextView)row.findViewById(R.id.category_text);
		categoryText.setText(categories[position]);
		Switch categorySwitch = (Switch)row.findViewById(R.id.category_switch);
		categorySwitch.setTag(categories[position]);
		categorySwitch.setChecked(isCategoryActive(categories[position]));
		categorySwitch.setOnCheckedChangeListener(this);
		
		return row;
	}
	
	/**
	 * Checks whether a category is active.
	 * @param category The category.
	 * @return A boolean value indicating whether the category is active or not.
	 */
	private boolean isCategoryActive(String category) {
		SharedPreferences preferences = ((Activity) context).getPreferences(Context.MODE_PRIVATE);
		boolean isActive = preferences.getBoolean(category, false);
		return isActive;
	}
	
	/**
	 * Set the category state.
	 * @param category The category.
	 * @param isActive The state of the cagegory (active or inactive).
	 */
	private void setCategoryState(String category, boolean isActive) {
		SharedPreferences preferences = ((Activity) context).getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = preferences.edit();
		prefEditor.putBoolean(category, isActive);
		prefEditor.commit();
	}

	/**
	 * Event handler function called when a category state was changed by its switch. 
	 */
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		String category = (String)buttonView.getTag();
		setCategoryState(category, isChecked);
	}
}
