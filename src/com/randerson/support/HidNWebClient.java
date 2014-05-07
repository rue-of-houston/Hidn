package com.randerson.support;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.widget.EditText;

public class HidNWebClient extends android.webkit.WebViewClient {

	public EditText URL_FIELD;
	
	public HidNWebClient(EditText urlField) 
	{
		// pass a reference to the url field
		URL_FIELD = urlField;
	}
	
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		
		
		// override the super method to avoid redirecting traffic to
		// device browser
		
		return false;
	}
	
	@Override
	public void onReceivedLoginRequest(WebView view, String realm,
			String account, String args) {
		super.onReceivedLoginRequest(view, realm, account, args);
	}
	
	@Override
	public void onLoadResource(WebView view, String url) {
		super.onLoadResource(view, url);
	}
	
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		super.onPageStarted(view, url, favicon);
	}
	
	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		
		// update the url field with the url string that is loaded
		URL_FIELD.setText(url);
	}
}
