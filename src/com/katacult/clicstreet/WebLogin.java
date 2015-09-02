package com.katacult.clicstreet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;


public class WebLogin extends Activity {
 
	private WebView webView;
	String sessionId = null;
	String sessionId2 = null;
	public Button mGuestBtn;
	public String cookie = null;
	public Dialog dialog;
	public Boolean dialogShown =false;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_login);
		checkConnection();
 
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    // Check if the key event was the Back button and if there's history
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
	        webView.goBack();
	        return true;
	    }
	    // If it wasn't the Back key or there's no web page history, bubble up to the default
	    // system behavior (probably exit the activity)
	    return super.onKeyDown(keyCode, event);
	}

	public void checkConnection(){
		if (isOnline()){
			if (dialogShown)
			{
				dialog.dismiss();
				dialogShown=false;
			}else{}

		//CookieSyncManager.createInstance(getApplicationContext());

		if (sessionId!=null){
		
			
			
		}	 
		webView = (WebView) findViewById(R.id.webView1);
		mGuestBtn = (Button) findViewById(R.id.guestBtn);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);

		webView.setWebViewClient(new MyWebViewClient());
		webView.loadUrl("http://www.clicstreet.com/login");
		onPageFinished(webView, "http://www.clicstreet.com/login");
		String cookies = CookieManager.getInstance().getCookie("http://www.clicstreet.com/login");
		System.out.println("All the cookies in a string:" + cookies);
		
		
		mGuestBtn.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	Intent myIntent = new Intent(WebLogin.this, MainActivity.class);
				myIntent.putExtra("key", ""); //Optional parameters
				myIntent.putExtra("con", false);
				WebLogin.this.startActivity(myIntent);
	        }});}
		else {
			alertConnection();
		}
	}
	@Override
	public void onResume(){
		super.onResume();
		checkConnection();
	    // put your code here...

	}
	public void alertConnection(){

		AlertDialog.Builder alert=new AlertDialog.Builder(this);
		alert.setTitle("Connexion inexistante");
		alert.setMessage("Vous n'êtes pas connecté à Internet!\nVeuillez vérifier vos réglages.");
		alert.setIcon(R.drawable.ic_launcher);
		alert.setPositiveButton("Quitter", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, close
				// current activity
				WebLogin.this.finish();
			}});
		alert.setNegativeButton("Réglages", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, close
				// current activity
				startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
				WebLogin.this.finish();
			}});
		dialog=alert.show();
		dialogShown=true;
		
	}
	
	
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	public void onPageFinished(WebView webView, String url) {
	       //CookieSyncManager.getInstance().sync();
	       // Get the cookie from cookie jar.
	       cookie = CookieManager.getInstance().getCookie(url);
	       if (cookie == null) {
	    	   sessionId="bleble";
	         return;
	       }

    	   // sessionId=cookie;
	       // Cookie is a string like NAME=VALUE [; NAME=VALUE]
	       String[] pairs = cookie.split(";");
	       for (int i = 0; i < pairs.length; ++i) {
	         String[] parts = pairs[i].split("=", 2);
	         
	         // If token is found, return it to the calling activity.
	         for (int j = 0; j < parts.length; ++j) {
	         if (parts[0].equals("__cfduid")) {
	           sessionId=(parts[1]);
	           }
	         if (parts[0].equals(" PHPSESSID") || parts[0].equals("PHPSESSID")) {
		           sessionId2=(parts[1]);
		           }
	         }
	       }
	     }
	private class MyWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        if (	Uri.parse(url).toString().equals("http://www.clicstreet.com/login") ||
	        		Uri.parse(url).toString().equals("http://www.clicstreet.com/register/")
	        	) {

	            // This is my web site, so do not override; let my WebView load the page
	            return false;
	        }
	        else if (	
	        		Uri.parse(url).toString().equals("http://www.clicstreet.com/login") || 
	        		Uri.parse(url).toString().equals("http://www.clicstreet.com/register/")
	        	)
	        {
	            return false;}
	        	
	        	else if (	
		        		Uri.parse(url).toString().equals("http://www.clicstreet.com/login_check"))
	        {
	        		onPageFinished(webView, "http://www.clicstreet.com/login_check");
		            return true;
	        		
	        	
	        	
	        	
	        	
	        }
	        // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
	        /*Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	        startActivity(intent);
	        return true;*/
	        Intent myIntent = new Intent(WebLogin.this, MainActivity.class);
			myIntent.putExtra("key", sessionId); //Optional parameters
			myIntent.putExtra("key2", sessionId2);
			//myIntent.putExtra("con", true);
			myIntent.putExtra("cookie", cookie);
    		if (sessionId!=null && sessionId2!=null){
			WebLogin.this.startActivity(myIntent);}
	    	return true;
	    }
	    
	}
	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	   switch (requestCode) {
	     case 0:
	       if (resultCode != RESULT_OK || data == null) {
	    	   sessionId="blpblp";
	         return;
	       }
	       // Get the token.
	       String token = data.getStringExtra("token");
	       if (token != null) {
	         /* Use the token to access data */
	    	  sessionId = token; 
	       }
	       return;
	   }
	   super.onActivityResult(requestCode, resultCode, data);
	 }/*
	 @Override
	 protected void onPause() {
	   super.onPause();
	   CookieSyncManager.getInstance().stopSync();
	 }

	 @Override
	 protected void onResume() {
	   super.onResume();
	   CookieSyncManager.getInstance().startSync();
	 }*/
 
}