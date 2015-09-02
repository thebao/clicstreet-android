package com.katacult.clicstreet;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Space;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.tyczj.mapnavigator.Directions;
import com.tyczj.mapnavigator.Navigator;
import com.tyczj.mapnavigator.Navigator.OnPathSetListener;
import com.tyczj.mapnavigator.Route;



public class CommerceActivity extends FragmentActivity implements LocationListener {
	private GoogleMap map;

	public TextView mId;
	public TextView mName;
	public TextView mDesc;
	public TextView mType;
	public TextView mTest;
	public TextView mAdresse;
	public TextView mAmTimes;
	public TextView mOuvert;
	public TextView mPmTimes;
	public ImageView mCover;
	public ImageView mLogo;
	public TextView mTextPropos;
	public TextView mTextFlashs;
	public TextView mDaysTimes; 
	public Drawable image;
	public String mCoverUrl;
	public String mLogoUrl;
	public String mColPri;
	public String mColSec;
	public String mTestString;
	public Button mPhone;
	public Button mDateBtn;
	public Button mShareBtn;
	public Button mButtonTime;
	public Button mFollowBtn;
	public Button mFollowAppBtn;
	public Button mFollowEmailBtn;
	public Button mFollowSmsBtn;
	public ImageButton mFacebookBtn;
	public ImageButton mTwitterBtn;
	public ImageButton mEmailBtn;
	public ArrayList mSelectedItems = new ArrayList();
	public ArrayList mShareLinks = new ArrayList();
	public Dialog dialog;
	public Boolean dialogShown=false;
	public String mColBak;
	public View mBody;
	public LinearLayout mCatBox;
	public LinearLayout mFlashBox;
	public LinearLayout mDateBar;
	public LinearLayout mShareBar;
	public LinearLayout mLeftBox;
	public LinearLayout mRightBox;
	public LinearLayout mFollowBar;
	public LinearLayout mBigBar;
	public LinearLayout mLoadStatus;
	public LinearLayout mWrapper;
	public String mFacebookLink;
	public String mTwitterLink;
	public String mEmailSub;
	public String mAppString;
	String userName;
	String userName2;
	String shopId;
	String textDesc;
	String miniText;
	int dayOfWeek;
	public String makeUrl;
	int totalMinutes;
	public Double followedLat;
	public Double followedLong;
	public String followedName;
	public String followedType;
	public String icon;
	public String followedIcon; 
	public Integer locoMotion;
	public Integer locoMotionGet;
	public Boolean traceRoute=false;
	public LatLng coords;
	public Boolean beenMade=false;
	public Long rightNow;

	public Boolean isApp=true;
	public Boolean isEmail=true;
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute = 1000 * 60 * 1; 


	protected LocationManager locationManager;
	// flag for GPS status
	boolean isGPSEnabled = false;

	// flag for network status
	boolean isNetworkEnabled = false;

	boolean canGetLocation = false;

	Location location; // location
	double latitude; // latitude
	double longitude; // longitude

  Boolean cutText=false;
	public String mEmailBod;
    public HashMap<Integer, String> mDays = new HashMap<Integer, String>();
	
	public static class Typefaces {
		private static final String TAG = "Typefaces";

		static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

