package com.randerson.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.NonReadableChannelException;
import java.nio.channels.NonWritableChannelException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;
import android.util.Log;

import com.randerson.interfaces.ExplorerSetup;

public class HidNExplorer implements ExplorerSetup {

	Context CONTEXT = null;
	
	public HidNExplorer(Context context)
	{
		CONTEXT = context.getApplicationContext();
	}
	
	// method for returning the top-most directories
	// for a given path
	@Override
	public ArrayList<File> getDirectories(File directory)
	{
		
		ArrayList<File> directoryList = new ArrayList<File>();
		
		// check for valid objects
		if (directory != null && directoryList != null)
		{
			File[] fileList = directory.listFiles();
			
			// verify the fileList has been created properly
			if (fileList != null)
			{
				// iterate over the files in the fileList
				for (File file : fileList)
				{
					// check if the current file in the loop is a directory or not
					// if it is a directory add the file to the directoryList object
					if (file.isDirectory())
					{
						directoryList.add(file);
					}
				}
			}
		}
		
		return directoryList;
	}
	

	// method for getting a list of image files
	@Override
	public ArrayList<File> getListOfPhotos()
	{
		// arrayList to return
		ArrayList<File> photoList = new ArrayList<File>();
		
		// file types to query and return
		String[] fileTypes = new String[] {".jpg", ".bmp", ".png", ".gif"};
		
		// system directories to query in
		File[] directories = new File[] {
											getPublicDirectory(PICTURES),
											getPublicDirectory(DCIM),
											getPublicDirectory(DOWNLOADS)
										};
		
		// get a list of all image files in the provided directories
		photoList = getFiles(directories, fileTypes);
		
		return photoList;
	}

	// method for getting a list of video files
	@Override
	public ArrayList<File> getListOfVideos()
	{
		// arrayList to return
		ArrayList<File> videoList = new ArrayList<File>();
				
		// file types to query and return
		String[] fileTypes = new String[] {".mpg", ".wmv", ".3gp", ".mp4"};
		
		// system directories to query in
		File[] directories = new File[] {
											getPublicDirectory(MOVIES),
											getPublicDirectory(DCIM),
											getPublicDirectory(DOWNLOADS)
										};
		
		// get a list of all video files in the provided directories
		videoList = getFiles(directories, fileTypes);

		return videoList;
	}

	// method for getting a list of document files
	@Override
	public ArrayList<File> getListOfDocuments()
	{
		// arrayList to return
		ArrayList<File> documentList = new ArrayList<File>();
				
		// file types to query and return
		String[] fileTypes = new String[] {".pdf", ".txt", ".doc"};
		
		// system directories to query in
		File[] directories = new File[] {
											new File(MAIN_DIRECTORY)
										};
		
		// get a list of all documents in the provided directories
		documentList = getFiles(directories, fileTypes);
		
		return documentList;
	}

	// private method for returning matching files of a type on the system
	// from private method query
	private ArrayList<File> getFiles(File[] directories, String[] fileTypes)
	{
		// array list of files returned from directories
		ArrayList<File> fileList = new ArrayList<File>();
		
		// array list of files that match the fileTypes
		ArrayList<File> returnList = new ArrayList<File>();
		
		if (fileList != null)
		{
			// iterate over the passed in directories / files
			for (int n = 0; n < directories.length; n++)
			{
				fileList = query(directories[n], fileList);
			}
			
			// iterate over each file in the fileList
			for (File file : fileList)
			{
				// verify the file is valid
				if (file != null)
				{
					// iterate over the fileTypes array
					for (int i = 0; i < fileTypes.length; i++)
					{
						// check if the file has image extension
						if (file.getName().endsWith(fileTypes[i]))
						{
							// add the file to the photo list array
							returnList.add(file);
							
							break;
						}
					}
					
				}
			}
			
		}
		
		return returnList;
	}
	

	// method for getting File for public directories
	@Override
	public File getPublicDirectory(String type)
	{
		File directory = Environment.getExternalStoragePublicDirectory(type);
		
		return directory;
	}
	
