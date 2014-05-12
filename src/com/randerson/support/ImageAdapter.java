package com.randerson.support;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter 
{
	Context CONTEXT = null;
	ArrayList<File> GRID_CONTENT = null;
	BitmapFactory.Options OPTIONS = null;
	ImageView[] THUMBS = null;
	
	public ImageAdapter(Context context, ArrayList<File> content)
	{
		CONTEXT = context.getApplicationContext();
		GRID_CONTENT = content;
		
		// the bitmap factory options
		OPTIONS = new BitmapFactory.Options();
		OPTIONS.inPurgeable = true;
		
		// 
		THUMBS = new ImageView[GRID_CONTENT.size()];
	}

	@Override
	public int getCount() 
	{
		return GRID_CONTENT.size();
	}

	@Override
	public Object getItem(int position)
	{
		return THUMBS[position];
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{	
		ImageView imageView = null;
		
		if (convertView == null)
		{
			imageView = new ImageView(CONTEXT);
			//imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
			imageView.setAdjustViewBounds(true);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(8, 8, 8, 8);
		}
		else 
		{
			// convert the view
			imageView = (ImageView) convertView;
		}

			// get the file path for the current item
			File currentFile = GRID_CONTENT.get(position);
			if (currentFile != null)
			{
				int sample = HidNExplorer.calulateBitmapSampleSize(currentFile.getPath(), 90, 90);
				
				// set the sampling size in the options
				OPTIONS.inSampleSize = sample;
				
				// create bitmap from file path with appropriate options
				Bitmap image = BitmapFactory.decodeFile(currentFile.getPath(), OPTIONS);
				
				// set the imageview bitmap
				imageView.setImageBitmap(image);
				
			}
			
			// store a reference to the imageView
			THUMBS[position] = imageView;
			
		return imageView;
	}
	
	@Override
	public boolean hasStableIds()
	{
		return true;
	}
	
}