		public static Typeface get(Context c, String assetPath) {
			synchronized (cache) {
				if (!cache.containsKey(assetPath)) {
					try {
						Typeface t = Typeface.createFromAsset(c.getAssets(),
								assetPath);
						cache.put(assetPath, t);
					} catch (Exception e) {
						Log.e(TAG, "Could not get typeface '" + assetPath
								+ "' because " + e.getMessage());
						return null;
					}
				}
				return cache.get(assetPath);
			}
		}
	}
/*
	  String userName;
	  String userName2;*/
	private static final String LOG_TAG = "CommerceActivity";
  @Override
  protected void onCreate(Bundle savedInstanceState) {

  //if it's a string you stored.
    super.onCreate(savedInstanceState);

	  Intent intent = getIntent();
	  	Bundle extras = intent.getExtras();
	  	shopId = intent.getStringExtra("kshopid");
    	userName = intent.getStringExtra("user");
    	userName2 = intent.getStringExtra("secret");
    	if (extras.containsKey("locoMotion")){
    		locoMotionGet = intent.getIntExtra("locoMotion", 12);
    		traceRoute=true;
    	};
    		
    			
	  	/*
    	userName = intent.getStringExtra("key");
    	userName2 = intent.getStringExtra("key2");*/
    setContentView(R.layout.commerce_view);
    /*CookieManager cookieManager = CookieManager.getInstance();
    cookieManager.setCookie("http://www.clicstreet.com", "__cfduid=d0d5dcdd3633e7eccb43e008def17f6a91385036071408");
    cookieManager.setCookie("http://www.clicstreet.com", "PHPSESSID=238blcthqilps2nk88rpkd0h42");*/
    Calendar c = Calendar.getInstance();
    c.setTime(new Date());
    dayOfWeek = c.get(Calendar.DAY_OF_WEEK)-1;
    if (dayOfWeek==0)
    	dayOfWeek=7;
    int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
    int minuteOfDay = c.get(Calendar.MINUTE);
    totalMinutes = 60*hourOfDay+minuteOfDay;

    if (totalMinutes<300)
    {totalMinutes=totalMinutes+24*60;}
    rightNow = new Date().getTime();
    System.out.println("rightnow:"+rightNow);
/*
    Thread thread =  new Thread(null, viewOrders, "MagentoBackground");
    thread.start();
    m_ProgressDialog = ProgressDialog.show(CommerceActivity.this,    
          "Attendez un instant...", "Récupération des données...", true);
    */
    
    

	map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
	        .getMap();
	makeUrl = ("http://www.clicstreet.com/api/mobile/follow?place="+shopId+"&_user="+userName+"&_secret="+userName2);
	  mName = (TextView) findViewById(R.id.commName);
	  mDesc = (TextView) findViewById(R.id.commDesc);
	  mType = (TextView) findViewById(R.id.commSub);
	  mCover = (ImageView) findViewById(R.id.coverImg);
	  mLogo = (ImageView) findViewById(R.id.logoImg);
	  mTest = (TextView) findViewById(R.id.testHtml);
	  mAdresse = (TextView) findViewById(R.id.commAdd);/*
	  mAmTimes = (TextView) findViewById(R.id.timeAm);
	  mPmTimes = (TextView) findViewById(R.id.timePm);
	  mDaysTimes = (TextView) findViewById(R.id.timeDays);*/

	  mPhone = (Button) findViewById(R.id.buttonCall);
	  mDateBtn = (Button) findViewById(R.id.buttonTime);
	  mShareBtn = (Button) findViewById(R.id.shareBtn);
	  mFollowBtn = (Button) findViewById(R.id.followBtn);/*
	  mFollowAppBtn = (Button) findViewById(R.id.followAppBtn);
	  mFollowEmailBtn = (Button) findViewById(R.id.followEmailBtn);
	  mFollowSmsBtn = (Button) findViewById(R.id.followSmsBtn);*/
	  mPhone.setVisibility(View.INVISIBLE);
	  mTextPropos = (TextView) findViewById(R.id.textPropos);
	  mOuvert = (TextView) findViewById(R.id.commOuvert);
	  mTextFlashs = (TextView) findViewById(R.id.textFlashs);
	  mBody = (View) findViewById(R.id.commerce_form);
	  mCatBox = (LinearLayout) findViewById(R.id.catBox);
	  mFlashBox = (LinearLayout) findViewById(R.id.flashBox);
	  mLeftBox = (LinearLayout) findViewById(R.id.leftBox);
	  mRightBox = (LinearLayout) findViewById(R.id.rightBox);
	  mLoadStatus = (LinearLayout) findViewById(R.id.load_status);
	  mWrapper = (LinearLayout) findViewById(R.id.wrapper);/*
	  mShareBar = (LinearLayout) findViewById(R.id.shareBar);
	  mFollowBar = (LinearLayout) findViewById(R.id.followBar);*/
	  mBigBar = (LinearLayout) findViewById(R.id.bigBar);/*
	  mFacebookBtn = (ImageButton) findViewById(R.id.facebookBtn);
	  mTwitterBtn = (ImageButton) findViewById(R.id.twitterBtn);
	  mEmailBtn = (ImageButton) findViewById(R.id.emailBtn);*/
	  mButtonTime = (Button) findViewById(R.id.buttonTime);
	  


	  Typeface font = Typefaces.get(CommerceActivity.this, "fonts/nothingdo.ttf");  
	  mType.setTypeface(font);  /*
	  mFollowBar.setVisibility(View.INVISIBLE);
	  mFollowBar.setVisibility(View.VISIBLE);
	  mFollowBar.setVisibility(View.INVISIBLE);
	  mShareBar.setVisibility(View.INVISIBLE);
	  mShareBar.setVisibility(View.VISIBLE);
	  mShareBar.setVisibility(View.INVISIBLE);
	  mBigBar.setVisibility(View.INVISIBLE);*/
	  
	  
	  
	  
	  
    final Button button = (Button) findViewById(R.id.closeBtn);
    button.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	itineraryDialog();
        }});

   
   
    /*mFacebookBtn.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        		Intent internetIntent = new Intent(Intent.ACTION_VIEW,
        		Uri.parse(mFacebookLink));
		        internetIntent.setComponent(new ComponentName("com.android.browser","com.android.browser.BrowserActivity"));
		        internetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        startActivity(internetIntent);
          }});
    mTwitterBtn.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        		Intent internetIntent = new Intent(Intent.ACTION_VIEW,
        		Uri.parse(mTwitterLink));
		        internetIntent.setComponent(new ComponentName("com.android.browser","com.android.browser.BrowserActivity"));
		        internetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        startActivity(internetIntent);
          }});
    mEmailBtn.setOnClickListener(new View.OnClickListener() {
              public void onClick(View v) {
            	  Intent i = new Intent(Intent.ACTION_SEND);
            	  i.setType("text/html");
            	  i.putExtra(Intent.EXTRA_EMAIL  , new String[]{""});
            	  i.putExtra(Intent.EXTRA_SUBJECT, mEmailSub);
            	  i.putExtra(Intent.EXTRA_TEXT   , Html.fromHtml(mEmailBod));
            	  startActivity(Intent.createChooser(i, "Send mail..."));
              }});*//*
    mFollowEmailBtn.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	
        	
        	
        	
        	
        	
        }});
    mFollowSmsBtn.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
  		  	//String makeUrl = ("http://www.clicstreet.com/api/mobile/follow?place="+shopId+"&mail=false&app=true&sms=true&_user="+userName+"&_secret="+userName2);
  		  	mRunFollow task = new mRunFollow();
        	task.execute((Void) null);

          }});*/
    
    new Thread(new Runnable() {
    	public void run() {
    		try {
    			retrieveAndAddCities(shopId);
    		} 
    		catch (IOException e) {
    			Log.e(LOG_TAG, "Cannot retrieve cities", e);
    			return;
    		}
    	}
    	}).start();
    
    RelativeLayout mBar = (RelativeLayout) findViewById(R.id.drag_bar);
	findViewById(R.id.drag_bar).setOnTouchListener(new OnTouchListener()
	  {
	    int prevY;
	    @Override
	    public boolean onTouch(final View mBar,final MotionEvent event)
	      {
	      //final RelativeLayout.LayoutParams par=(RelativeLayout.LayoutParams)v.getLayoutParams();
	    	final View mBot = findViewById(R.id.botCont);	
	      final LinearLayout.LayoutParams par=(LinearLayout.LayoutParams) mBot.getLayoutParams();
	      switch(event.getAction())
	        {
	        case MotionEvent.ACTION_MOVE:
	          {
	        	  
	          par.height+=(int)event.getRawY()-prevY;
	          prevY=(int)event.getRawY();
	          if (par.height>2){
	          mBot.setLayoutParams(par);
	          return true;}
	          else{
	        	  par.height=2;
	  	          mBot.setLayoutParams(par);
	        	  return false;
	        	  
	          	}
	          }
	        case MotionEvent.ACTION_UP:
	          {
	          par.height+=(int)event.getRawY()-prevY;
	          if (par.height>2){
	          mBot.setLayoutParams(par);
	          return true;}
	          else{
	        	  par.height=2;
	  	          mBot.setLayoutParams(par);
	        	  return false;
	          }
	        	  
	        }
	        case MotionEvent.ACTION_DOWN:
	          {
	          prevY=(int)event.getRawY();
	          par.height-=event.getRawY()-prevY;
	          mBot.setLayoutParams(par);
	          return true;
	          }
	        }
	      return false;
	      }
	  });
    
    
  }
  
  
  
  
 /* 
  public class URLImageParser implements ImageGetter {
	    Context c;
	    View container;

	    public URLImageParser(View t, Context c) {
	        this.c = c;
	        this.container = t;
	    }

	    public Drawable getDrawable(String source) {
	        URLDrawable urlDrawable = new URLDrawable();

	        // get the actual source
	        ImageGetterAsyncTask asyncTask = 
	            new ImageGetterAsyncTask( urlDrawable);

	        asyncTask.execute(source);

	        // return reference to URLDrawable where I will change with actual image from
	        // the src tag
	        return urlDrawable;
	    }

	    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable>  {
	        URLDrawable urlDrawable;

	        public ImageGetterAsyncTask(URLDrawable d) {
	            this.urlDrawable = d;
	        }

	        @Override
	        protected Drawable doInBackground(String... params) {
	            String source = params[0];
	            return fetchDrawable(source);
	        }

	        @Override
	        protected void onPostExecute(Drawable result) {
	            // set the correct bound according to the result from HTTP call
	            urlDrawable.setBounds(0, 0, 0 + result.getIntrinsicWidth(), 0 
	                    + result.getIntrinsicHeight()); 

	            // change the reference of the current drawable to the result
	            // from the HTTP call
	            urlDrawable.drawable = result;

	            // redraw the image by invalidating the container
	            URLImageParser.this.container.invalidate();
	        }


	        public Drawable fetchDrawable(String urlString) {
	            try {
	                InputStream is = fetch(urlString);
	                Drawable drawable = Drawable.createFromStream(is, "src");
	                drawable.setBounds(0, 0, 0 + drawable.getIntrinsicWidth(), 0 
	                        + drawable.getIntrinsicHeight()); 
	                return drawable;
	            } catch (Exception e) {
	                return null;
	            } 
	        }

	        private InputStream fetch(String urlString) throws MalformedURLException, IOException {
	            DefaultHttpClient httpClient = new DefaultHttpClient();
	            HttpGet request = new HttpGet(urlString);
	            HttpResponse response = httpClient.execute(request);
	            return response.getEntity().getContent();
	        }
	    }
	}*/
  
  private ContextWrapper getContext() {
	// TODO Auto-generated method stub
	return null;
}

  protected void mTogVis(LinearLayout lay){
	  if(!lay.isShown()){
		  if(!mBigBar.isShown()){
			  mBigBar.setVisibility(View.VISIBLE);
			  lay.setVisibility(View.VISIBLE);
		  }
		  else
		  {lay.setVisibility(View.VISIBLE);}
		  
	  }
	  else {
		  lay.setVisibility(View.INVISIBLE);		  
	  }
	  if (!mFollowBar.isShown() && !mShareBar.isShown()){

		  mBigBar.setVisibility(View.INVISIBLE);
	  }
	  
  }
  protected void mTogText(){
	  if(cutText){
		  mDesc.setText(Html.fromHtml(textDesc));
		  cutText=false;
	  }
	  else {
		  mDesc.setText(Html.fromHtml(miniText));
		  cutText=true;
	  }
	  
  }


  private class mRunFollow extends AsyncTask<Void, Void, String> {
  	protected String doInBackground(Void... url) {
  	int code = 0;
	  
	  HttpURLConnection followConn = null;

	  HttpURLConnection conn = null;
      DefaultHttpClient client = new DefaultHttpClient();
	  try {
		     DefaultHttpClient httpclient2 = new DefaultHttpClient();
		     HttpPost httppost = new HttpPost("http://www.clicstreet.com/api/mobile/follow?_user="+userName+"&_secret="+userName2);
		     httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		     httppost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml");
		     List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		     nameValuePairs.add(new BasicNameValuePair("app", isApp.toString()));
		     nameValuePairs.add(new BasicNameValuePair("_user", userName));
		     nameValuePairs.add(new BasicNameValuePair("_secret", userName2));
		     nameValuePairs.add(new BasicNameValuePair("place", shopId));
		     nameValuePairs.add(new BasicNameValuePair("mail", isEmail.toString()));
		     nameValuePairs.add(new BasicNameValuePair("sms", "false"));
		     
		     try {httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));}
		     catch (UnsupportedEncodingException e) {}

		     
		     HttpResponse httpResponse = null;
		     String responseBody;
		     try {httpResponse = httpclient2.execute(httppost);
		  
		     HttpEntity resEntity = httppost.getEntity();
		     BufferedReader br = new BufferedReader(new InputStreamReader(
	                    resEntity.getContent(), "UTF-8"));
	            String line;
	            String result = "";
	            while (((line = br.readLine()) != null)) {              
	                result = result + line + "\n";
	            }

	            try {
					  createMarkersFromJson("["+result.toString()+"]");
				  } 
				  catch (JSONException e) {
				  }
	            System.out.println("Result"+result);

			
		 	
		     }
		     catch (IOException e) {}
		     //System.out.println("Can log in ? "+canLogIn);

			
			
			
			
			
			
			
		} finally {
		  if (followConn != null) {
			  followConn.disconnect();
			  
		  }}

	      return Integer.toString(code);}


      @Override
      protected void onPostExecute(String result) {
		finish();
		startActivity(getIntent());
      }
  }
  
  
  protected void mTogVis2(LinearLayout lay){
	  if(!lay.isShown()){
		  lay.setVisibility(View.VISIBLE);
	  }
	  else {
		  lay.setVisibility(View.GONE);		  
	  }
	  
  }
  
