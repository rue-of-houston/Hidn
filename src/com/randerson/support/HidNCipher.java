package com.randerson.support;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONException;
import org.json.JSONObject;

import libs.UniArray;

import com.randerson.interfaces.DataSetup;
import com.randerson.interfaces.EncryptionSetup;

public class HidNCipher implements EncryptionSetup {

	// class fields for AES encryption
	private byte[] SECRET_KEY = null;
	private SecretKeySpec SECRET_KEY_SPEC = null;
	
	// class fields for RSA encryption
	//private Key PUBLIC_KEY = null;
	//private Key PRIVATE_KEY = null;
	
	private int ENCRYPTION_ID = 0;
	
	// key map file
	private UniArray KEYS = null;
	
	// save key boolean to determine whether the keys should be stored or not
	// if no keys are stored new keys should be saved otherwise non should resave
	public boolean SHOULD_SAVE_KEYS = true;
	
	// application context
	public Context CONTEXT = null;
	
	// the app data managing class
	DataManager DATA_MANAGER = null;
	
	/** The default constructor used for all operations encoding / decoding objects. */
	public HidNCipher(Context context, String algorithmType)
	{
		CONTEXT = context;
		
		// setup the app storage class
		DATA_MANAGER = new DataManager(CONTEXT);
		
		// setup the encryption algorithm to utilize
		if (algorithmType.equals(AES_ALGORITHM))
		{
			ENCRYPTION_ID = AES_ENCRYPTION;
		}
		else if (algorithmType.equals(RSA_ALGORITHM))
		{
			ENCRYPTION_ID = RSA_ENCRYPTION;
		}
		
		// setup the class to use the specific encryption algorithms
		setupCipher();
	}
	
	/** Only use this method if you only need base64 encoding & decoding. */
	public HidNCipher(){};

	@Override
	/** RSA Encryption has been removed */
	public String encodeData(byte[] objectBytes)
	{
		// string for encoded data to return
		String encodedData = null;
		
		// create a cipher for encrypting
		Cipher cipher = null;
		
		// create byte array of encoded bytes
		byte[] codedBytes = null;
		
		if (ENCRYPTION_ID == AES_ENCRYPTION)
		{
			try {
				
				// get an instance of the cipher with specified algorithm
				// and initialize it with specific key for encryption
				cipher = Cipher.getInstance(AES_ALGORITHM);
				cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY_SPEC);
				
				// finalize the cipher encryption and capture the byte array
				codedBytes = cipher.doFinal(objectBytes);
				
				// capture base64 string of encoded data
				encodedData = encodeToBaseString(codedBytes);
				
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				
				Log.i("Cipher Instance Error", "Algorithm Not Found");
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
				
				Log.i("Cipher Instance Error", "Unsupported Padding Mechanism");
			} catch (InvalidKeyException e) {
				e.printStackTrace();
				
				Log.i("Cipher Init Error ", "Invalid key");
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
				
				Log.i("Cipher Final Error ", "Data Length Does Not Match Block Size");
			} catch (BadPaddingException e) {
				e.printStackTrace();
				
				Log.i("Cipher Final Error ", "Input Data Does Not Have Proper Padding Bytes");
			}
			
		}
		/*else if (ENCRYPTION_ID == RSA_ENCRYPTION)
		{
			try {
				
				// get an instance of the cipher with specified algorithm
				// and initialize it with specific key for encryption
				cipher = Cipher.getInstance(RSA_ALGORITHM);
				cipher.init(Cipher.ENCRYPT_MODE, PRIVATE_KEY);
				
				// finalize the cipher encryption and capture the byte array
				codedBytes = cipher.doFinal(objectBytes);
				
				// capture base64 string of encoded data
				encodedData = encodeToBaseString(codedBytes);
				
			} catch (InvalidKeyException e) {
				e.printStackTrace();
				
				Log.i("Cipher (RSA) Init Error ", "Invalid key");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				
				Log.i("Cipher (RSA) Instance Error", "Algorithm Not Found");
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
				
				Log.i("Cipher (RSA) Instance Error", "Unsupported Padding Mechanism");
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
				
				Log.i("Cipher (RSA) Final Error ", "Data Length Does Not Match Block Size");
			} catch (BadPaddingException e) {
				e.printStackTrace();
				
				Log.i("Cipher (RSA) Final Error ", "Input Data Does Not Have Proper Padding Bytes");
			}
		}*/
		
