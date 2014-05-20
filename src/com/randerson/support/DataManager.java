package com.randerson.support;

import com.randerson.interfaces.DataSetup;

import libs.FileSystem;
import libs.UniArray;
import android.content.Context;

public class DataManager implements DataSetup
{
	Context CONTEXT = null;
	
	public DataManager(Context context)
	{
		CONTEXT = context;
	}
	
	public UniArray load(int dataType)
	{
		UniArray data = null;
		UniArray APP_DATA;
		
			// switch case for retrieving the 1st level UniArray object
			switch(dataType)
			{
			case BROWSER_DATA:
				
					APP_DATA = (UniArray) FileSystem.readObjectFile(CONTEXT, APP_BROWSER_FILENAME, false);
					data = (UniArray) APP_DATA.getObject(BROWSER_KEY);
				break;
				
			case CONTACT_DATA:
				
					APP_DATA = (UniArray) FileSystem.readObjectFile(CONTEXT, APP_CONTACTS_FILENAME, false);
					data = (UniArray) APP_DATA.getObject(CONTACT_KEY);
				break;
				
			case DOCUMENT_DATA:
				
					APP_DATA = (UniArray) FileSystem.readObjectFile(CONTEXT, APP_DOCUMENTS_FILENAME, false);
					data = (UniArray) APP_DATA.getObject(DOCUMENT_KEY);
				break;
				
			case ENCRYPTION_DATA:
				
					APP_DATA = (UniArray) FileSystem.readObjectFile(CONTEXT, APP_ENCRYPTION_FILENAME, false);
					data = (UniArray) APP_DATA.getObject(ENCRYPTION_KEY);
				break;
				
			case NOTE_DATA:
				
					APP_DATA = (UniArray) FileSystem.readObjectFile(CONTEXT, APP_NOTES_FILENAME, false);
					data = (UniArray) APP_DATA.getObject(NOTE_KEY);
				break;
				
			case PHOTO_DATA:
				
					APP_DATA = (UniArray) FileSystem.readObjectFile(CONTEXT, APP_PHOTOS_FILENAME, false);
					data = (UniArray) APP_DATA.getObject(PHOTO_KEY);
				break;
				
			case VIDEO_DATA:
				
					APP_DATA = (UniArray) FileSystem.readObjectFile(CONTEXT, APP_VIDEOS_FILENAME, false);
					data = (UniArray) APP_DATA.getObject(VIDEO_KEY);
				break;
			
				default:
					break;
			}
		
		return data;
	}
	
	@Override
	public boolean saveTopLevel(int dataType, UniArray data)
	{	
		boolean success = false;
		UniArray APP_DATA = null;
		String APP_DATA_FILENAME = null;
		
		if (data != null)
		{
			// switch case for setting the data to the appropriate field in the 1st level Uniarray
			switch(dataType)
			{
			case BROWSER_DATA:
				
					// set the application file name
					APP_DATA_FILENAME = APP_BROWSER_FILENAME;
					
					APP_DATA = (UniArray) FileSystem.readObjectFile(CONTEXT, APP_BROWSER_FILENAME, false);
				
					// add the updated data to the application save file
					APP_DATA.putObject(BROWSER_KEY, data);
					
				break;
				
			case CONTACT_DATA:
				
					// set the application file name
					APP_DATA_FILENAME = APP_CONTACTS_FILENAME;
				
					APP_DATA = (UniArray) FileSystem.readObjectFile(CONTEXT, APP_CONTACTS_FILENAME, false);
				
					// add the updated data to the application save file
					APP_DATA.putObject(CONTACT_KEY, data);
				
				break;
				
			case DOCUMENT_DATA:
				
					// set the application file name
					APP_DATA_FILENAME = APP_DOCUMENTS_FILENAME;
					
					APP_DATA = (UniArray) FileSystem.readObjectFile(CONTEXT, APP_DOCUMENTS_FILENAME, false);
				
					// add the updated data to the application save file
					APP_DATA.putObject(DOCUMENT_KEY, data);
				break;
				
			case ENCRYPTION_DATA:
					
					// set the application file name
					APP_DATA_FILENAME = APP_ENCRYPTION_FILENAME;
				
					APP_DATA = (UniArray) FileSystem.readObjectFile(CONTEXT, APP_ENCRYPTION_FILENAME, false);
					
					// add the updated data to the application save file
					APP_DATA.putObject(ENCRYPTION_KEY, data);
				break;
				
			case NOTE_DATA:
				
					// set the application file name
					APP_DATA_FILENAME = APP_NOTES_FILENAME;
				
					APP_DATA = (UniArray) FileSystem.readObjectFile(CONTEXT, APP_NOTES_FILENAME, false);
					
					// add the updated data to the application save file
					APP_DATA.putObject(NOTE_KEY, data);
				break;
				
			case PHOTO_DATA:
				
					// set the application file name
					APP_DATA_FILENAME = APP_PHOTOS_FILENAME;
				
					APP_DATA = (UniArray) FileSystem.readObjectFile(CONTEXT, APP_PHOTOS_FILENAME, false);
					
					// add the updated data to the application save file
					APP_DATA.putObject(PHOTO_KEY, data);
				break;
				
			case VIDEO_DATA:
				
					// set the application file name
					APP_DATA_FILENAME = APP_VIDEOS_FILENAME;
				
					APP_DATA = (UniArray) FileSystem.readObjectFile(CONTEXT, APP_VIDEOS_FILENAME, false);
					
					// add the updated data to the application save file
					APP_DATA.putObject(VIDEO_KEY, data);
				break;
			
				default:
					break;
			}
			
			if (APP_DATA_FILENAME != null && APP_DATA != null)
			{
				// save the file to the system and capture the result
				success = FileSystem.writeObjectFile(CONTEXT, APP_DATA, APP_DATA_FILENAME, false);
			}
		}
		
		return success;
	}

