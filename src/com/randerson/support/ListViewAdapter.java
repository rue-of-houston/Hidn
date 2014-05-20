package com.randerson.support;

import libs.ApplicationDefaults;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListViewAdapter extends ArrayAdapter<String>{

	int TEXT_VIEW_ID = 0;
	Context CONTEXT = null;
	int themeListA = 0;
	int themeListB = 0;
	
	public ListViewAdapter(Context context, int resource,
			int textViewResourceId, String[] objects) 
	{
		super(context, resource, textViewResourceId, objects);
		
		CONTEXT = context;
		TEXT_VIEW_ID = textViewResourceId;
		
		ApplicationDefaults defaults = new ApplicationDefaults(CONTEXT);
		
		if (defaults != null)
		{
			themeListA = defaults.getData().getInt("themeListA", ThemeMaster.getColor("#9FA6A4"));
			themeListB = ThemeMaster.getColor("#FFFFFF");
		}
	}
	
	@Override
	public boolean hasStableIds() {
		return true;
	}
	
	@Override
	public String getItem(int position)
	{
		return super.getItem(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LinearLayout layout = (LinearLayout) super.getView(position, convertView, parent);
		
		TextView textView = (TextView) layout.findViewById(TEXT_VIEW_ID);
		
		if (textView != null)
		{
			// check if the position is even or odd and set the background to appropriate drawable
			if (position % 2 == 0)
			{
				textView.setBackgroundColor(themeListB);
				textView.setTextColor(ThemeMaster.getColor("#000000"));
			}
			else
			{
				textView.setBackgroundColor(themeListA);
			}
		}
		
		return layout;
	}

}
