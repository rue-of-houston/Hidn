package com.randerson.interfaces;

import java.io.File;
import java.util.ArrayList;

import android.os.Environment;

public interface ExplorerSetup 
{

	public final int TYPE_PICTURES = 0;
	public final int TYPE_VIDEOS = 1;
	public final int TYPE_DOCUMENTS = 2;
	public final int TYPE_DIRECTORY = 3;
	
	public final String PICTURES = Environment.DIRECTORY_PICTURES;
	public final String DCIM = Environment.DIRECTORY_DCIM;
	public final String MOVIES = Environment.DIRECTORY_MOVIES;
	public final String DOWNLOADS = Environment.DIRECTORY_DOWNLOADS;
	public final String MUSIC = Environment.DIRECTORY_MUSIC;
	public final String BLUETOOTH = Environment.getExternalStorageDirectory().getPath() + "/Bluetooth";
	public final String DOCUMENTS = Environment.getExternalStorageDirectory().getPath() + "/Documents";
	public final String MAIN_DIRECTORY = Environment.getExternalStorageDirectory().getPath(); 
	
	public ArrayList<File> getDirectories(File directory);
	public ArrayList<File> getListOfPhotos();
	public ArrayList<File> getListOfVideos();
	public ArrayList<File> getListOfDocuments();
	public File getPublicDirectory(String type);
	public boolean hideFile(File file);
	public boolean renameFile(File file, String name);
	public boolean deleteFile(File file);
	public boolean moveFile(File source, File destination, boolean shouldHideFile);
}
