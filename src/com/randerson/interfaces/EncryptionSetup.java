package com.randerson.interfaces;

public interface EncryptionSetup {

	public int RSA_ENCRYPTION = 100;
	public int AES_ENCRYPTION = 200;
	public String RSA_ALGORITHM = "RSA";
	public String AES_ALGORITHM = "AES";
	
	public String encodeData(int encryptionMethod, byte[] objectBytes);
	
	public byte[] decodeData(int encryptionMethod, String encodedData);
	
	public void setupCipher();
	
	public String encodeToBaseString(byte[] encodedBytes);
	
	public byte[] decodeBaseString(String codedString);
	
}
