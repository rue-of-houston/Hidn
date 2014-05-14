package com.randerson.interfaces;

public interface EncryptionSetup {

	public final int RSA_ENCRYPTION = 100;
	public final int AES_ENCRYPTION = 200;
	public final String RSA_ALGORITHM = "RSA";
	public final String AES_ALGORITHM = "AES";
	
	public String encodeData(byte[] objectBytes);
	public byte[] decodeData(String encodedData);
	public String encodeToBaseString(byte[] encodedBytes);
	public byte[] decodeBaseString(String codedString);
	
}
