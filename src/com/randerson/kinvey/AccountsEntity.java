package com.randerson.kinvey;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class AccountsEntity extends GenericJson 
{
	@Key("_id")
	public String id;
	
	@Key("pin")
	public String pin;
	
	@Key("firstName")
	public String firstName;
	
	@Key("lastName")
	public String lastName;
	
	@Key("secretKey")
	public String secretKey;
	
	public AccountsEntity(){};
}
