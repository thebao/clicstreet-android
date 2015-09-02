package com.katacult.clicstreet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class SelectActivity extends Activity {

	Button mQuit;
	Button mFav;
	Button mFlash;
	String userName;
	String userName2;
	Dialog dialog;
	Boolean dialogShown=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getIntent().getExtras() != null){
	    	Intent intent = getIntent();
	    	userName = intent.getStringExtra("user");
	    	userName2 = intent.getStringExtra("secret");
			}
		setContentView(R.layout.activity_select);
		mQuit = (Button) findViewById(R.id.quitBtn);
		mFlash = (Button) findViewById(R.id.flashBtn);
		mFav = (Button) findViewById(R.id.favBtn);
		
	    mFlash.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	Intent in=new Intent(getApplicationContext(),UserActivity.class);
	    		in.putExtra("user", userName);
	    		in.putExtra("secret", userName2);
	    		in.putExtra("mode", "flash");
	    		startActivity(in);
        }});
	    mFav.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	Intent in=new Intent(getApplicationContext(),UserActivity.class);
	    		in.putExtra("user", userName);
	    		in.putExtra("secret", userName2);
	    		in.putExtra("mode", "fav");
	    		startActivity(in);
        }});
	    mQuit.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		shareDialog();}});



  

}
public void shareDialog(){

	AlertDialog.Builder alert=new AlertDialog.Builder(this);
	alert.setTitle("Clicstreet");
	alert.setMessage("Etes-vous sûr de vouloir de quitter?");
	alert.setPositiveButton("Non", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog,int id) {
			// if this button is clicked, close
			dialog.dismiss();
			//LoginActivity.this.finish();
		}});
	alert.setNegativeButton("Oui", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog,int id) {
			// if this button is clicked, close

			deletePreferences("storedName");

			Intent intent = new Intent("CLOSE_ALL");
			sendBroadcast(intent);
			finish();
			
			//LoginActivity.this.finish();
		}});
	dialog=alert.show();
	dialogShown=true;
}
private void deletePreferences(String key) {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    Editor editor = sharedPreferences.edit();
    editor.remove(key);
    editor.commit();
}
}