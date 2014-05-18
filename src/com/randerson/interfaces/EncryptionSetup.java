package com.randerson.interfaces;


public interface EncryptionSetup {

	public final int PUBLIC_KEY_TYPE = 1;
	public final int PRIVATE_KEY_TYPE = 2;
	public final int SECRET_KEY_TYPE = 3;
	
	public final int RSA_ENCRYPTION = 100;
	public final int AES_ENCRYPTION = 200;
	public final String RSA_ALGORITHM = "RSA";
	public final String AES_ALGORITHM = "AES";
	
	public String encodeData(byte[] objectBytes);
	public byte[] decodeData(String encodedData);
	public String encodeToBaseString(byte[] encodedBytes);
	public byte[] decodeBaseString(String codedString);
	
}