	@Override
	public boolean saveItem(int dataType, UniArray data)
	{
		UniArray dataWrapper = null;
		String key = null;
		boolean success = false;
		UniArray APP_DATA = null;
		
		if (data != null)
		{
			// switch case for setting the data to the appropriate field in the 1st level Uniarray
			switch(dataType)
			{
			case BROWSER_DATA:
				
					APP_DATA = (UniArray) FileSystem.readObjectFile(CONTEXT, APP_BROWSER_FILENAME, false);
					
					// use the object inner item key as key
					key = data.getString("name");
					
					// add the updated data to the application save file
					dataWrapper = (UniArray) APP_DATA.getObject(BROWSER_KEY);
					
				break;
				
			case CONTACT_DATA:
				
					APP_DATA = (UniArray) FileSystem.readObjectFile(CONTEXT, APP_CONTACTS_FILENAME, false);
				
					// use the object inner item key as key
					key = data.getString("primaryPhone");
					
					// add the updated data to the application save file
					dataWrapper = (UniArray) APP_DATA.getObject(CONTACT_KEY);
				
				break;
				
			case DOCUMENT_DATA:
				
					APP_DATA = (UniArray) FileSystem.readObjectFile(CONTEXT, APP_DOCUMENTS_FILENAME, false);
				
					// use the object inner item key as key
					key = data.getString("fileName");
					
					// add the updated data to the application save file
					dataWrapper = (UniArray) APP_DATA.getObject(DOCUMENT_KEY);
				break;
				
			case ENCRYPTION_DATA:
				
					APP_DATA = (UniArray) FileSystem.readObjectFile(CONTEXT, APP_ENCRYPTION_FILENAME, false);
				
					// set the object key
					key = ENCRYPTION_KEY;
					
					// add the updated data to the application save file
					dataWrapper = (UniArray) APP_DATA.getObject(ENCRYPTION_KEY);
				break;
				
			case NOTE_DATA:
				
					APP_DATA = (UniArray) FileSystem.readObjectFile(CONTEXT, APP_NOTES_FILENAME, false);
				
					// use the object inner item key as key
					key = data.getString("id");
					
					// add the updated data to the application save file
					dataWrapper = (UniArray) APP_DATA.getObject(NOTE_KEY);
				break;
				
			case PHOTO_DATA:
				
					APP_DATA = (UniArray) FileSystem.readObjectFile(CONTEXT, APP_PHOTOS_FILENAME, false);
				
					// use the object inner item key as key
					key = data.getString("fileName");
					
					// add the updated data to the application save file
					dataWrapper = (UniArray) APP_DATA.getObject(PHOTO_KEY);
				break;
				
			case VIDEO_DATA:
				
					APP_DATA = (UniArray) FileSystem.readObjectFile(CONTEXT, APP_VIDEOS_FILENAME, false);
				
					// use the object inner item key as key
					key = data.getString("fileName");
					
					// add the updated data to the application save file
					dataWrapper = (UniArray) APP_DATA.getObject(VIDEO_KEY);
				break;
			
				default:
					break;
			}
			
			if (data != null && dataWrapper != null)
			{
				// add the passed in data to the proper data level in 2nd level UniArray
				dataWrapper.putObject(key, data);
				
				// pass the updated 1st level UniArray to be saved in the top-level
				success = saveTopLevel(dataType, dataWrapper);
			}
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
			data.putString("primaryPhone", phoneNumbers[0]);
			data.putString("secondaryPhone", phoneNumbers[1]);
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
	public UniArray createKeysItem(String key, String encodedValue)
	{
		// create a map file for storing the mediaItem data
		UniArray data = new UniArray();
		
		if (data != null)
		{
			// add the data to the map
			data.putObject(key, encodedValue);
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
			int randomIdNumber = (int) (Math.random() * 1000000);
			String id = name.substring(0, 3) + "-" + randomIdNumber;
			
			// add the data to the map
			data.putString("title", name);
			data.putString("id", id);
			data.putString("encodedContent", encodedContent);
		}
		
		return data;
	}

	@Override
	public boolean removeItem(int dataType, String key)
	{
		UniArray dataWrapper = null;
		boolean success = false;
		UniArray APP_DATA = null;
		
		if (key != null)
		{
			// switch case for setting the data to the appropriate field in the 1st level Uniarray
			switch(dataType)
			{
			case BROWSER_DATA:
				
					APP_DATA = (UniArray) FileSystem.readObjectFile(CONTEXT, APP_BROWSER_FILENAME, false);
					
					// add the updated data to the application save file
					dataWrapper = (UniArray) APP_DATA.getObject(BROWSER_KEY);
					
				break;
				
			case CONTACT_DATA:
				
					APP_DATA = (UniArray) FileSystem.readObjectFile(CONTEXT, APP_CONTACTS_FILENAME, false);
				
					// add the updated data to the application save file
					dataWrapper = (UniArray) APP_DATA.getObject(CONTACT_KEY);
				
				break;
				
			case DOCUMENT_DATA:
				
					APP_DATA = (UniArray) FileSystem.readObjectFile(CONTEXT, APP_DOCUMENTS_FILENAME, false);
				
					// add the updated data to the application save file
					dataWrapper = (UniArray) APP_DATA.getObject(DOCUMENT_KEY);
				break;
				
			case ENCRYPTION_DATA:
				
					APP_DATA = (UniArray) FileSystem.readObjectFile(CONTEXT, APP_ENCRYPTION_FILENAME, false);
				
					// add the updated data to the application save file
					dataWrapper = (UniArray) APP_DATA.getObject(ENCRYPTION_KEY);
				break;
				
			case NOTE_DATA:
				
					APP_DATA = (UniArray) FileSystem.readObjectFile(CONTEXT, APP_NOTES_FILENAME, false);
				
					// add the updated data to the application save file
					dataWrapper = (UniArray) APP_DATA.getObject(NOTE_KEY);
				break;
				
			case PHOTO_DATA:
				
					APP_DATA = (UniArray) FileSystem.readObjectFile(CONTEXT, APP_PHOTOS_FILENAME, false);
				
					// add the updated data to the application save file
					dataWrapper = (UniArray) APP_DATA.getObject(PHOTO_KEY);
				break;
				
			case VIDEO_DATA:
				
					APP_DATA = (UniArray) FileSystem.readObjectFile(CONTEXT, APP_VIDEOS_FILENAME, false);
				
					// add the updated data to the application save file
					dataWrapper = (UniArray) APP_DATA.getObject(VIDEO_KEY);
				break;
			
				default:
					break;
			}
			
			if (dataWrapper != null)
			{
				// add the passed in data to the proper data level in 2nd level UniArray
				dataWrapper.removeObject(key);
				
				// pass the updated 1st level UniArray to be saved in the top-level
				success = saveTopLevel(dataType, dataWrapper);
			}
		}
		
		return success;
		
	}
}