	// method for returning all files and nested files in a filepath
	private ArrayList<File> query(File filePath, ArrayList<File> listOfFiles)
	{
		// check if the file is a directory to be re-queried or a file to be added
		if (filePath.isDirectory())
		{
			// get a list of the files inside this directory
			File[] filesArray = filePath.listFiles();
			
			for (File arrayFile : filesArray)
			{
				// query the directory for files and directories
				// returning all of the files found added to the array list
				// and re-querying the directories
				listOfFiles = query(arrayFile, listOfFiles);
			}
			
		}
		else if (filePath.isFile())
		{
			// add the file to the array list
			listOfFiles.add(filePath);
		}
		
		return listOfFiles;
	}

	@Override
	public boolean hideFile(File file)
	{
		boolean success = false;
		
		if (file.exists())
		{
			// string representation of the file path and the file name
			String path = file.getParentFile().toString();
			String name = file.getName();
			
			// attempt to hide the file on the system level and
			// capture the operation success
			success = file.renameTo(new File(path + "/." + name));
		}
		
		return success;
	}

	@Override
	public boolean renameFile(File file, String name)
	{
		boolean success = false;
		
		if (file.exists())
		{
			// string representation of the file path
			String path = file.getParentFile().toString();
			
			// attempt to rename the file and capture the operation result
			success = file.renameTo(new File(path, name));
		}
		
		return success;
	}

	@Override
	public boolean deleteFile(File file)
	{
		boolean success = false;
		
		// verify the file exists and is not a directory
		if (file.exists() && file.isFile())
		{
			// delete the file and capture the operation success or fail
			success = file.delete();
		}
		
		return success;
	}

	@Override
	public boolean moveFile(File source, File destination, boolean shouldHideFile)
	{
		boolean success = false;
		
		// get the source file filename
		String filename = source.getName();
		
		if (shouldHideFile)
		{
			filename = "." + filename;
		}
		
		destination = new File(destination.getPath(), filename);
		Log.i("Path", destination.getPath());
		
		try {
			
			FileInputStream inputStream = new FileInputStream(source);
			FileOutputStream outputStream = new FileOutputStream(destination);
			
			FileChannel sourceChannel = inputStream.getChannel();
			FileChannel destinationChannel = outputStream.getChannel();
			
			if (sourceChannel != null && destinationChannel != null)
			{
				try {
					
					long count = 0;
					long size = sourceChannel.size();
					
					while (count < size)
					{
						count += sourceChannel.transferTo(0, size - count, destinationChannel);
						
						Log.i("File Channel", count + " Bytes Transferred Out Of " + size);
					}
					
					sourceChannel.close();
					destinationChannel.close();
					
					if (count == size)
					{
						success = true;
					}
					
				} catch (NonReadableChannelException e) {
					e.printStackTrace();
					
					Log.i("Channel Error", "Source Channel Not Readable");
					
				} catch (NonWritableChannelException e) {
					e.printStackTrace();
					
					Log.i("Channel Error", "Destination Channel Not Writable");
					
				} catch (IOException e) {
					e.printStackTrace();
					
					Log.i("IO Error", "Error Transfering From Source Channel");
				}
			}
			
			inputStream.close();
			outputStream.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
			Log.i("FileChannel Error", "File Not Found");
		} catch (IOException e) {
			e.printStackTrace();
			
			Log.i("IO Error", "Error closing IO stream");
		}
		
		return success;
	}
	
	public static int calulateBitmapSampleSize(String filepath, int targetHeight, int targetWidth)
	{
		// default image sample size
		int sampleSize = 1;
		
		// create an options object
		BitmapFactory.Options options = new Options();
		
		if (options != null && filepath != null)
		{
			// set the flag for decoding bounds only
			options.inJustDecodeBounds = true;
			
			// create bitmap from file path with appropriate options
			BitmapFactory.decodeFile(filepath, options);
			
			// check if the images need to be subsampled
			if (options.outHeight > targetHeight || options.outWidth > targetWidth)
			{

				final int halvedHeight = (options.outHeight / 2);
				final int halvedWidth = (options.outWidth / 2);
				
				
				// iterate over the values increasing the sample size by powers of 2 
				// until a sample size is valid for both arguments
				while (((halvedHeight / sampleSize) > targetHeight) &&
						((halvedWidth / sampleSize) > targetWidth))
				{
				
					// increase the sample size in range of power of 2
					sampleSize *= 2;
				}
			}
		}
		
		return sampleSize;
	}

}
