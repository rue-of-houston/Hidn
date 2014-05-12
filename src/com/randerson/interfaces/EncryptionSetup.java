package com.randerson.interfaces;

public interface EncryptionSetup {

	public int RSA_ENCRYPTION = 100;
	public int AES_ENCRYPTION = 200;
	public String RSA_ALGORITHM = "RSA";
	public String AES_ALGORITHM = "AES";
	
	public String encodeData(byte[] objectBytes);
	public byte[] decodeData(String encodedData);
	public String encodeToBaseString(byte[] encodedBytes);
	public byte[] decodeBaseString(String codedString);
	
}
