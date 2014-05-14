package com.randerson.support;

import android.content.Context;
import android.widget.ArrayAdapter;

public class ListViewAdapter extends ArrayAdapter<String>{

	public ListViewAdapter(Context context, int resource,
			int textViewResourceId, String[] objects) 
	{
		super(context, resource, textViewResourceId, objects);
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

}
