package com.randerson.support;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import libs.FileSystem;
import libs.UniArray;

import com.randerson.interfaces.EncryptionSetup;

public class HidNCipher implements EncryptionSetup {

	// class fields for AES encryption
	private SecretKeySpec KEY_SPEC = null;
	private KeyGenerator KEY_GEN = null;
	private SecureRandom RANDOMIZER = null;
	
	// class fields for RSA encryption
	private Key PUBLIC_KEY = null;
	private Key PRIVATE_KEY = null;
	private KeyPair KEY_PAIR = null;
	private KeyPairGenerator KEY_PAIR_GEN = null;
	
	// key map file
	private UniArray KEYS = null;
	
	// save key boolean to determine whether the keys should be stored or not
	// if no keys are stored new keys should be saved otherwise non should resave
	public boolean SHOULD_SAVE_KEYS = true;
	
	// application context
	public Context CONTEXT = null;
	
	public HidNCipher(Context context, String algorithmType)
	{
		CONTEXT = context;
		
		// setup the class to use the specific encryption algorithms
		setupCipher();
	}

	@Override
	public String encodeData(int encryptionMethod, byte[] objectBytes)
	{
		// string for encoded data to return
		String encodedData = null;
		
		// create a cipher for encrypting
		Cipher cipher = null;
		
		// create byte array of encoded bytes
		byte[] codedBytes = null;
		
		if (encryptionMethod == AES_ENCRYPTION)
		{
			try {
				
				// get an instance of the cipher with specified algorithm
				// and initialize it with specific key for encryption
				cipher = Cipher.getInstance(AES_ALGORITHM);
				cipher.init(Cipher.ENCRYPT_MODE, KEY_SPEC);
				
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
		else if (encryptionMethod == RSA_ENCRYPTION)
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
		}
		
		return encodedData;
	}

	@Override
	public byte[] decodeData(int encryptionMethod, String encodedData)
	{
		// create a cipher for encrypting
		Cipher cipher = null;
		
		// byte array of decoded data to return
		byte[] decodedData = null;
		
		// create byte array of decoded bytes
		byte[] encodedBytes = decodeBaseString(encodedData);
		
		if (encryptionMethod == AES_ENCRYPTION)
		{
			
			try {
				
				// get an instance of the cipher with specified algorithm
				// and initialize it with specific key for encryption
				cipher = Cipher.getInstance(AES_ALGORITHM);
				cipher.init(Cipher.DECRYPT_MODE, KEY_SPEC);
				
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
		else if (encryptionMethod == RSA_ENCRYPTION)
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
		}
		
		return decodedData;
	}

	
	// method for setting up class for encryption / decryption
	@SuppressLint("TrulyRandom")
	@Override
	public void setupCipher()
	{
		// SETTING UP THE AES ALGORITHM SIDE
			try {
				
				// create with default constructor for strongest instance cryptographic provider
				RANDOMIZER = new SecureRandom();
				
				// create generator for specified algorithm
				// and get an instance with supplied randomness
				KEY_GEN = KeyGenerator.getInstance(AES_ALGORITHM);
				KEY_GEN.init(128, RANDOMIZER);
				
				// set the secret key specialization
				KEY_SPEC = new SecretKeySpec(KEY_GEN.generateKey().getEncoded(), AES_ALGORITHM);
				
			} catch (NoSuchAlgorithmException e) {
				
				e.printStackTrace();
				Log.i("Key Spec Error", "Algorithm Not Found");
			}
		
		
		
		// SETTING UP THE RSA ALGORITHM SIDE
			try {
				
				// create generator for specified algorithm
				// and get an instance with supplied randomness
				KEY_PAIR_GEN = KeyPairGenerator.getInstance(RSA_ALGORITHM);
				KEY_PAIR_GEN.initialize(1024);
				
				// generate the key pairs
				KEY_PAIR = KEY_PAIR_GEN.generateKeyPair();
				
				// get the public and private keys
				PRIVATE_KEY = KEY_PAIR.getPrivate();
				PUBLIC_KEY = KEY_PAIR.getPublic();
				
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				
				Log.i("Key Pair Error", "Algorithm Not Found");
			}
			
			// load the stored keys
			loadStoredKeys();
			
			// save the keys
			storeCipherKeys();
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
	
	public void loadStoredKeys()
	{
		// check for previously created keys to overwrite the newly generated keys
		KEYS = (UniArray) FileSystem.readObjectFile(CONTEXT, "keys.hdn", true);
		
		if (KEYS != null)
		{
			// check if the saved data has the public and private keys saved
			// if so, update the class fields to use those keys
			if (KEYS.hasObject("publicKey") && KEYS.hasObject("privateKey"))
			{
				PUBLIC_KEY = (Key) KEYS.getObject("publicKey");
				PRIVATE_KEY = (Key) KEYS.getObject("privateKey");
			}
			
			// check if the saved data has the secret key saved
			// if so, update the class field to use that key
			if (KEYS.hasObject("keySpec"))
			{
				KEY_SPEC = (SecretKeySpec) KEYS.getObject("keySpec");
			}
			
			// reset the key save bool
			SHOULD_SAVE_KEYS = false;
		}
	}

	public void storeCipherKeys()
	{
		// check if the keys UniArray is initialized or not
		// if not initialize it
		if (KEYS == null)
		{
			KEYS = new UniArray();
		}
		
		// verify that the keys should be stored
		if (SHOULD_SAVE_KEYS)
		{
			KEYS.putObject("publicKey", PUBLIC_KEY);
			KEYS.putObject("privateKey", PRIVATE_KEY);
			KEYS.putObject("keySpec", KEY_SPEC);
		}
	}
	
}