protected void retrieveAndAddCities(String shopId) throws IOException {
	  HttpURLConnection conn = null;
	  final StringBuilder json = new StringBuilder();
	  try {
		  // Connect to the web service
		   

		  URL url = new URL("http://www.clicstreet.com/api/mobile/place/"+shopId+".json?_user="+userName+"&_secret="+userName2);
		  conn = (HttpURLConnection) url.openConnection();
		  
		  //conn.setRequestProperty("Cookie", "PHPSESSID="+sessId);
		  //conn.setRequestProperty("Cookie", "PHPSESSID=gponnniimsvmsvpstmr8gk2rv4");
		  
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
	   
	  // Create markers for the city data.
	  // Must run this on the UI thread since it's a UI operation.
	  runOnUiThread(new Runnable() {
		  public void run() {
			  try {
				  createMarkersFromJson(json.toString());
			  } 
			  catch (JSONException e) {
				  Log.e(LOG_TAG, "Error processing JSON", e);
			  }
  		}
	  });
	  }

private class PhoneCallListener extends PhoneStateListener {
	 
	private boolean isPhoneCalling = false;

	String LOG_TAG = "LOGGING 123";

	@Override
	public void onCallStateChanged(int state, String incomingNumber) {

		if (TelephonyManager.CALL_STATE_RINGING == state) {
			// phone ringing
			Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
		}

		if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
			// active
			Log.i(LOG_TAG, "OFFHOOK");

			isPhoneCalling = true;
		}

		if (TelephonyManager.CALL_STATE_IDLE == state) {
			// run when class initial and phone call ended, 
			// need detect flag from CALL_STATE_OFFHOOK
			Log.i(LOG_TAG, "IDLE");

			if (isPhoneCalling) {

				Log.i(LOG_TAG, "restart app");

				// restart app
				Intent i = getBaseContext().getPackageManager()
					.getLaunchIntentForPackage(
						getBaseContext().getPackageName());
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);

				isPhoneCalling = false;
			}

		}
	}
}





	  void createMarkersFromJson(String json) throws JSONException {
		  // De-serialize the JSON string into an array of city objects
		  //JSONArray jsonArray = new JSONArray(json);
		  
		  
			  // Create a marker for each city in the JSON data.
		  		//System.out.println(json);
			  JSONObject jsonObj =  new JSONObject(json);
			  mName.setText(jsonObj.getString("name"));
			  mType.setText(jsonObj.getString("summary"));
			  followedName=jsonObj.getString("name");
			  followedType=jsonObj.getString("summary");
			  followedLat=jsonObj.getJSONArray("latlng").getDouble(0);
			  followedLong=jsonObj.getJSONArray("latlng").getDouble(1);
			  followedIcon=jsonObj.getString("icon");
			  String slash="/";
			  if(followedIcon.contains(slash)){
				  followedIcon = followedIcon.substring(followedIcon.lastIndexOf(slash)+1, followedIcon.length());
			  }
			  String truncIcon = followedIcon.substring(0, followedIcon.length()-4);
			  icon = "@drawable/"+truncIcon;
			  coords= new LatLng(followedLat, followedLong);
			  //mType.setText(sessId+" "+cfduId);
			  //mName.setText(userName2);
			  //mType.setText(userName2);*/
			  textDesc=jsonObj.getString("description");
			  
			  //mDesc.setText(Html.fromHtml(jsonObj.getString("description")));

			  makeMap();
			  
			  if (Html.fromHtml(textDesc).toString().length()>160){
				  cutText=true;
				  miniText=Html.fromHtml(textDesc).toString().substring(0, 160)+"... ...";
				  mDesc.setText(miniText);
				  mDesc.setOnClickListener(new View.OnClickListener() {
			        public void onClick(View v) {
			        	mTogText();
			        }});
			  }
			  else
				  mDesc.setText(Html.fromHtml(textDesc));
			  mAdresse.setText(Html.fromHtml(
					  jsonObj.getString("street")+
					  " "+
					  jsonObj.getString("zipcode")+
					  " "+
					  jsonObj.getString("city")
					  ));
			  mEmailSub=("Clicstreet - "+jsonObj.getString("name"));
			  mEmailBod=("Retrouvez et suivez "
					  +jsonObj.getString("name")
					  +" sur Clicstreet.com en vous rendant à l'adresse :<br/> <a href='http://www.clicstreet.com/grenoble/"
					  +jsonObj.getString("slug")
					  +"'>"
					  +jsonObj.getString("name")
					  +"</a>"
					  +"<br/><br/><br/><b>Clic<i>street</i></b> -- <i>Ayez le réflexe!</i>"
					  
					  );
			  if (jsonObj.getString("phone").length()>3){
				  mPhone.setText("APPELER");
				  //mPhone.setPadding(12, 12, 12, 12);
			  	mPhone.setVisibility(View.VISIBLE);
			  	}
			  else{
				  mPhone.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.comm_nophone_icon,0,0);
				  mPhone.setText("PAS DE NUMÉRO");
				  //mPhone.setPadding(12, 12, 12, 12);
				  mPhone.setVisibility(View.VISIBLE);
			  }

			  JSONArray mCatalog = jsonObj.getJSONArray("catalog");
			  if(jsonObj.getBoolean("followed")){
				  mFollowBtn.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.comm_star_elec,0,0);
				  mFollowBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_odd));
				  mFollowBtn.setText("ABONNÉ !");
				  mFollowBtn.setTextColor(Color.parseColor("#b3b3b3"));
			  }
			  else if(userName.equals("guest")){
				  mFollowBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_blue));
				  mFollowBtn.setText("CONNECTEZ-VOUS");
				  mFollowBtn.setTextColor(Color.WHITE);
				  mFollowBtn.setOnClickListener(new View.OnClickListener() {
			            public void onClick(View v) {
			            	Intent intent = new Intent(CommerceActivity.this, LoginActivity.class);
			    		   	finish();
			    		   	startActivity(intent);
			            	
			            }});
			  }
			  else{
				  mFollowBtn.setOnClickListener(new View.OnClickListener() {
			            public void onClick(View v) {
			            	followDialog();
			              }});
				  mName.setOnClickListener(new View.OnClickListener() {
					            public void onClick(View v) {
			            	followDialog();
			              }});
			  
			  }
			  JSONObject mTimetable = jsonObj.getJSONObject("openinghours");
			  final String[] amOpen = new String[7];
			  final String[] amClose = new String[7];
			  final String[] pmOpen = new String[7];
			  final String[] pmClose = new String[7];
			  final Integer[] amOpH = new Integer[7];
			  final Integer[] amOpM = new Integer[7];
			  final Integer[] amClH = new Integer[7];
			  final Integer[] amClM = new Integer[7];
			  final Integer[] pmOpH = new Integer[7];
			  final Integer[] pmOpM = new Integer[7];
			  final Integer[] pmClH = new Integer[7];
			  final Integer[] pmClM = new Integer[7];
			  final Integer[] pmOpTot = new Integer[7];
			  final Integer[] pmClTot = new Integer[7];
			  final Integer[] amOpTot = new Integer[7];
			  final Integer[] amClTot = new Integer[7];

			  final String[] amTimes = new String[7];
			  final String[] pmTimes = new String[7];
			  /*
			  final StringBuilder amString = new StringBuilder();
			  final StringBuilder pmString = new StringBuilder();
			  amString.append("<b>Matin</b>");
			  pmString.append("<b>Apr�s-midi</b>");*/
			  
			  String matinOuvert="";
			  for (int j = 0; j < 7; j++) {
				  amOpen[j]= mTimetable.getJSONObject(Integer.toString(j+1)).getString("amOpening");
				  amClose[j]= mTimetable.getJSONObject(Integer.toString(j+1)).getString("amClosing");
				  pmOpen[j]= mTimetable.getJSONObject(Integer.toString(j+1)).getString("pmOpening");
				  pmClose[j]= mTimetable.getJSONObject(Integer.toString(j+1)).getString("pmClosing");
				  if (!amOpen[j].equals("null")){
				  amOpH[j] = Integer.parseInt(amOpen[j].substring(0,2));
				  amOpM[j] = Integer.parseInt(amOpen[j].substring(3,5));
				  amOpTot[j]=60*amOpH[j]+amOpM[j];
				  }
				  if (!amClose[j].equals("null")){
				  amClH[j] = Integer.parseInt(amClose[j].substring(0,2));
				  amClM[j] = Integer.parseInt(amClose[j].substring(3,5));
				  amClTot[j]=60*amClH[j]+amClM[j];
				  }
				  if (!pmOpen[j].equals("null")){
				  pmOpH[j] = Integer.parseInt(pmOpen[j].substring(0,2));
				  pmOpM[j] = Integer.parseInt(pmOpen[j].substring(3,5));
				  pmOpTot[j]=60*pmOpH[j]+pmOpM[j];}
				  if (!pmClose[j].equals("null")){
				  pmClH[j] = Integer.parseInt(pmClose[j].substring(0,2));
				  pmClM[j] = Integer.parseInt(pmClose[j].substring(3,5));
				  pmClTot[j]=60*pmClH[j]+pmClM[j];}
				  if (amOpen[j]=="null" && amClose[j]=="null" && pmOpen[j]=="null" && pmClose[j]=="null"){
					  amTimes[j]="Ferm�";
					  pmTimes[j]="Ferm�";
				  }else if (amOpen[j]!="null" && amClose[j]!="null" && pmOpen[j]=="null" && pmClose[j]=="null"){
					  amTimes[j]=amOpen[j]+"/"+amClose[j];
					  pmTimes[j]="Ferm�";
				  }else if (amOpen[j]=="null" && amClose[j]=="null" && pmOpen[j]!="null" && pmClose[j]!="null"){
					  amTimes[j]="Ferm�";
					  pmTimes[j]=pmOpen[j]+"/"+pmClose[j];
				  }else if (amOpen[j]!="null" && pmClose[j]!="null" && amClose[j]=="null" && pmOpen[j]=="null"){
					  amTimes[j]=amOpen[j];
					  pmTimes[j]=pmClose[j];
				  }else if (amOpen[j]!="null" && pmClose[j]!="null"  && amClose[j]!="null" ){
					  amTimes[j]=amOpen[j]+"/"+amClose[j];
					  pmTimes[j]=pmOpen[j]+"/"+pmClose[j];
				  }
			  }
			  int arrayDayOfWeek=dayOfWeek-1;
			  if (amOpen[arrayDayOfWeek]=="null" && amClose[arrayDayOfWeek]=="null" && pmOpen[arrayDayOfWeek]=="null" && pmClose[arrayDayOfWeek]=="null")
			  {
			  mButtonTime.setText(Html.fromHtml("FERMÉ"));
			  mButtonTime.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_even));
			  mOuvert.setText("Fermé");
			  mOuvert.setBackgroundColor(Color.parseColor("#cc0000"));}
			  else if (amOpen[arrayDayOfWeek]!="null" && amClose[arrayDayOfWeek]!="null" && pmOpen[arrayDayOfWeek]=="null" && pmClose[arrayDayOfWeek]=="null")
				  {
				  	if (amOpTot[arrayDayOfWeek]< totalMinutes && totalMinutes < amClTot[arrayDayOfWeek]){
						  mButtonTime.setText(Html.fromHtml("OUVERT")+amClose[arrayDayOfWeek]);
						  mButtonTime.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_elec));
						  mOuvert.setText("Ouvert");
						  mOuvert.setBackgroundColor(Color.parseColor("#00cc00"));

						  
				  	}
				  	else
				  	{
						  mButtonTime.setText(Html.fromHtml("FERMÉ"));
						  mButtonTime.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_even));}
					  mOuvert.setText("Fermé");
					  mOuvert.setBackgroundColor(Color.parseColor("#cc0000"));
				  	}
			  else if (amOpen[arrayDayOfWeek]=="null" && amClose[arrayDayOfWeek]=="null" && pmOpen[arrayDayOfWeek]!="null" && pmClose[arrayDayOfWeek]!="null")
			  {
				if (pmClTot[arrayDayOfWeek]<pmOpTot[arrayDayOfWeek]){	
					pmClTot[arrayDayOfWeek]=pmClTot[arrayDayOfWeek]+24*60;
				}
			  	if (pmOpTot[arrayDayOfWeek]< totalMinutes && totalMinutes < pmClTot[arrayDayOfWeek]){
					  mButtonTime.setText(Html.fromHtml("OUVERT")+pmClose[arrayDayOfWeek]);
					  mButtonTime.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_elec));
					  mOuvert.setText("Ouvert");
					  mOuvert.setBackgroundColor(Color.parseColor("#00e6a4"));
			  	}
			  	else
			  	{
					  mButtonTime.setText(Html.fromHtml("FERMÉ"));
					  mOuvert.setText("Fermé");
					  mOuvert.setBackgroundColor(Color.parseColor("#cc0000"));
					  mButtonTime.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_even));}
			  	

			  }
			  
			  
			  
			  else if (amOpen[arrayDayOfWeek]!="null" && amClose[arrayDayOfWeek]=="null" && pmOpen[arrayDayOfWeek]=="null" && pmClose[arrayDayOfWeek]!="null")
			  {if (pmClTot[arrayDayOfWeek]<amOpTot[arrayDayOfWeek]){	
					pmClTot[arrayDayOfWeek]=pmClTot[arrayDayOfWeek]+24*60;
				}
			  	if (amOpTot[arrayDayOfWeek]< totalMinutes && totalMinutes < pmClTot[arrayDayOfWeek]){
					  mButtonTime.setText(Html.fromHtml("OUVERT")+pmClose[arrayDayOfWeek]);
					  mButtonTime.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_elec));
					  mOuvert.setText("Ouvert");
					  mOuvert.setBackgroundColor(Color.parseColor("#00e6a4"));
			  	}
			  	else
			  	{
					  mButtonTime.setText(Html.fromHtml("FERMÉ"));
					  mOuvert.setText("Fermé");
					  mOuvert.setBackgroundColor(Color.parseColor("#cc0000"));
					  mButtonTime.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_even));}
			  	}
			  else if (amOpen[arrayDayOfWeek]!="null" && amClose[arrayDayOfWeek]!="null" && pmOpen[arrayDayOfWeek]!="null" && pmClose[arrayDayOfWeek]!="null")
			  {if (pmClTot[arrayDayOfWeek]<pmOpTot[arrayDayOfWeek]){	
					pmClTot[arrayDayOfWeek]=pmClTot[arrayDayOfWeek]+24*60;
				}
			  	if (amOpTot[arrayDayOfWeek]< totalMinutes && totalMinutes < amClTot[arrayDayOfWeek]){
					  mButtonTime.setText(Html.fromHtml("OUVERT")+amClose[arrayDayOfWeek]);
					  mButtonTime.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_elec));
					  mOuvert.setText("Ouvert");
					  mOuvert.setBackgroundColor(Color.parseColor("#00e6a4"));
			  	}
			  	else if (pmOpTot[arrayDayOfWeek]< totalMinutes && totalMinutes < pmClTot[arrayDayOfWeek]){
					  mButtonTime.setText(Html.fromHtml("OUVERT")+pmClose[arrayDayOfWeek]);
					  mButtonTime.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_elec));
					  mOuvert.setText("Ouvert");
					  mOuvert.setBackgroundColor(Color.parseColor("#00e6a4"));
			  	}
			  	else
			  	{
					  mButtonTime.setText(Html.fromHtml("FERMÉ"));
					  mButtonTime.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_even));
					  mOuvert.setText("Fermé");
					  mOuvert.setBackgroundColor(Color.parseColor("#cc0000"));}
			  	}/*
			 final String amHtmlString = amString.toString();
			 final String pmHtmlString = pmString.toString();*/
			 final String daysHtmlString = "<b> </b><br/>Lundi<br/>Mardi<br/>Mercredi<br/>Jeudi<br/>Vendredi<br/>Samedi<br/>Dimanche&nbsp;&nbsp;";
			 
			  //mType.setText(matinOuvert);
			 /*
			  mAmTimes.setText(Html.fromHtml(amHtmlString));
			  mPmTimes.setText(Html.fromHtml(pmHtmlString));
			  mDaysTimes.setText(Html.fromHtml(daysHtmlString));*/
			    mDateBtn.setOnClickListener(new View.OnClickListener() {
			        public void onClick(View v) {
			      	  //mTogVis2(mDateBar);
			        	showTimes(amTimes,pmTimes);
			        }});
			  //mPmTimes.setText(pmString);
			  String mCoverUrl = jsonObj.getString("picture");
			  String mLogoUrl = jsonObj.getString("logo");
			  if (mCatalog.length() > 0){

				  mTextPropos.setTextColor(Color.parseColor("#b3b3b3"));
				  String leTextePropos = jsonObj.getString("name")+" vous propose:";
				  mTextPropos.setText(leTextePropos);
				  mTextPropos.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
			  }
			  else {

				  mTextPropos.setVisibility(View.INVISIBLE);
			  }

			  JSONArray mNews = jsonObj.getJSONArray("news");
			  if (mNews.length() > 0){
				  String leTextFlash = "Derniers flashs:";
				  mTextFlashs.setText(leTextFlash);
			  }
			  else {

				  mTextFlashs.setVisibility(View.INVISIBLE);
			  }
			  //mTextPropos.setText(leTextePropos);
			  mName.setTextColor(Color.parseColor("#"+jsonObj.getJSONObject("colors").getString("secondary")));
			  mType.setTextColor(Color.parseColor("#"+jsonObj.getJSONObject("colors").getString("secondary")));
			  mAdresse.setTextColor(Color.parseColor("#"+jsonObj.getJSONObject("colors").getString("secondary")));
			  //mBody.setBackgroundColor(Color.parseColor("#"+jsonObj.getJSONObject("colors").getString("background")));

			  mFacebookLink=(
					  "https://www.facebook.com/sharer/sharer.php?s=100&p[url]=http%3A%2F%2Fwww.clicstreet.com%2Fgrenoble%2F"
					  +jsonObj.getString("slug")
					  +"&p[images][0]="
					  +URLEncoder.encode(jsonObj.getString("logo"))
					  +"&p[title]=Clicstreet+-+"
					  +jsonObj.getString("name").replace(" ", "+")
					  +"&p[summary]=Retrouvez+et+suivez+"
					  +jsonObj.getString("name").replace(" ", "+")
					  +"+sur+Clicstreet.com+!"
					  
					  
					  );
			  mTwitterLink=("http://twitter.com/home?status=http://www.clicstreet.com/grenoble/"+jsonObj.getString("slug"));
			 mShareLinks.add(0, mFacebookLink);
			 mShareLinks.add(1, mTwitterLink);
			  
			  //mDesc.setText(mFacebookLink);
			  //mName.setText(userName);
			  UrlImageViewHelper.setUrlDrawable(mCover, mCoverUrl);
			  UrlImageViewHelper.setUrlDrawable(mLogo, mLogoUrl);
			  final LinearLayout[] catLines = new LinearLayout[mCatalog.length()];
			  final TextView[] textViews = new TextView[mCatalog.length()];
			  final TextView[] priceViews = new TextView[mCatalog.length()];			  
			  for (int i = 0; i < mCatalog.length(); i++) {
				  JSONObject jsonCat = mCatalog.getJSONObject(i);
				  catLines[i] = new LinearLayout(this);
				  catLines[i].setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));

				  ((LinearLayout) mCatBox).addView(catLines[i]);
				  
				  
				  textViews[i] = new TextView(this);
				  textViews[i].setText("* "+Html.fromHtml(jsonCat.getString("name")));
				  
				  textViews[i].setTextColor(Color.parseColor("#5e5e5e"));
				  textViews[i].setGravity(Gravity.LEFT);
				  textViews[i].setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				  ((LinearLayout) catLines[i]).addView(textViews[i]);
				  priceViews[i] = new TextView(this);
				  if  (!jsonCat.getJSONObject("price").isNull("amount")){
					  Integer priceGet = jsonCat.getJSONObject("price").getInt("amount");
					  Double realPrice = (double)priceGet/100;
					  priceViews[i].setText(String.format(" -- %.2f",realPrice)+"€");
					  priceViews[i].setGravity(Gravity.RIGHT);
				  }
				  else {
					  priceViews[i].setText("");}

				  priceViews[i].setTextColor(Color.parseColor("#b3b3b3"));
				  priceViews[i].setLayoutParams(new LayoutParams(120,LayoutParams.WRAP_CONTENT));
				  ((LinearLayout) catLines[i]).addView(priceViews[i]);
				 /* buffer.append("<tr><td>");
				  buffer.append(jsonCat.getString("name"));
				  buffer.append("</td><td>");
				  buffer.append(jsonCat.getJSONObject("price").getInt("amount")/100);
				  buffer.append("&euro;</td></tr>");*/
			  }
			  final LinearLayout[] flashContainer = new LinearLayout[mNews.length()];
			  final LinearLayout[] flashDivider = new LinearLayout[mNews.length()];
			  final ImageView[] flashIconViews = new ImageView[mNews.length()];
			  final TextView[] textNewsViews = new TextView[mNews.length()];
			  final TextView[] dateNewsViews = new TextView[mNews.length()];
			  final Space[] spacerViews = new Space[mNews.length()];
			  for (int i = 0; i < mNews.length(); i++) {
				  JSONObject jsonNews = mNews.getJSONObject(i);
				  flashDivider[i] = new LinearLayout(this);
				  flashDivider[i].setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				  flashDivider[i].setOrientation(LinearLayout.VERTICAL);
				  flashDivider[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.droppedf2));

				  ((LinearLayout) mFlashBox).addView(flashDivider[i]);
				  
				  
				  flashContainer[i] = new LinearLayout(this);
				  flashContainer[i].setPadding(12, 12, 12, 12);
				  flashContainer[i].setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				  flashContainer[i].setOrientation(LinearLayout.HORIZONTAL);
				  ((LinearLayout) flashDivider[i]).addView(flashContainer[i]);
				  
				  
				  
				  flashIconViews[i] = new ImageView(this);
				  flashIconViews[i].setImageDrawable(getResources().getDrawable(R.drawable.flash_blue_nobg));

				  ((LinearLayout) flashContainer[i]).addView(flashIconViews[i]);
				  
				  

				  dateNewsViews[i] = new TextView(this);
				  dateNewsViews[i].setGravity(Gravity.RIGHT);
				  dateNewsViews[i].setTextColor(Color.parseColor("#00e6a4"));
				  dateNewsViews[i].setTypeface(null, Typeface.ITALIC);
				  dateNewsViews[i].setText(compareDates(jsonNews.getJSONObject("date").getString("date")));
				  
				  dateNewsViews[i].setPadding(4, 4, 4, 4);
				  ((LinearLayout) flashDivider[i]).addView(dateNewsViews[i]);
				  
				  
				  
				  textNewsViews[i] = new TextView(this);
				  textNewsViews[i].setText(Html.fromHtml(jsonNews.getString("description")));
				  textNewsViews[i].setPadding(4, 4, 4, 0);
				  textNewsViews[i].setTextColor(Color.parseColor("#000000"));
				  textNewsViews[i].setGravity(Gravity.LEFT);
				  textNewsViews[i].setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
				  ((LinearLayout) flashContainer[i]).addView(textNewsViews[i]);

				  
				  
				  
				  
				 /* buffer.append("<tr><td>");
				  buffer.append(jsonCat.getString("name"));
				  buffer.append("</td><td>");
				  buffer.append(jsonCat.getJSONObject("price").getInt("amount")/100);
				  buffer.append("&euro;</td></tr>");*/
			  }
			  //Remove phoning
			  /*
			  if (jsonObj.getString("phone").length()>3){
			  final String makeCall="tel:"+jsonObj.getString("phone");
			  mPhone.setOnClickListener(new View.OnClickListener() {
			    	 
					@Override
					public void onClick(View arg0) {
						
						Intent callIntent = new Intent(Intent.ACTION_CALL);
						callIntent.setData(Uri.parse(makeCall));
						startActivity(callIntent);

					}

				});}*/
			    mShareBtn.setOnClickListener(new View.OnClickListener() {
			        public void onClick(View v) {
			      	  //mTogVis(mShareBar);
			        		shareDialog();
			        }}); 
			  /*PhoneCallListener phoneListener = new PhoneCallListener();
			TelephonyManager telephonyManager = (TelephonyManager) this
					.getSystemService(Context.TELEPHONY_SERVICE);
			telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);*/
		      //mCover.setImageDrawable(image);
			 // mTest.setText(Html.fromHtml("<img src='http://www.clicstreet.com/media/cache/place_picture/places/17/picture/c88ce1.png' />"));
			  /*String html = 
					  "" +
					  "<img src='http://www.clicstreet.com/media/cache/place_picture/places/17/picture/c88ce1.png' height='200' width='600'/>" +
					  "";

					  
					  URLImageParser p = new URLImageParser(mDesc, this);
					  Spanned htmlSpan = Html.fromHtml(html, p, null);
					  mDesc.setText(htmlSpan);
			  
			  */
			  
			  
			  /*
			  mMap.put(jsonObj.getString("name"), jsonObj.getString("id"));
			  String getIcon = jsonObj.getString("icon");
			  String truncIcon = getIcon.substring(0, getIcon.length()-4);
			  String icon = "@drawable/"+truncIcon;
			  String markerId = "marker"+jsonObj.getString("id");

			 Marker marker = map.addMarker(new MarkerOptions()
				  .title(jsonObj.getString("name"))
				  .icon(BitmapDescriptorFactory.fromResource(getResources().getIdentifier(icon,"drawable", getPackageName())))
				  .snippet(jsonObj.getString("typetitle"))
				  .position(new LatLng(
					  jsonObj.getJSONArray("latlng").getDouble(0),
					  jsonObj.getJSONArray("latlng").getDouble(1)
					  )
				  )
			  );
		  }
	  }*/
			

			  mLoadStatus.setVisibility(View.GONE);
			  mWrapper.setVisibility(View.VISIBLE);
	  }		
	  public void followDialog(){

			AlertDialog.Builder alert=new AlertDialog.Builder(this);

			LayoutInflater inflater = this.getLayoutInflater();
			View diaView = inflater.inflate(R.layout.follow_alert,null);
			alert.setView(diaView);
			final TextView mApp = (TextView)diaView.findViewById(R.id.appBtn);
			mApp.setOnClickListener(new View.OnClickListener() {
			        public void onClick(View v) {
			        	toggleBtn(mApp);
		        }});
			final TextView mEmail = (TextView)diaView.findViewById(R.id.mailBtn);
			mEmail.setOnClickListener(new View.OnClickListener() {
			        public void onClick(View v) {
			        	toggleOtherBtn(mEmail);
		        }});
			
			
			alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, close
					// current activity
					//LoginActivity.this.finish();
					int sumTest=0;
               	 if (isApp)
               	 	{makeUrl+="&app=true";
               	 	sumTest+=1;}
               	 else
               	 	{makeUrl+="&app=false";}
               	 if (isEmail)
            	 	{makeUrl+="&mail=true";
               	 	sumTest+=1;}
            	 else
            	 	{makeUrl+="&mail=false";}
               	 if (sumTest>0){
               	System.out.println("output="+makeUrl);
               	mRunFollow task = new mRunFollow();
            	task.execute((Void) null);}

               	 
				}});
			alert.setPositiveButton("Annuler", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, close
					dialog.dismiss();
					//LoginActivity.this.finish();
				}});
			dialog=alert.show();
			dialogShown=true;
			
		}

	  public void itineraryDialog(){

			AlertDialog.Builder alert=new AlertDialog.Builder(this);
			alert.setTitle("Itinéraire");
			alert.setItems(R.array.options_itinerary, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	            	   if (which==0){
	            		   	locoMotion=3;}
	            	   else if (which==1){
	            		   	locoMotion=2;}
	            	   else {
	            		   locoMotion=0;
	            	   }
	            	   if (which==0 || which==1 || which==2){/*
	            		   Intent in=new Intent(getApplicationContext(),CommerceActivity.class);
					  		in.putExtra("destLat", followedLat);
					  		in.putExtra("destLong", followedLong);
					  		in.putExtra("destName", followedName);
					  		in.putExtra("destId", shopId);
					  		in.putExtra("kshopid", shopId);
					  		in.putExtra("destType", followedType);
					  		in.putExtra("destIcon", followedIcon);
					  		in.putExtra("locoMotion", locoMotion);
					  		in.putExtra("user", userName);
				  			in.putExtra("secret", userName2);
				  			startActivity(in);*/
	            		   LatLng fromPosition = new LatLng(45.189102, 5.724468);
	            		   Location myLoc = getLocation();
	            		   if (myLoc!=null){
	            		   fromPosition = new LatLng (myLoc.getLatitude(), myLoc.getLongitude());}
	            		   MakeRoute(locoMotion, fromPosition, coords);
	            		   //rSystem.out.println("trigger 1"+imHere+coords);
	            	   
	            	   }
            	   	}
	            	   

       });
			alert.setPositiveButton("Annuler", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, close
					dialog.dismiss();
					//LoginActivity.this.finish();
				}});
			dialog=alert.show();
			dialogShown=true;
			
		}
	  
	  
	  
	  
	  public void shareDialog(){

			AlertDialog.Builder alert=new AlertDialog.Builder(this);
			LayoutInflater inflater = this.getLayoutInflater();
			View diaView = inflater.inflate(R.layout.share_alert,null);
			alert.setView(diaView);

			final TextView mFB = (TextView)diaView.findViewById(R.id.fbBtn);
			mFB.setOnClickListener(new View.OnClickListener() {
			        public void onClick(View v) {
			        	Uri uri = Uri.parse(mShareLinks.get(0).toString());
	               		Intent browserIntent = new Intent(Intent.ACTION_VIEW);
	               		browserIntent.setDataAndType(uri, "text/html");
	               		browserIntent.addCategory(Intent.CATEGORY_BROWSABLE);
	               		startActivity(browserIntent);
		        }});
			final TextView mTW = (TextView)diaView.findViewById(R.id.twBtn);
			mTW.setOnClickListener(new View.OnClickListener() {
			        public void onClick(View v) {
			        	Uri uri = Uri.parse(mShareLinks.get(1).toString());
	               		Intent browserIntent = new Intent(Intent.ACTION_VIEW);
	               		browserIntent.setDataAndType(uri, "text/html");
	               		browserIntent.addCategory(Intent.CATEGORY_BROWSABLE);
	               		startActivity(browserIntent);
		        }});
			final TextView mEM = (TextView)diaView.findViewById(R.id.emBtn);
			mEM.setOnClickListener(new View.OnClickListener() {
		    	public void onClick(View v) {
	             	  Intent i = new Intent(Intent.ACTION_SEND);
	             	  i.setType("text/html");
	             	  i.putExtra(Intent.EXTRA_EMAIL  , new String[]{""});
	             	  i.putExtra(Intent.EXTRA_SUBJECT, mEmailSub);
	             	  i.putExtra(Intent.EXTRA_TEXT   , Html.fromHtml(mEmailBod));
	             	  startActivity(Intent.createChooser(i, "Send mail..."));}
		    	});
			alert.setPositiveButton("Annuler", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, close
					dialog.dismiss();
					//LoginActivity.this.finish();
				}});
			dialog=alert.show();
			
		}
	  
	  public void showTimes(String[] am, String[] pm){
		  
			AlertDialog.Builder alert=new AlertDialog.Builder(this);
			LayoutInflater inflater = this.getLayoutInflater();
			View diaView = inflater.inflate(R.layout.dialog_times,null);
			alert.setView(diaView);
			TableLayout mTimeLayout = (TableLayout) diaView.findViewById(R.id.timeTable);

			mTimeLayout.setBackgroundColor(Color.parseColor("#b3b3b3"));
			final TableRow tableHeader = new TableRow(this);
			tableHeader.setWeightSum(3);
			tableHeader.setBackgroundColor(Color.parseColor("#f2f2f2"));
			((TableLayout) mTimeLayout).addView(tableHeader);
			final TextView tableTitle = new TextView(this);
			TableLayout.LayoutParams headPar = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			tableHeader.setPadding(20, 20, 20, 20);
			tableHeader.setLayoutParams(headPar);
			TableRow.LayoutParams params2 = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f);
			tableTitle.setText(R.string.times_header);
			tableTitle.setTextSize(20f);
			tableTitle.setTextColor(Color.parseColor("#808080"));
			final TextView amTitle = new TextView(this);
			amTitle.setText(R.string.times_am);
			amTitle.setGravity(1);
			amTitle.setTextColor(Color.parseColor("#808080"));
			final TextView pmTitle = new TextView(this);
			pmTitle.setText(R.string.times_pm);
			pmTitle.setGravity(1);
			pmTitle.setTextColor(Color.parseColor("#808080"));

			tableTitle.setLayoutParams(params2);
			amTitle.setLayoutParams(params2);
			pmTitle.setLayoutParams(params2);
			((TableRow) tableHeader).addView(tableTitle);
			((TableRow) tableHeader).addView(amTitle);
			((TableRow) tableHeader).addView(pmTitle);
			
			
			  final TableRow[] dayLines = new TableRow[7];
			  String[] mDayNames = new String[7];
			  mDayNames = getResources().getStringArray(R.array.weekdays);  
			  final TextView[] dayNames = new TextView[7];
			  final TextView[] amTimes = new TextView[7];
			  final TextView[] pmTimes = new TextView[7];
			LayoutParams params = new LayoutParams(
				        LayoutParams.MATCH_PARENT,      
				        LayoutParams.WRAP_CONTENT
				);
			  for (int i = 0; i < 7; i++) {
				  
				  dayLines[i] = new TableRow(this);
				  ((TableLayout) mTimeLayout).addView(dayLines[i]);

				  dayLines[i].setWeightSum(3);
			  /**
			  mAmTimes = (TextView) diaView.findViewById(R.id.timeAm);
			  mPmTimes = (TextView) diaView.findViewById(R.id.timePm);
			  mAmTimes.setText(Html.fromHtml(am));
			  mPmTimes.setText(Html.fromHtml(pm));
			  mDaysTimes.setText(Html.fromHtml(days));*/
				  dayNames[i] = new TextView(this);
				  dayNames[i].setText(mDayNames[i]);
				  dayNames[i].setBackgroundColor(Color.parseColor("#f2f2f2"));
				  if (dayOfWeek-1==i){
					  dayNames[i].setBackgroundColor(Color.parseColor("#00e6a4"));
				  }
				  dayNames[i].setTextColor(Color.parseColor("#808080"));
				  dayNames[i].setTextSize(15f);
				  dayNames[i].setPadding(8, 8, 8, 8);
				  amTimes[i] = new TextView(this);
				  amTimes[i].setText(am[i]);
				  amTimes[i].setPadding(8, 8, 8, 8);
				  amTimes[i].setGravity(1);
				  amTimes[i].setTextColor(Color.WHITE);
				  pmTimes[i] = new TextView(this);
				  pmTimes[i].setTextColor(Color.WHITE);
				  pmTimes[i].setText(pm[i]);
				  pmTimes[i].setGravity(1);
				  pmTimes[i].setPadding(8, 8, 8, 8);
				  dayNames[i].setLayoutParams(params2);
				  amTimes[i].setLayoutParams(params2);
				  pmTimes[i].setLayoutParams(params2);
				  ((TableRow) dayLines[i]).addView(dayNames[i]);
				  ((TableRow) dayLines[i]).addView(amTimes[i]);
				  ((TableRow) dayLines[i]).addView(pmTimes[i]);

			  }
			alert.setPositiveButton("Fermer", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, close
					dialog.dismiss();
					//LoginActivity.this.finish();
				}});
			dialog=alert.show();
			dialogShown=true;
			
		}
	  @Override
	    public boolean onCreateOptionsMenu(Menu menu)
	    {
	        MenuInflater menuInflater = getMenuInflater();
	        menuInflater.inflate(R.layout.commerce_menu, menu);
	        return true;
	    }
	     
	    /**
	     * Event Handling for Individual menu item selected
	     * Identify single menu item by it's id
	     * */
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item)
	    {
	         
	        switch (item.getItemId())
	        {
	        case R.id.menu_suivre:
	            // Single menu item is selected do something
	            // Ex: launching new activity/screen or show alert message
	            Toast.makeText(CommerceActivity.this, "Bookmark is Selected", Toast.LENGTH_SHORT).show();
	            return true;
	 
	        case R.id.menu_facebook:
	            Toast.makeText(CommerceActivity.this, "Save is Selected", Toast.LENGTH_SHORT).show();
	            return true;
	 
	        case R.id.menu_twitter:
	            Toast.makeText(CommerceActivity.this, "Search is Selected", Toast.LENGTH_SHORT).show();
	            return true;
	 
	        case R.id.menu_email:
	            Toast.makeText(CommerceActivity.this, "Share is Selected", Toast.LENGTH_SHORT).show();
	            return true;

	        default:
	            return super.onOptionsItemSelected(item);
	        }
	    }
	    protected void timeCheck(){
	    	Calendar c = Calendar.getInstance(); 
	        int hour = c.get(Calendar.HOUR_OF_DAY);
	        int minutes = c.get(Calendar.MINUTE);
	        int day = c.get(Calendar.DAY_OF_WEEK);
	        mDays.put(1, "Sunday");
	        mDays.put(2, "Monday");
	        mDays.put(3, "Tuesday");
	        mDays.put(4, "Wednesday");
	        mDays.put(5, "Thursday");
	        mDays.put(6, "Friday");
	        mDays.put(7, "Saturday");
	    	
	    	
	    	
	    }
	    
	    public void  MakeRoute(Integer mode, LatLng from, LatLng to){

	    	if (beenMade){
	    		map.clear();
	    		makeMap();
	    		
	    	}
	    	
	    		final Navigator nav =  new Navigator(map,from,to);
	    		System.out.println(nav.getPathLines().size());
				nav.setMode(mode, 1234, 1);
				nav.findDirections(true, false);
				
				nav.setOnPathSetListener(new OnPathSetListener() {
				@Override
				public void onPathSetListener(Directions directions) {
					Directions routeDir = nav.getDirections();
					Integer routeSize = nav.getDirections().getRoutes().size();
					if (routeSize>0){
						Route lastRoute = routeDir.getRoutes().get(routeSize-1);
						
						LatLngBounds rteBnds = lastRoute.getMapBounds();
						
					    map.moveCamera(CameraUpdateFactory.newLatLngBounds(rteBnds, 50));}}});
				beenMade=true;
		
	    }
	    
	    public Location getLocation() {
	        try {
	            locationManager = (LocationManager) this
	                    .getSystemService(LOCATION_SERVICE);
	 
	            // getting GPS status
	            isGPSEnabled = locationManager
	                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
	 
	            // getting network status
	            isNetworkEnabled = locationManager
	                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	 
	            if (!isGPSEnabled && !isNetworkEnabled) {
	                //System.out.println("No location services");
	            } else {
	                this.canGetLocation = true;
	                // First get location from Network Provider
	                if (isNetworkEnabled) {
	                    locationManager.requestLocationUpdates(
	                            LocationManager.NETWORK_PROVIDER,
	                            MIN_TIME_BW_UPDATES,
	                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
	                    //System.out.println("Network location");
	                    if (locationManager != null) {
	                        location = locationManager
	                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	                        if (location != null) {
	                            latitude = location.getLatitude();
	                            longitude = location.getLongitude();
	                        }
	                    }
	                }
	                // if GPS Enabled get lat/long using GPS Services
	                if (isGPSEnabled) {
	                    if (location == null) {
	                        locationManager.requestLocationUpdates(
	                                LocationManager.GPS_PROVIDER,
	                                MIN_TIME_BW_UPDATES,
	                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
	                        if (locationManager != null) {
	                            location = locationManager
	                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
	                            if (location != null) {
	                                latitude = location.getLatitude();
	                                longitude = location.getLongitude();
	                            }
	                        }
	                    }
	                }
	            }
	 
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	 
	        return location;
	    }
		public double getLatitude(){
	        if(location != null){
	            latitude = location.getLatitude();
	        }
	         
	        return latitude;
	    }
	     
	    /**
	     * Function to get longitude
	     * */
	    public double getLongitude(){
	        if(location != null){
	            longitude = location.getLongitude();
	        }
	        
	        return longitude;
	    }




		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			
		}




		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}




		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		public void makeMap(){

			  map.moveCamera(CameraUpdateFactory.newLatLngZoom(coords, 18));
			  
			  Marker marker=null;
				int u = getResources().getIdentifier(icon,"drawable", getPackageName());
				  if (u!=0) {
				 marker = map.addMarker(new MarkerOptions()
				  .title(followedName)
				  .icon(BitmapDescriptorFactory.fromResource(u))
				  .snippet(followedType)
				  .position(coords)
			  );}
			  final LatLng fromPosition = new LatLng(45.19302, 5.72950);
		}


		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		public void toggleBtn(TextView btn){
			if (!isApp){
        		btn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.share_app_blue, 0, 0);
        		btn.setTextColor(Color.parseColor("#143cff"));
        		isApp=true;
        	}
			else {
				btn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.share_app, 0, 0);
        		btn.setTextColor(Color.parseColor("#b3b3b3"));
        		isApp=false;
			}
			
		}
		public void toggleOtherBtn(TextView btn){
			if (!isEmail){
        		btn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.share_mail_blue, 0, 0);
        		btn.setTextColor(Color.parseColor("#143cff"));
        		isEmail=true;
        	}
			else {
				btn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.share_mail, 0, 0);
        		btn.setTextColor(Color.parseColor("#b3b3b3"));
        		isEmail=false;
			}
			
		}
		public String isPlural(long s){
			if (s>1)
				return "s";
			else
				return "";
			
		}
		
		
		
		public String compareDates(String publishDate){
			Date date;
			long diff;
			String string;
			  try {
				date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE).parse(publishDate);
				diff=rightNow-date.getTime();
				long minutes=diff/(1000*60);
				if (minutes<1)
					string = "� l'instant";
				else if(minutes<60)
					string = "il y a "+Math.round(minutes)+" minute"+isPlural(Math.round(minutes));
				else if (minutes<(60*24))
					string = "il y a "+Math.round(minutes/60)+" heure"+isPlural(Math.round(minutes/60));
				else if (minutes<(60*24*7))
					string = "il y a "+Math.round(minutes/(60*24))+" jour"+isPlural(Math.round(minutes/(60*24)));
				else 
					string = "il y a "+Math.round(minutes/(60*24*7))+" semaine"+isPlural(Math.round(minutes/(60*24*7)));
				return string;
			  } 
			  catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			  }
		}

}