		return encodedData;
	}

	@Override
	/** RSA Encryption has been removed */
	public byte[] decodeData(String encodedData)
	{
		// create a cipher for encrypting
		Cipher cipher = null;
		
		// byte array of decoded data to return
		byte[] decodedData = null;
		
		// create byte array of decoded bytes
		byte[] encodedBytes = decodeBaseString(encodedData);
		
		if (ENCRYPTION_ID == AES_ENCRYPTION)
		{
			
			try {
				
				// get an instance of the cipher with specified algorithm
				// and initialize it with specific key for encryption
				cipher = Cipher.getInstance(AES_ALGORITHM);
				cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY_SPEC);
				
				// finalize the cipher encryption and capture the byte array
				decodedData = cipher.doFinal(encodedBytes);
				
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				
				Log.i("Cipher (Decoding) Instance Error", "Algorithm Not Found");
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
				
				Log.i("Cipher (Decoding) Instance Error", "Unsupported Padding Mechanism");
			} catch (InvalidKeyException e) {
				e.printStackTrace();
				
				Log.i("Cipher (Decoding) Init Error ", "Invalid key");
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
				
				Log.i("Cipher (Decoding) Final Error ", "Data Length Does Not Match Block Size");
			} catch (BadPaddingException e) {
				e.printStackTrace();
				
				Log.i("Cipher (Decoding) Final Error ", "Input Data Does Not Have Proper Padding Bytes");
			}
			
		}
		/*else if (ENCRYPTION_ID == RSA_ENCRYPTION)
		{
			try {
				
				// get an instance of the cipher with specified algorithm
				// and initialize it with specific key for encryption
				cipher = Cipher.getInstance(RSA_ALGORITHM);
				cipher.init(Cipher.DECRYPT_MODE, PUBLIC_KEY);
				
				// finalize the cipher encryption and capture the byte array
				decodedData = cipher.doFinal(encodedBytes);
				
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				
				Log.i("Cipher (RSA Decoding) Instance Error", "Algorithm Not Found");
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
				
				Log.i("Cipher (RSA Decoding) Instance Error", "Unsupported Padding Mechanism");
			} catch (InvalidKeyException e) {
				e.printStackTrace();
				
				Log.i("Cipher (RSA Decoding) Init Error ", "Invalid key");
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
				
				Log.i("Cipher (RSA Decoding) Final Error ", "Data Length Does Not Match Block Size");
			} catch (BadPaddingException e) {
				e.printStackTrace();
				
				Log.i("Cipher (RSA Decoding) Final Error ", "Input Data Does Not Have Proper Padding Bytes");
			}
		}*/
		
		return decodedData;
	}

	
	// method for setting up class for encryption / decryption
	@SuppressLint("TrulyRandom")
	private void setupCipher()
	{
		// SETTING UP THE AES ALGORITHM SIDE
			try {
				
				// create generator for specified algorithm
				// and get an instance with supplied randomness
				KeyGenerator KEY_GEN = KeyGenerator.getInstance(AES_ALGORITHM);
				KEY_GEN.init(128);
				
				// generate a secret key
				SECRET_KEY = (KEY_GEN.generateKey()).getEncoded();
				
				// set the secret key specialization
				SECRET_KEY_SPEC = new SecretKeySpec(SECRET_KEY, AES_ALGORITHM);
				
			} catch (NoSuchAlgorithmException e) {
				
				e.printStackTrace();
				Log.i("Key Spec Error", "Algorithm Not Found");
			}
		
		
		
		// SETTING UP THE RSA ALGORITHM SIDE
		/*	try {
				
				// create generator for specified algorithm
				// and get an instance with supplied randomness
				KeyPairGenerator KEY_PAIR_GEN = KeyPairGenerator.getInstance(RSA_ALGORITHM);
				KEY_PAIR_GEN.initialize(1024);
				
				// generate the key pairs
				KeyPair KEY_PAIR = KEY_PAIR_GEN.generateKeyPair();
				
				// get the public and private keys
				PRIVATE_KEY = KEY_PAIR.getPrivate();
				PUBLIC_KEY = KEY_PAIR.getPublic();
							
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				
				Log.i("Key Pair Error", "Algorithm Not Found");
			}
			*/
			// load the stored keys
			loadStoredKeys();
			
			// save the keys
			storeCipherKeys();
	}
	
	private void loadStoredKeys()
	{
		// disable the key save bool
		SHOULD_SAVE_KEYS = false;
		
		// check for previously created keys to overwrite the newly generated keys
		KEYS = (UniArray) DATA_MANAGER.load(DataManager.ENCRYPTION_DATA).getObject(DataManager.ENCRYPTION_KEY);
		
		if (KEYS != null)
		{	
			// check if the saved data has the public and private keys saved
			// if so, update the class fields to use those keys
			/*PUBLIC_KEY = (Key) KEYS.getObject(DataManager.PUBLIC_KEY);
			  PRIVATE_KEY = (Key) KEYS.getObject(DataManager.PRIVATE_KEY);
			
			if (PUBLIC_KEY != null && PRIVATE_KEY != null)
			{
				Log.i("Key Validation", "Valid Keys Found");
			}
			else 
			{
				Log.i("Key Validation", "One Or More Invalid Keys Found");
				
				// reset the key save bool
				SHOULD_SAVE_KEYS = true;
			}
			*/
			
			// retrieve the coded secret key and decode it
			String encodedString = (String) KEYS.getObject(DataManager.SECRET_KEY);
			SECRET_KEY = decodeBaseString(encodedString);
			
			if (SECRET_KEY != null)
			{
				// set the secret key specialization with the decoded secret key
				SECRET_KEY_SPEC = new SecretKeySpec(SECRET_KEY, AES_ALGORITHM);
				
				Log.i("Secret Key Validation", "Valid Secret Key Found");
			}
			else 
			{
				Log.i("Secret Key Validation", "Invalid Secret Key Found");
				
				// reset the key save bool
				SHOULD_SAVE_KEYS = true;
			}
			
		}
		else
		{
			// reset the key save bool
			SHOULD_SAVE_KEYS = true;
			
			Log.i("Loading Stored Keys", "No Keys Found");
		}
	}

	private void storeCipherKeys()
	{

		// verify that the keys should be stored
		if (SHOULD_SAVE_KEYS)
		{	
			if (DATA_MANAGER != null)
			{
				
				// create the value mapping
				String codedSecretKey = encodeToBaseString(SECRET_KEY);
				
				// pass in the mappings to create a keys item for storing
				KEYS = DATA_MANAGER.createKeysItem(DataManager.SECRET_KEY, codedSecretKey);
				
				// save the keys item
				DATA_MANAGER.saveItem(DataManager.ENCRYPTION_DATA, KEYS);
				
				Log.i("Storing Keys", "Keys Stored Successfully");
			}	
		}
		else
		{
			Log.i("Storing Keys", "Keys Failed To Store");
		}
	}

	// public method for getting a file bytes
	public static ArrayList<byte[]> toByteArray(File file)
	{
		
		final long BYTE_COUNT = file.length();
		int[] count = {(int) BYTE_COUNT};
		int bytePasses = 1;
		int currentPass = 1;
		ArrayList<byte[]> masterBytes = new ArrayList<byte[]>();
		FileInputStream in = null;
		
		
		// check if the byte size is greater than integer limit
		// if so the file must be split into parts to capture all of the bytes
		if (BYTE_COUNT > Integer.MAX_VALUE)
		{
			// divide the file size by the max integer size
			// and round up to obtain the number of split bytes
			bytePasses = (int) Math.ceil(BYTE_COUNT / Integer.MAX_VALUE);
			
			// set a new count
			count = new int[bytePasses];
			
			// set the new byte count to match the greatest int value allowed
			for (int n = 0; n < bytePasses; n++)
			{
				// check if n has incremented to the last iteration
				// if not the count is the maximum allowed
				// otherwise the count is the different between the set of values
				// and the max integer value
				if (n < (bytePasses - 1))
				{
					count[n] = Integer.MAX_VALUE;
				}
				else if (n == (bytePasses - 1))
				{
					count[n] = (int) (BYTE_COUNT - (Integer.MAX_VALUE * (bytePasses - 1)));
				}
			}
		}
		
		// CODE TO BEGIN READING THE BYTES
		try 
		{
			
			// initialize the file input stream to the current file
			in = new FileInputStream(file);
			
			if (in != null)
			{
				int byteOffset = 0;
				
				for (int i = 0; i < bytePasses; i++)
				{
					// checks if this has exceeded the first pass
					// setting the byte starting offset to match the
					// next byte in the file from where the previous pass left
					if (i > 0)
					{
						byteOffset = (currentPass * Integer.MAX_VALUE) + 1;
					}
					
					// create a new byteArray buffer equal to the number of bytes in the current count
					byte[] buffer = new byte[count[i]];
					
					// create the int for storing the amount of bytes already read
					int bytesRead = 0;
					
					// while the number of bytes read is less than the max byte count
					while(bytesRead < count[i])
					{
						try {
							
							// read the bytes and capture the amount of bytes read
							bytesRead += in.read(buffer, byteOffset, count[i]);
							
						} catch (IOException e)
						{
							e.printStackTrace();
							
							Log.i("IO Error", "Error Reading Bytes Into Buffer");
							
						}
					}	// end while loop
					
					// add the read byte array into the master byte array
					masterBytes.add(buffer);
					
				}	// end for loop
			}
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			
			Log.i("IO Error", "File Not Found");
		}
		finally 
		{
			
			try 
			{
				
				in.close();
				
			} catch (IOException e) 
			{
				e.printStackTrace();
				
				Log.i("IO Error", "Error Closing Input Stream");
			}
		}
		
		return masterBytes;
	}
	
	// method for encoding a byte array into a base64 string
		@Override
		public String encodeToBaseString(byte[] encodedBytes)
		{
			// encode the bytes into base64 String
			String encodedString = Base64.encodeToString(encodedBytes, Base64.DEFAULT);
			
			return encodedString;
		}

		
		// method for decoding an encoding base64 string back into a byte array
		@Override
		public byte[] decodeBaseString(String codedString)
		{
			// decode the base64 string back into byte array
			byte[] decodedString = Base64.decode(codedString, Base64.DEFAULT);
			
			return decodedString;
		}
		
		/** Method for retrieving Keys from the JSON wrapper*/
		public Object[] unWrapKeys(JSONObject jsonWrappedKeys)
		{
			Object[] keys = null;
			
			if (jsonWrappedKeys != null)
			{
				try {
					
					// get the encryption keys from their json wrapper
					Key publicKey = (Key) jsonWrappedKeys.get(DataSetup.PUBLIC_KEY);
					Key privateKey = (Key) jsonWrappedKeys.get(DataSetup.PRIVATE_KEY);
					SecretKey secretKey = (SecretKey) jsonWrappedKeys.get(DataSetup.SECRET_KEY);
					
					// create an object array to return the keys in
					keys = new Object[]{publicKey, privateKey, secretKey};
					
				} catch (JSONException e) {
					e.printStackTrace();
					
					Log.i("JSON Error", "Error Retrieving Keys From JSON");
				}
			}
			
			return keys;
		}
		
		/** Method for wrapping the keys in json for storing remotely*/
		public JSONObject getWrappedKeys(Key[] values)
		{	
			// create a new json object
			JSONObject wrapper = new JSONObject();
			
			String[] keys = new String[]{DataSetup.PUBLIC_KEY, 
										 DataSetup.PRIVATE_KEY, 
										 DataSetup.SECRET_KEY};
			
			try {
				
				for (int i = 0; i < keys.length; i++)
				{
					// wrap the keys in a json object
					wrapper.put(keys[i], values[i]);
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
				
				Log.i("JSON Error", "Error Wrapping Keys In JSON");
			}
			
			return wrapper;
		}
	
}
