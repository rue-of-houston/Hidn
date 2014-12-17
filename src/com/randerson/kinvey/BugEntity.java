package com.randerson.kinvey;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class BugEntity extends GenericJson
{

	@Key("content")
	public String content;
	
	@Key("user")
	public String user;
	
	public BugEntity(){};
}
