package com.randerson.kinvey;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class NewsEntity extends GenericJson
{

	@Key("_id")
	public String id;
	
	@Key("content")
	public String content;
	
	@Key("author")
	public String author;
	
	public NewsEntity(){};
}
