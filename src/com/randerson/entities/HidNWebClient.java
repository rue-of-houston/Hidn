package com.randerson.entities;

import android.webkit.WebView;

public class HidNWebClient extends android.webkit.WebViewClient {

	public HidNWebClient() {}
	
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		//return super.shouldOverrideUrlLoading(view, url);
		
		return false;
	}
}
