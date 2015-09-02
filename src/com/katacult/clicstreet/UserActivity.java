package com.katacult.clicstreet;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Space;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserActivity extends Activity {

  String userName;
  String userName2;
  String mode;
  LinearLayout mFlashBox;
  LinearLayout mSubBox;
  Button mShowAllFlashes;
  Button mPickHome;
  TextView mLastFlash;
  TextView mListSubs;
private static final String LOG_TAG = "CommerceActivity";	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		if (getIntent().getExtras() != null){
	    	Intent intent = getIntent();
	    	userName = intent.getStringExtra("user");
	    	userName2 = intent.getStringExtra("secret");
	    	mode = intent.getStringExtra("mode");
			}
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


		mFlashBox = (LinearLayout) findViewById(R.id.flashBox);
		mSubBox = (LinearLayout) findViewById(R.id.subBox);
		mLastFlash = (TextView) findViewById(R.id.lastFlash);
		mListSubs = (TextView) findViewById(R.id.listSubs);
		mFlashBox.setVisibility(View.GONE);
		mLastFlash.setVisibility(View.GONE);
		mListSubs.setVisibility(View.GONE);
		mSubBox.setVisibility(View.GONE);
		if (mode.equals("flash")){
			mFlashBox.setVisibility(View.VISIBLE);
			mLastFlash.setVisibility(View.VISIBLE);
		}
		else
		{	mSubBox.setVisibility(View.VISIBLE);
			mListSubs.setVisibility(View.VISIBLE);}
	    new Thread(new Runnable() {
	    	public void run() {
	    		try {
	    			retrieveAndAddCities();
	    		} 
	    		catch (IOException e) {
	    			Log.e(LOG_TAG, "Cannot retrieve user info", e);
	    			return;
	    		}
	    	}
	    	}).start();
		
	}

	  protected void retrieveAndAddCities() throws IOException {
		  
		  HttpURLConnection conn = null;
		  final StringBuilder json = new StringBuilder();
		  try {
			  // Connect to the web service
			  URL url = new URL("http://www.clicstreet.com/api/mobile/flashes.json?_user="+userName+"&_secret="+userName2);
			  
			  conn = (HttpURLConnection) url.openConnection();
			  InputStreamReader in = new InputStreamReader(conn.getInputStream());
			   
			  // Read the JSON data into the StringBuilder
			  int read;
			  char[] buff = new char[1024];
				  while ((read = in.read(buff)) != -1) {
				  json.append(buff, 0, read);
				  }
		  } catch (IOException e) {
			  Log.e(LOG_TAG, "Error connecting to service", e);
			  //throw new IOException("Error connecting to service", e);
		  } finally {
			  if (conn != null) {
				  conn.disconnect();
				  
			  }
		  }
		  //System.out.println(json.toString());
		  // Create markers for the city data.
		  // Must run this on the UI thread since it's a UI operation.
		  runOnUiThread(new Runnable() {
			  public void run() {
				  try {
					  createMarkersFromJson(json.toString());
				  } 
				  catch (JSONException e) {
					  Log.e(LOG_TAG, "Error processing JSON: "+json.toString(), e);
				  }
	  		}
		  });
		  }
	  


		  void createMarkersFromJson(String json) throws JSONException {
			  JSONObject jsonObj =  new JSONObject(json);
			  JSONObject jsonUsr = jsonObj.getJSONObject("user");
			  JSONArray jsonSubs = jsonObj.getJSONArray("followedPlaces");
			  final String[] followedPlaces = new String[jsonSubs.length()];

			  final String[] followedPlacesId = new String[jsonSubs.length()];
			  final ImageView[] followedStar = new ImageView[jsonSubs.length()];
			  final LinearLayout[] followedLines = new LinearLayout[jsonSubs.length()];
			  final LinearLayout[] removeWrapper = new LinearLayout[jsonSubs.length()];
			  final Button[] followedButton = new Button[jsonSubs.length()];
			  final ImageButton[] removeButton = new ImageButton[jsonSubs.length()];
			  final ImageButton[] routeButton = new ImageButton[jsonSubs.length()];
			  LayoutParams params = new LayoutParams(
				        LayoutParams.WRAP_CONTENT,       
				        LayoutParams.WRAP_CONTENT
				);
			  params.setMargins(2, 2, 2, 2);
			  LayoutParams params2 = new LayoutParams(
				        LayoutParams.FILL_PARENT,      
				        LayoutParams.WRAP_CONTENT
				);
			  LayoutParams params3 = new LayoutParams(
				        90,      
				        LayoutParams.MATCH_PARENT
				);
			  //params2.setMargins(2, 2, 2, 12);
			  for (int i = 0; i < jsonSubs.length(); i++) {
				  followedLines[i] = new LinearLayout(this);

				  ((LinearLayout) mSubBox).addView(followedLines[i]);

				  followedLines[i].setLayoutParams(params2);
				  followedLines[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.droppedf2));
				  Locale fr = new Locale("fr_Fr");
				  followedPlaces[i]=jsonSubs.getJSONObject(i).getString("name").toUpperCase(fr);
				  if (followedPlaces[i].length()>23)
				  	{followedPlaces[i]=followedPlaces[i].substring(0,19)+" ...";}
				  followedPlacesId[i]=jsonSubs.getJSONObject(i).getString("id");
				  final String placeId=followedPlacesId[i];
				  final Double followedLat=jsonSubs.getJSONObject(i).getDouble("latitude");
				  final Double followedLong=jsonSubs.getJSONObject(i).getDouble("longitude");
				  final String  clickedId=followedPlacesId[i];
				  final String  followedName=followedPlaces[i];
				  final String followedType=jsonSubs.getJSONObject(i).getString("summary");
				  final String followedIcon=jsonSubs.getJSONObject(i).getJSONObject("category").getString("marker_logo");
				  
				  followedStar[i] = new ImageView(this);
				  followedStar[i].setPadding(0, 8, 0, 0);
				  followedStar[i].setImageResource(R.drawable.comm_star_elec);
				  ((LinearLayout) followedLines[i]).addView(followedStar[i]);
				  
				  followedButton[i] = new Button(this);
				  followedButton[i].setText(followedPlaces[i]);
				  followedButton[i].setBackgroundDrawable(null);
				  followedButton[i].setTextColor(Color.parseColor("#b3b3b3"));
				  followedButton[i].setGravity(48);
				  followedButton[i].setOnClickListener(new View.OnClickListener() {
				    	 
						@Override
						public void onClick(View arg0) {
						  	Intent in=new Intent(getApplicationContext(),CommerceActivity.class);
					  		in.putExtra("kshopid", clickedId);
				  			in.putExtra("user", userName);
				  			in.putExtra("secret", userName2);
				  			startActivity(in);}});

				  ((LinearLayout) followedLines[i]).addView(followedButton[i]);
				  followedButton[i].setLayoutParams(params);

				  removeWrapper[i] = new LinearLayout(this);
				  removeWrapper[i].setPadding(0, 0, 0, 8);
				  removeWrapper[i].setGravity(Gravity.RIGHT);
				  LayoutParams rmWr = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.MATCH_PARENT);
				  ((LinearLayout) followedLines[i]).addView(removeWrapper[i], rmWr);
				  
				  removeButton[i] = new ImageButton(this);
				  removeButton[i].setImageResource(R.drawable.gl_remove);
				  removeButton[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.button_even));
				  removeButton[i].setOnClickListener(new View.OnClickListener() {
				    	 
						@Override
						public void onClick(View arg0) {
							
							new Thread(new Runnable() {
				            	public void run() {
				            		unFollow(clickedId);
				            	}
				            	}).start();

							
						}});

				  ((LinearLayout) removeWrapper[i]).addView(removeButton[i], params3);
				  removeButton[i].setLayoutParams(params3);

				  followedLines[i].setLayoutParams(params2);
				  /*
				  removeButton[i] = new Button(this);
				  removeButton[i].setText("Désabonner");
				  removeButton[i].setPadding(0,0,0,0);
				  removeButton[i].setTextAppearance(this, android.R.style.TextAppearance_Small);
				  removeButton[i].setTextColor(Color.parseColor("#FFFFFF"));
				  removeButton[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.orange_button));
				  removeButton[i].setGravity(17);

				  ((LinearLayout) mSubBox).addView(removeButton[i]);
				  removeButton[i].setLayoutParams(params2);*/
				  
			  }
			  final JSONArray mNews = jsonObj.getJSONArray("flashes");
			  final ImageView[] flashIcon = new ImageView[mNews.length()];
			  final LinearLayout[] lineWrap = new LinearLayout[mNews.length()];
			  final TextView[] textNewsViews = new TextView[mNews.length()];
			  final TextView[] dateNewsViews = new TextView[mNews.length()];
			  final Space[] spacerViews = new Space[mNews.length()];
			  final String[] flashDescr = new String[mNews.length()];
			  final Double[] rdm = new Double[mNews.length()];
			  for (int i = 0; i < mNews.length(); i++) {
				  JSONObject jsonNews = mNews.getJSONObject(i);
				  LayoutParams linePars = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				  lineWrap[i] = new LinearLayout(this);
				  lineWrap[i].setGravity(Gravity.CENTER_VERTICAL);	
				  lineWrap[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.droppedf2));
				  ((LinearLayout) mFlashBox).addView(lineWrap[i], linePars);
				  dateNewsViews[i] = new TextView(this);
				  Date date;
				try {
					date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE).parse(jsonNews.getString("created_at"));
					SimpleDateFormat dateOutput = new SimpleDateFormat("dd MMMM à HH:mm", Locale.FRANCE);
				  dateNewsViews[i].setText(jsonNews.getJSONObject("place").getString("name")+" - Le "+dateOutput.format(date));
				
				} catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}/*
				  dateNewsViews[i].setPadding(4, 4, 4, 4);
				  dateNewsViews[i].setTextColor(Color.parseColor("#FFFFFF"));
				  dateNewsViews[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.green_button_nobottom));
				  dateNewsViews[i].setGravity(17);
				  dateNewsViews[i].setTypeface(dateNewsViews[i].getTypeface(), Typeface.BOLD);
				  dateNewsViews[i].setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));*/

				  flashIcon[i] = new ImageView(this);
				  
				  flashIcon[i].setPadding(0, 0, 12, 0);
				  
				  rdm[i] = Math.random();
				  if (rdm[i]<0.5)
				  {
					  flashIcon[i].setImageResource(R.drawable.flash_blue_nobg);
				  }
				  else
				  {
					  flashIcon[i].setImageResource(R.drawable.flash_gray_nobg);					  
				  }
				  ((LinearLayout) lineWrap[i]).addView(flashIcon[i]);
				  
				  textNewsViews[i] = new TextView(this);
				  flashDescr[i]=Html.fromHtml(jsonNews.getString("description")).toString();
				  if (flashDescr[i].length()>80)
				  	{flashDescr[i]=flashDescr[i].substring(0,77)+" ...";}

				  textNewsViews[i].setText(flashDescr[i]);
				  textNewsViews[i].setPadding(16, 16, 16, 16);
				  textNewsViews[i].setTextColor(Color.parseColor("#b3b3b3"));
				  textNewsViews[i].setGravity(Gravity.LEFT);
				  textNewsViews[i].setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
				  
				  ((LinearLayout) lineWrap[i]).addView(textNewsViews[i]);
				  
				  
				  spacerViews[i] = new Space(this);
				  spacerViews[i].setPadding(4, 4, 4, 0);
				  spacerViews[i].setMinimumHeight(10);
				 /* buffer.append("<tr><td>");
				  buffer.append(jsonCat.getString("name"));
				  buffer.append("</td><td>");
				  buffer.append(jsonCat.getJSONObject("price").getInt("amount")/100);
				  buffer.append("&euro;</td></tr>");*/
			  }
		
		

		  }
		  private void unFollow(String placeId){

			     DefaultHttpClient httpclient2 = new DefaultHttpClient();
			     HttpPost httppost = new HttpPost("http://www.clicstreet.com/api/mobile/unfollow?place="+placeId+"&_user="+userName+"&_secret="+userName2);

			     HttpResponse httpResponse = null;
			     try {httpResponse = httpclient2.execute(httppost);

			     String responseContent = EntityUtils.toString(httpResponse.getEntity());
			     System.out.println(responseContent );
			     }
			     catch (IOException e) {}
			     finally {
					finish();
					startActivity(getIntent());}
		  }

			private void savePreferences(String key, Integer value) {
		        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		        Editor editor = sharedPreferences.edit();
		        editor.putInt(key, value);
		        editor.commit();
		    }

}
