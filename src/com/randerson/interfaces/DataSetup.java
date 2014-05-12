package com.randerson.interfaces;

import libs.UniArray;

public interface DataSetup {

	// application data file name	// UniArray top-level object
	public final String APP_DATA_FILENAME = "data.hdn";  
	
	// final object values for matching data
	public final int PHOTO_DATA = 0;
	public final int VIDEO_DATA = 1;
	public final int DOCUMENT_DATA = 2;
	public final int NOTE_DATA = 3;
	public final int CONTACT_DATA = 4;
	public final int BROWSER_DATA = 5;
	public final int ENCRYPTION_DATA = 6;
	
	// final object keys for storing / retrieving specific data  // UniArray 1st-level object
	public final String PHOTO_KEY = "photos";
	public final String VIDEO_KEY = "videos";
	public final String DOCUMENT_KEY = "documents";
	public final String NOTE_KEY = "notes";
	public final String CONTACT_KEY = "contacts";
	public final String BROWSER_KEY = "browser";
	public final String ENCRYPTION_KEY = "keys";
	
	// final object keys for storing / retrieving encryption keys  // HashMap 2nd-level object
	public final String PUBLIC_KEY = "publicKey";
	public final String PRIVATE_KEY = "privateKey";
	public final String SECRET_KEY = "secretKey";
	
	// methods for storing and retrieving
	public UniArray load(int dataType);
	public boolean saveTopLevel(int dataType, UniArray data);
	public boolean saveItem(int dataType, UniArray data);
	
	// methods for creating items for storing 1st-level UniArray
	public UniArray createMediaItem(String filename, String sourcePath, String hidnPath, String[] encodedData);
	public UniArray createNoteItem(String name, String encodedContent);
	public UniArray createContactItem(String[] name, String email, String[] phoneNumbers, String address);
	public UniArray createBookmarkItem(String name, String url);
	public UniArray createKeysItem(String[] key, Object[] value);
}
