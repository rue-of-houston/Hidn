package com.randerson.support;
import libs.UniArray;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PhotoAdapter extends ArrayAdapter<String> {


	Context CONTEXT;
	String[] LIST_DATA = null;
	String[] LIST_DATA_PATHS = null;
	int LAYOUT_ID = 0;
	int TEXT_VIEW_ID = 0;
	DataManager DATA_MANAGER = null;
	
	public PhotoAdapter(Context context, int resource, int textViewResourceId,
			String[] objects) 
	{
		super(context, resource, textViewResourceId, objects);
		
		CONTEXT = context;
		LIST_DATA = objects;
		LAYOUT_ID = resource;
		TEXT_VIEW_ID = textViewResourceId;
		
		DATA_MANAGER = new DataManager(context);
	}

	@Override
	public int getCount() {
		return LIST_DATA.length;
	}

	@Override
	public String getItem(int position)
	{	
		String title = null;
		
		if (LIST_DATA != null)
		{
			title = LIST_DATA[position];
		}
		
		return title;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LinearLayout layout = (LinearLayout) super.getView(position, convertView, parent);
		
		TextView textView = null;
		ImageView imageView = null;
		
		// get the hidn photo paths
		LIST_DATA_PATHS = getPaths();
		
			textView = (TextView) layout.findViewById(TEXT_VIEW_ID);
			imageView = (ImageView) layout.findViewById(com.randerson.hidn.R.id.photoListIcon);
			
			if (textView != null)
			{
				if (LIST_DATA != null)
				{
					// set the text view text
					textView.setText(LIST_DATA[position]);	
				}
				
				if (LIST_DATA_PATHS != null)
				{
					// get the bitmap sample size
					int sampleSize = HidNExplorer.calulateBitmapSampleSize(LIST_DATA_PATHS[position], 30, 30);
					
					// create and set the bitmap factory options
					BitmapFactory.Options options = new BitmapFactory.Options();
					
					if (options != null)
					{
						options.inPurgeable = true;
						options.inSampleSize = sampleSize;
					}
					
					// create the bitmap
					Drawable image = null;
					Bitmap baseImage = BitmapFactory.decodeFile(LIST_DATA_PATHS[position], options);
					
					if (baseImage != null)
					{
						image = new BitmapDrawable(CONTEXT.getResources(), baseImage);
						
						if (imageView != null)
						{	
							// create and set the drawable
							imageView.setBackground(image);
						}
					}
				}
			}
		
		return layout;
	}
	
	@Override
	public boolean hasStableIds()
	{
		return true;
	}
	
	private String[] getPaths()
	{
		String[] photoPaths = null;
		
		if (DATA_MANAGER != null)
		{
			// get the photos uniarray
			UniArray photos = (UniArray) DATA_MANAGER.load(DataManager.PHOTO_DATA);
			
			if (photos !=  null)
			{
				// create a string array to hold the paths
				photoPaths = new String[LIST_DATA.length];
				
				if (photoPaths != null)
				{
					for (int i = 0; i < LIST_DATA.length; i++)
					{
						UniArray item = (UniArray) photos.getObject(LIST_DATA[i]);
						
						if (item != null)
						{
							photoPaths[i] = item.getString("hidnPath");
						}
					}
				}
			}
		}
		
		return photoPaths;
	}

}
