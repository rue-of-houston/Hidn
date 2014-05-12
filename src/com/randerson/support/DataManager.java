package com.randerson.support;

import com.randerson.interfaces.DataSetup;

import libs.FileSystem;
import libs.UniArray;
import android.content.Context;

public class DataManager implements DataSetup
{
	Context CONTEXT = null;
	UniArray APP_DATA = null;
	
	public DataManager(Context context)
	{
		CONTEXT = context;
		APP_DATA = (UniArray) FileSystem.readObjectFile(context, APP_DATA_FILENAME, true);
	}
	
	public UniArray load(int dataType)
	{
		UniArray data = null;
		
		if (APP_DATA != null)
		{
			// switch case for retrieving the 1st level UniArray object
			switch(dataType)
			{
			case BROWSER_DATA:
				
					data = (UniArray) APP_DATA.getObject(BROWSER_KEY);
				break;
				
			case CONTACT_DATA:
				
					data = (UniArray) APP_DATA.getObject(CONTACT_KEY);
				break;
				
			case DOCUMENT_DATA:
				
					data = (UniArray) APP_DATA.getObject(DOCUMENT_KEY);
				break;
				
			case ENCRYPTION_DATA:
				
					data = (UniArray) APP_DATA.getObject(ENCRYPTION_KEY);
				break;
				
			case NOTE_DATA:
				
					data = (UniArray) APP_DATA.getObject(NOTE_KEY);
				break;
				
			case PHOTO_DATA:
				
					data = (UniArray) APP_DATA.getObject(PHOTO_KEY);
				break;
				
			case VIDEO_DATA:
				
					data = (UniArray) APP_DATA.getObject(VIDEO_KEY);
				break;
			
				default:
					break;
			}
		}
		
		return data;
	}
	
	@Override
	public boolean saveTopLevel(int dataType, UniArray data)
	{	
		boolean success = false;
		
		if (data != null)
		{
			// switch case for setting the data to the appropriate field in the 1st level Uniarray
			switch(dataType)
			{
			case BROWSER_DATA:
				
					// add the updated data to the application save file
					APP_DATA.putObject(BROWSER_KEY, data);
					
				break;
				
			case CONTACT_DATA:
				
					// add the updated data to the application save file
					APP_DATA.putObject(CONTACT_KEY, data);
				
				break;
				
			case DOCUMENT_DATA:
				
					// add the updated data to the application save file
					APP_DATA.putObject(DOCUMENT_KEY, data);
				break;
				
			case ENCRYPTION_DATA:
				
					// add the updated data to the application save file
					APP_DATA.putObject(ENCRYPTION_KEY, data);
				break;
				
			case NOTE_DATA:
				
					// add the updated data to the application save file
					APP_DATA.putObject(NOTE_KEY, data);
				break;
				
			case PHOTO_DATA:
				
					// add the updated data to the application save file
					APP_DATA.putObject(PHOTO_KEY, data);
				break;
				
			case VIDEO_DATA:
				
					// add the updated data to the application save file
					APP_DATA.putObject(VIDEO_KEY, data);
				break;
			
				default:
					break;
			}
			
			// save the file to the system and capture the result
			success = FileSystem.writeObjectFile(CONTEXT, APP_DATA, APP_DATA_FILENAME, true);
		
			
		}
		
		return success;
	}

	@Override
	public boolean saveItem(int dataType, UniArray data)
	{
		UniArray dataWrapper = null;
		String key = null;
		boolean success = false;
		
		if (data != null)
		{
			// switch case for setting the data to the appropriate field in the 1st level Uniarray
			switch(dataType)
			{
			case BROWSER_DATA:
				
					// use the object inner item key as key
					key = data.getString("name");
					
					// add the updated data to the application save file
					dataWrapper = (UniArray) APP_DATA.getObject(BROWSER_KEY);
					
				break;
				
			case CONTACT_DATA:
				
					// use the object inner item key as key
					key = data.getString("mobilePhone");
					
					// add the updated data to the application save file
					dataWrapper = (UniArray) APP_DATA.getObject(CONTACT_KEY);
				
				break;
				
			case DOCUMENT_DATA:
				
					// use the object inner item key as key
					key = data.getString("fileName");
					
					// add the updated data to the application save file
					dataWrapper = (UniArray) APP_DATA.getObject(DOCUMENT_KEY);
				break;
				
			case ENCRYPTION_DATA:
				
					// use the object inner item key as key
					key = data.getString("keys");
					
					// add the updated data to the application save file
					dataWrapper = (UniArray) APP_DATA.getObject(ENCRYPTION_KEY);
				break;
				
			case NOTE_DATA:
				
					// use the object inner item key as key
					key = data.getString("name");
					
					// add the updated data to the application save file
					dataWrapper = (UniArray) APP_DATA.getObject(NOTE_KEY);
				break;
				
			case PHOTO_DATA:
				
					// use the object inner item key as key
					key = data.getString("fileName");
					
					// add the updated data to the application save file
					dataWrapper = (UniArray) APP_DATA.getObject(PHOTO_KEY);
				break;
				
			case VIDEO_DATA:
				
					// use the object inner item key as key
					key = data.getString("fileName");
					
					// add the updated data to the application save file
					dataWrapper = (UniArray) APP_DATA.getObject(VIDEO_KEY);
				break;
			
				default:
					break;
			}
			
			// add the passed in data to the proper data level in 2nd level UniArray
			dataWrapper.putObject(key, data);
			
			// pass the updated 1st level UniArray to be saved in the top-level
			success = saveTopLevel(dataType, dataWrapper);
		}
		
		return success;
	}

	@Override
	public UniArray createMediaItem(String filename,
			String sourcePath, String hidnPath, String[] encodedData) 
	{
		// create a map file for storing the mediaItem data
		UniArray data = new UniArray();
		
		if (data != null)
		{
			// add the data to the map
			data.putString("fileName", filename);
			data.putString("sourcePath", sourcePath);
			data.putString("hidnPath", hidnPath);
			data.putObject("encodedData", encodedData);
		}
		
		return data;
	}

	@Override
	public UniArray createContactItem(String[] name,
			String email, String[] phoneNumbers, String address) {

		// create a map file for storing the mediaItem data
		UniArray data = new UniArray();
		
		if (data != null)
		{
			// add the data to the map
			data.putString("firstName", name[0]);
			data.putString("lastName", name[1]);
			data.putString("address", address);
			data.putString("email", email);
			data.putString("homePhone", phoneNumbers[0]);
			data.putString("mobilePhone", phoneNumbers[1]);
		}
		
		return data;
	}

	@Override
	public UniArray createBookmarkItem(String name, String url)
	{
		
		// create a map file for storing the mediaItem data
		UniArray data = new UniArray();
		
		if (data != null)
		{
			// add the data to the map
			data.putString("name", name);
			data.putString("url", url);
		}
		
		return data;
	}

	@Override
	public UniArray createKeysItem(String[] key, Object[] value)
	{
		// create a map file for storing the mediaItem data
		UniArray data = new UniArray();
		
		if (data != null)
		{
			// add the data to the map
			for (int i = 0; i < key.length; i++)
			{
				data.putObject(key[i], value[i]);
			}
		}
		
		return data;
	}

	@Override
	public UniArray createNoteItem(String name, String encodedContent)
	{
		// create a map file for storing the mediaItem data
		UniArray data = new UniArray();
		
		if (data != null)
		{
			// add the data to the map
			data.putString("name", name);
			data.putString("encodedContent", encodedContent);
		}
		
		return data;
	}
}
