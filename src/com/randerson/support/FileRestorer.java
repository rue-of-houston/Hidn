package com.randerson.support;

import java.io.File;

import libs.FileSystem;
import libs.UniArray;
import android.content.Context;
import android.util.Log;


public class FileRestorer {

	// public do all operation ids
	public static final int BULK_RESTORE = 100;
	public static final int BULK_DELETE = 200;
	
	/** Method for restoring a media item back (document, photo, video) to its original location.
	 * container is the lowest level item container not the parent or group container */
	public static boolean restoreMediaItem(Context context, UniArray container, int dataType)
	{
		// the success bool
		boolean success = false;
		
		// create the explorer instance
		HidNExplorer explorer = new HidNExplorer(context);
		
		if (explorer != null)
		{
			// verify the container is not null
			if (container != null)
			{
				// get the file key
				String fileName = container.getString("fileName");
				
				// get the container meta path data
				String sourcePath = container.getString("sourcePath");
				String hidnPath = container.getString("hidnPath");
				
				// create the files from the paths
				// this is a reverse operation so the sourcePath is actually
				// used to create the destination file and the hidnPath the source file
				File source = new File(hidnPath);
				String destinationPath = new File(sourcePath).getParent();
				File destination = new File(destinationPath);
				
				if (source != null && destination != null && source.exists())
				{
					// attempt to move the file
					success = explorer.moveFile(source, destination, false);
					
					if (success)
					{
						// item moved, old file should be removed
						boolean result = explorer.deleteFile(source);
						
						if (result)
						{
							DataManager dataManager = new DataManager(context);
							
							if (dataManager != null)
							{
								// try to remove the item from the stored file
								boolean result2 = dataManager.removeItem(dataType, fileName);
								
								if (result2)
								{
									Log.i("HidNExplorer", "Item Data Removed");
								}
								else
								{
									Log.i("HidNExplorer", "Item Data Failed To Be Removed");
								}
							}
							
							Log.i("HidNExplorer", "File Removed");
						}
						else
						{
							Log.i("HidNExplorer", "File Failed To Be Removed");
						}
					}
				}
			}
		}
		
		return success;
	}
	
	/** Method for doing bulk restore or bulk delete - should pass in FileRestore.BULK_DELETE or FileRestore.BULK_RESTORE*/
	public static boolean doAll(Context context, UniArray groupContainer, int dataType, int bulkOperation)
	{
		UniArray container = null;
		UniArray APP_DATA = null;
		boolean success  = false;
		
		// switch case for setting the data to the appropriate field in the 1st level Uniarray
		switch(dataType)
		{
			
		case DataManager.DOCUMENT_DATA:
			
				APP_DATA = (UniArray) FileSystem.readObjectFile(context, DataManager.APP_DOCUMENTS_FILENAME, false);
			
				// add the updated data to the application save file
				container = (UniArray) APP_DATA.getObject(DataManager.DOCUMENT_KEY);
			break;
			
		case DataManager.PHOTO_DATA:
			
				APP_DATA = (UniArray) FileSystem.readObjectFile(context, DataManager.APP_PHOTOS_FILENAME, false);
			
				// add the updated data to the application save file
				container = (UniArray) APP_DATA.getObject(DataManager.PHOTO_KEY);
			break;
			
		case DataManager.VIDEO_DATA:
			
				APP_DATA = (UniArray) FileSystem.readObjectFile(context, DataManager.APP_VIDEOS_FILENAME, false);
			
				// add the updated data to the application save file
				container = (UniArray) APP_DATA.getObject(DataManager.VIDEO_KEY);
			break;
		
			default:
				break;
		}
		
		// check to make sure the container object is not null
		if (container != null)
		{
			// verify that the operation id is a valid one
			if (bulkOperation == BULK_RESTORE || bulkOperation == BULK_DELETE)
			{
				// get the keys for the objects in the group container
				String[] itemKeys = container.getAllObjectKeys();
				
				for (int i = 0; i < itemKeys.length; i++)
				{
					// get the key for i
					String key = itemKeys[i];
					
					if (key != null)
					{
						// get the low-level item container from the group container
						UniArray itemContainer = (UniArray) container.getObject(key);
						
						if (itemContainer != null)
						{
							if (bulkOperation == BULK_DELETE)
							{
								// perform the delete operation
								success = FileRestorer.deleteMediaItem(context, itemContainer, dataType);
							}
							else if (bulkOperation == BULK_RESTORE)
							{
								// perform the restore operation
								success = FileRestorer.restoreMediaItem(context, itemContainer, dataType);
							}
						}
					}
				}
			}
			else
			{
				Log.i("Operation Error", "No operation found to match that id");
			}
		}
		
		return success;
	}
	
	public static boolean deleteMediaItem(Context context, UniArray container, int dataType)
	{
		boolean success = false;
		
		// get an instance of the explorer object
		HidNExplorer explorer = new HidNExplorer(context);
		
		if (explorer != null)
		{
			// get the file name for the item
			String fileName = container.getString("fileName");
					
			// get the file path for the item
			String filePath = container.getString("hidnPath");
			
			if (filePath != null)
			{
				// create the file at the given path
				File source = new File(filePath);
				
				// verify that the item is valid and exists there
				if (source != null && source.exists())
				{
					// delete the file
					success = source.delete();
					
					if (success)
					{
						Log.i("File Removal", "Operation Success");
						
						// get an instance of the data manager class
						DataManager dataManager = new DataManager(context);
						
						if (dataManager != null)
						{
							// remove the object code
							boolean result = dataManager.removeItem(dataType, fileName);
							
							if (result)
							{
								Log.i("Item Removal", "Operation Success");
							}
							else
							{
								Log.i("Item Removal", "Operation Failed");
							}
						}
						
					}
					else
					{
						Log.i("File Removal", "Operation Failed");
					}
				}
			}
			else
			{
				Log.i("File Error", "File does not exists at path: " + filePath);
			}
		}
		
		return success;
	}
	
	public static boolean deleteAllNonMediaItems(Context context, UniArray container, int dataType)
	{
		boolean success = false;
		
		if (container != null)
		{
			// gets all of the keys
			String[] keys = container.getAllObjectKeys();
			
			if (keys != null)
			{
				// get an instance of the dataManager class
				DataManager dataManager = new DataManager(context);
				
				if (dataManager !=  null)
				{
					for (int i = 0; i < keys.length; i++)
					{
						// get the current key
						String key = keys[i];
						
						// try to remove the item
						success = dataManager.removeItem(dataType, key);
						
						if (success)
						{
							Log.i("Non Media Item", "Item Removed");
						}
						else
						{
							Log.i("Non Media Item", "Item Removal Failed");
						}
					}
				}
				
			}
		}
		
		return success;
	}
	
}
