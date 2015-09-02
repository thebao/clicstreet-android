package com.katacult.clicstreet;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ClipData.Item;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
/*
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.Overlay;*/
//import com.katacult.clicstreet.CommerceActivity.mRunFollow;

public class MainActivity extends FragmentActivity {
public TextView mUserName;
private static final String LOG_TAG = "MainActivity";
//private static final String SERVICE_URL = "http://ruebrooch.com/test.json";
private static final String SERVICE_URL = "http://www.clicstreet.com/api/mobile/grenoble.json";
//private static final String SERVICE_URL = "http://www.ruebrooch.com/test.json";
static LatLng VICTOR = new LatLng(45.1889, 5.7244);

  static final LatLng FNAC = new LatLng(45.19037, 5.72632);
  static final LatLng ETAM = new LatLng(45.19185, 5.728369);
  static final LatLng ARTHAUD = new LatLng(45.19163, 5.72842);
  static final LatLng DIF = new LatLng(45.1915464, 5.7305748);
  static final Map mMap = new HashMap();
  static final HashMap mDays = new HashMap();


  private GoogleMap map;
  private CheckBox mTogAll;
  private TextView mUsername;
  public ImageButton mMenuTog;
  public ImageButton mOpenTog;
  public Button mServiceTog;
  public ImageButton mCompte;
  public LinearLayout mMenuBar;
  public LinearLayout mLoadStatus;
  public LinearLayout mWrapper;
  public Item mDiscItem;
  TextView mOpenString;
  Double destLat=0.0;
  Double destLong=0.0;
  String userName;
  String userName2;
  String cookie;
  Integer markNum=0;
  Boolean notGuest;
  EditText mSearchBar;
  LinearLayout mSearchCont;
  LinearLayout mResultBar;
  Button mResetSearch;
  TextView mSearchTerm;
  TextView mToggleCircle;
  TextView mToggleCircleGreen;
  Boolean soloedBtn=false;
  public String searchVal;
  Boolean filterOpen=false;
  Boolean togAll=true;
  Boolean searchFilter=false;
  List<Integer> resultIds = new ArrayList<Integer>();
  ArrayList<LatLng> testPoint = new ArrayList<LatLng>();
  ArrayList<LatLng> searchBounds = new ArrayList<LatLng>();

	int dayOfWeek;
	int totalMinutes;
	public Dialog dialog;
	public String savedOptions;
	public Boolean dialogShown=false;
  Boolean canSolo=true;


  public static final String EXTRA_MESSAGE = "message";
  public static final String PROPERTY_REG_ID = "registration_id";
  private static final String PROPERTY_APP_VERSION = "appVersion";
  private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
  private static final int BACKOFF_MILLI_SECONDS = 2000;
  private static final int MAX_ATTEMPTS = 5;
  private static final Random random = new Random();
  static final String DISPLAY_MESSAGE_ACTION =
          "com.katacult.GCM2.DISPLAY_MESSAGE";
  
  

  /**
   * Substitute you own sender ID here. This is the project number you got
   * from the API Console, as described in "Getting Started."
   */
  String SENDER_ID = "863795138432";

  AsyncTask<Void, Void, Void> mRegisterTask;
  /**
   * Tag used on log messages.
   */
  static final String TAG = "GCM Demo";

  TextView mDisplay;
  GoogleCloudMessaging gcm;
  AtomicInteger msgId = new AtomicInteger();
  Context context;

  String myVersion = android.os.Build.VERSION.RELEASE;
  String regid;
  String deviceName="";

  
  boolean[] checkedValues = new boolean[12];
  public static final Set<String> VALUECACHE = new HashSet<String>(Arrays.asList(new String[] {}));
  public static final Set<String> VALUES = new HashSet<String>(Arrays.asList(new String[] {
		  "service",
		  "food",
		  "association",
		  "cinema",
		  "shopping",
		  "culture",
		  "hotel",
		  "multimedia",
		  "restoration",
		  "public",
		  "goingout",
		  "transport"
		  }));
  String[] catNames = {"service",
             		  "food",
            		  "association",
            		  "cinema",
            		  "shopping",
            		  "culture",
            		  "hotel",
            		  "multimedia",
            		  "restoration",
            		  "public",
            		  "goingout",
            		  "transport"};
  private boolean drewMarkers = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
	//  CookieSyncManager.createInstance(getApplicationContext());
  //if it's a string you stored.
    super.onCreate(savedInstanceState);
    /*Intent intent = getIntent();
    String userName = intent.getStringExtra("key");

    mUserName = (TextView) findViewById(R.id.user);
    mUserName.setText(userName);*/
    setContentView(R.layout.activity_main);
    
    Calendar c2 = Calendar.getInstance();
    c2.setTime(new Date());
    dayOfWeek = c2.get(Calendar.DAY_OF_WEEK)-1;
    if (dayOfWeek==0)
    	dayOfWeek=7;
    int hourOfDay = c2.get(Calendar.HOUR_OF_DAY);
    int minuteOfDay = c2.get(Calendar.MINUTE);/*
    dayOfWeek=5;
    hourOfDay=3;
    minuteOfDay=15;*/
    
    
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction("CLOSE_ALL");
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
    	  System.out.println("received!");
        finish();
      }
    };
    registerReceiver(broadcastReceiver, intentFilter);
    
    
    
    totalMinutes = 60*hourOfDay+minuteOfDay;
    if (totalMinutes<300)
    {totalMinutes=totalMinutes+24*60;}
    this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	//savedOptions = sharedPreferences.getString("storedCategories", "111111111111");
    for (int i=0;i<12;i++){
    	//if (savedOptions.substring(i,i+1).equals("1"))
    		checkedValues[i]=true;
    	//System.out.println(catNames[i]+" is set to "+checkedValues[i]);
    	}
    if (getIntent().getExtras() != null){
    	Intent intent = getIntent();
    	String[] cookieArray = intent.getStringArrayExtra("cookieArray");
    	/*
    	for (int i = 0; i < cookieArray.length; i++) { 
			System.out.println("- " + cookieArray[i]); // Cookie found 
		} */
    	

    	userName = intent.getStringExtra("user");
    	userName2 = intent.getStringExtra("secret");
    	//notGuest = getIntent().getExtras().getBoolean("con");
    	cookie = intent.getStringExtra("cookie");}
    else {
    	cookie = "";
    	userName = "bao";
    	notGuest = false;}
    

    /*
    CookieManager cookieManager = CookieManager.getInstance();
    cookieManager.setCookie("http://www.clicstreet.com", "PHPSESSID="+userName);*/
    Log.e("user", userName +" secret: "+userName2);
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
 
    /*
    if(notGuest){    mUserName.setText(userName);}
    else{mUserName.setText("Guest");}*/
    //mUserName.setText(Integer.toString(cookie.length())+" : "+cookie);


     // TODO Auto-generated method stub
   
    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(MainActivity.this);
    System.out.println(GooglePlayServicesUtil.getErrorString(resultCode));
    if (resultCode!=0){
    if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
    if (resultCode == ConnectionResult.SERVICE_MISSING) {
       Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, MainActivity.this, resultCode);
   		dialog.show();
    } else if (resultCode == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED) {
    	Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, MainActivity.this, resultCode);
    	dialog.show();
    } else if (resultCode == ConnectionResult.SERVICE_DISABLED) {
    	Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, MainActivity.this, resultCode);
    	dialog.show();
		
    }}
    else {
    	Toast.makeText(this, "Cette application ne peut pas fonctionner sans Google Play Services...", 
		Toast.LENGTH_LONG).show();
    	finish();
    	
    }
    }
    
	  Log.e(LOG_TAG, "Cookie"+cookie);
    //mUserName.setText(Integer.toString(hour)+":"+Integer.toString(minutes)+" on "+mDays.get(day).toString());

    map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
    map.setMyLocationEnabled(true);
    map.setPadding(0,64,0,0);
    map.getUiSettings().setRotateGesturesEnabled(false);
    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    Criteria criteria = new Criteria();
    String provider = locationManager.getBestProvider(criteria, true);
    mMenuTog=(ImageButton) findViewById(R.id.menuTog);
    mCompte=(ImageButton) findViewById(R.id.menuCompte);
    if (userName.equals("guest")){
    	mCompte.setVisibility(View.GONE);
    }
    else{
    	savePreferences("the_user", userName);
    	savePreferences("the_secret", userName2);

        context = getApplicationContext();
    	
    	if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);
            //System.out.println("regid is"+regid);
            registerInBackground();
            if (regid == null || regid.length()==0) {
                registerInBackground();
            }
            
        } else {
        	System.out.println("noggogServ");
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
        
    	
    	
    	
    	
    }
    
    
    
    mOpenTog=(ImageButton) findViewById(R.id.openTog);
    mServiceTog=(Button) findViewById(R.id.menuService);
	mMenuBar = (LinearLayout) findViewById(R.id.topMenu);
	mLoadStatus = (LinearLayout) findViewById(R.id.load_status);
	mWrapper = (LinearLayout) findViewById(R.id.wrapper);
	mSearchBar = (EditText) findViewById(R.id.searchBar);
	mSearchCont = (LinearLayout) findViewById(R.id.searchCont);
	mResultBar = (LinearLayout) findViewById(R.id.searchResults);
	mResetSearch = (Button) findViewById(R.id.searchReset);
	mSearchTerm =(TextView) findViewById(R.id.searchTerm);
	mOpenString =(TextView) findViewById(R.id.openString);
	mMenuBar.setVisibility(View.GONE);
	mToggleCircle = (TextView) findViewById(R.id.spot);
	mToggleCircleGreen = (TextView) findViewById(R.id.spot_green);
    
    mMenuTog.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
      	 // mTogVis(mMenuBar);
        	showFilters();
        }});
    
    mCompte.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {/*
        	Intent in=new Intent(getApplicationContext(),SelectActivity.class);
    		in.putExtra("user", userName);
    		in.putExtra("secret", userName2);
    		startActivity(in);*/
        	showProfileMenu();
        }});
    
    mOpenTog.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	showAll();

        	
        }});
    mResetSearch.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	resetSearch();

        	
        }});
   
    mSearchBar.setOnEditorActionListener(new OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            	searchEngine();
                handled = true;
            }
            return handled;
        }
    });
    
    showAll();
    
    /*mServiceTog.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {

        	map.clear();
            new Thread(new Runnable() {
            	public void run() {
            		try {
            			retrieveAndAddCities();
            		} 
            		catch (IOException e) {
            			Log.e(LOG_TAG, "Cannot retrieve cities", e);
            			return;
            		}
            	}
            	}).start();
        }});*/
    
    //Location myLocation = locationManager.getLastKnownLocation(provider);
    //LatLng myLocationLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
 


    

   /*
    Marker fnac = map.addMarker(new MarkerOptions().position(FNAC)
            .title("FNAC")
            .icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.shopping)));
    Marker dif = map.addMarker(new MarkerOptions().position(DIF)
            .title("Le DiF�ReNt")
            .icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.beer)));
    Marker arthaud = map.addMarker(new MarkerOptions().position(ARTHAUD)
            .title("Arthaud")
            .icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.culture)));
    Marker etam = map.addMarker(new MarkerOptions().position(ETAM)
            .title("Etam Lingerie")
            .icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.slip)));*/

    
    new Thread(new Runnable() {
    	public void run() {
    		try {
    			retrieveAndAddCities();
    		} 
    		catch (IOException e) {
    			Log.e(LOG_TAG, "Cannot retrieve cities", e);
    			return;
    		}
    	}
    	}).start();
    

    if (getIntent().getExtras().getDouble("destLat") != 0.0 && getIntent().getExtras().getDouble("destLong") != 0.0){
    	
    	Intent intent = getIntent();
    	destLat = intent.getDoubleExtra("destLat", 0.0);
    	destLong= intent.getDoubleExtra("destLong", 0.0);
    	LatLng gotoPlace = new LatLng(destLat, destLong);
    	VICTOR =gotoPlace;
		final LatLng fromPosition = new LatLng(45.19302, 5.72950);
		final LatLng toPosition = new LatLng(destLat, destLong);
		
		
  	}
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(VICTOR, 16));
    map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {                      
        @Override
        public void onInfoWindowClick(Marker marker) {
        	/*if (marker.getTitle().equals("DiF�ReNt")) {
        		Intent in=new Intent(getApplicationContext(),CommerceActivity.class);
        		in.putExtra("key", mEmail); //Optional parameters
        		startActivity(in);
        	}*/
        	String clickedMarker = marker.getTitle();
        	String clickedId = mMap.get(clickedMarker).toString();
        		Intent in=new Intent(getApplicationContext(),CommerceActivity.class);
        		in.putExtra("kshopid", clickedId);
        		in.putExtra("user", userName);
        		in.putExtra("secret", userName2);
        		startActivity(in);
        }});

/*
    if (map.getZoomLevel() < 8)
        map.getController().setZoom(8);
  */
  }
  Handler mHandler = new Handler() {
      @Override
      public void handleMessage(Message msg) {
          String text = (String)msg.obj;
      }
};
  protected void mTogVis(LinearLayout lay){
	  if(!lay.isShown()){
		  lay.setVisibility(View.VISIBLE);

      	  //mMenuTog.setText(Html.fromHtml("Menu &#x25B2;"));
	  }
	  else {
		  lay.setVisibility(View.GONE);		
      	  //mMenuTog.setText(Html.fromHtml("Menu &#x25BC;"));  
	  }
	  
  }
  public void toggleBtn(View v){
	  String which = v.getTag().toString();
	  if (VALUES.contains(which)) {
		  map.clear();

	  v.setBackgroundDrawable(getResources().getDrawable(R.drawable.gray_button_noright));
	  VALUES.remove(which);
	  map.clear();
	    new Thread(new Runnable() {
	    	public void run() {
	    		try {
	    			retrieveAndAddCities();
	    		} 
	    		catch (IOException e) {
	    			Log.e(LOG_TAG, "Cannot retrieve cities", e);
	    			return;
	    		}
	    	}
	    	}).start();
	    
	  }
	  else {
		  map.clear();
	  v.setBackgroundDrawable(getResources().getDrawable(R.drawable.myshape_noright));
	  VALUES.add(which);
	  map.clear();
	    new Thread(new Runnable() {
	    	public void run() {
	    		try {
	    			retrieveAndAddCities();
	    		} 
	    		catch (IOException e) {
	    			Log.e(LOG_TAG, "Cannot retrieve cities", e);
	    			return;
	    		}
	    	}
	    	}).start();
	    
	  }
	  
  }
  

  public void soloBtn(View v){
	  if (canSolo){
	  String which = v.getTag().toString();
	  map.clear();
  			VALUECACHE.addAll(VALUES);
          VALUES.clear();
          VALUES.add(which);
          map.clear();
    	  v.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_button_noleft));
          new Thread(new Runnable() {
          	public void run() {
          		try {
          			retrieveAndAddCities();
          		} 
          		catch (IOException e) {
          			Log.e(LOG_TAG, "Cannot retrieve cities", e);
          			return;
          		}
          	}
          	}).start();
      canSolo=false;
      	}
	  else{
		  map.clear();
          VALUES.clear();
          VALUES.addAll(VALUECACHE);
          map.clear();
          v.setBackgroundDrawable(getResources().getDrawable(R.drawable.myshape_noleft));
          new Thread(new Runnable() {
            	public void run() {
            		try {
            			retrieveAndAddCities();
            		} 
            		catch (IOException e) {
            			Log.e(LOG_TAG, "Cannot retrieve cities", e);
            			return;
            		}
            	}
            	}).start();

          canSolo=true;
	  }
	  
  
  }
  
  
  
  protected void mButTogVis(Button but){
	  if(!but.isShown()){
		  but.setVisibility(View.VISIBLE);
	  }
	  else {
		  but.setVisibility(View.GONE);		  
	  }
	  
  }

  
  protected void retrieveAndAddCities() throws IOException {
	  
	  String listCats="";
	  /*for (Object obj : VALUES) {
	     listCats = listCats+obj.toString();
	   }


	  Message msg = new Message();
	   msg.obj = listCats;
	   mHandler.sendMessage(msg);*/
	  HttpURLConnection conn = null;
	  final StringBuilder json = new StringBuilder();
	  try {
		  // Connect to the web service
		  URL url = new URL(SERVICE_URL+"?_user="+userName+"&_secret="+userName2);
		  
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
		  // De-serialize the JSON string into an array of city objects
		  JSONArray jsonArray = new JSONArray(json);
		  int jsonTodo2 =jsonArray.length();
		  Boolean openShop[]=new Boolean[jsonTodo2];
		  String[] debugCase = new String[jsonTodo2];
		  Boolean amIviz[]=new Boolean[jsonTodo2];
		  for (int n = 0; n < jsonTodo2; n++) {
			  // Create a marker for each city in the JSON data.
			  JSONObject jsonObj = jsonArray.getJSONObject(n);
			  mMap.put(jsonObj.getString("name"), jsonObj.getString("id"));
			  String getIcon = jsonObj.getString("icon");
			  String slash="/";
			  if(getIcon.contains(slash)){
				  getIcon = getIcon.substring(getIcon.lastIndexOf(slash)+1, getIcon.length());
			  }
			  String truncIcon = getIcon.substring(0, getIcon.length()-4);
			  String icon = "@drawable/"+truncIcon;
			  String markerId = "marker"+jsonObj.getString("id");
			  String catHier[] = jsonObj.getString("type").split("\\.");
			  String mainCat = catHier[0];
			  String subCat = catHier[1];
			  amIviz[n]=true;
			  openShop[n]=true;

			  
			  
			  
			  if (filterOpen){
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
			  
			  final StringBuilder amString = new StringBuilder();
			  final StringBuilder pmString = new StringBuilder();
			  amString.append("<b>Matin</b>");
			  pmString.append("<b>Aprés-midi</b>");
			  String matinOuvert="";
			  int j = dayOfWeek-1;
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
			  debugCase[n]="zz";
			  int arrayDayOfWeek=dayOfWeek;
			  if (
					  amOpen[j]=="null" && 
					  amClose[j]=="null" && 
					  pmOpen[j]=="null" && 
					  pmClose[j]=="null")
			  {openShop[n]=false;
			  debugCase[n]="a";}
			  else if (
					  amOpen[j]!="null" && 
					  amClose[j]!="null" && 
					  pmOpen[j]=="null" && 
					  pmClose[j]=="null")
		  		{
				  if (amOpTot[j]< totalMinutes && totalMinutes < amClTot[j])
				  	{openShop[n]=true;
					  debugCase[n]="b";}
				  else
			  		{openShop[n]=false;
					  debugCase[n]="c";}
		  		}
			  else if (
					  amOpen[j]=="null" && 
					  amClose[j]=="null" && 
					  pmOpen[j]!="null" && 
					  pmClose[j]!="null")
		  		{
				  if (pmClTot[j]<pmOpTot[j]){	
						pmClTot[j]=pmClTot[j]+24*60;	
					}
				  if (pmOpTot[j]< totalMinutes && totalMinutes < pmClTot[j])
				  	{openShop[n]=true;
					  debugCase[n]="d";}
				  else
			  		{openShop[n]=false;
					  debugCase[n]="e";}
		  		}
			  else if (
					  amOpen[j]!="null" && 
					  amClose[j]=="null" && 
					  pmOpen[j]=="null" && 
					  pmClose[j]!="null")
		  		{
				  if (pmClTot[j]<amOpTot[j]){	
						pmClTot[j]=pmClTot[j]+24*60;	
					}
				  if (amOpTot[j]< totalMinutes && totalMinutes < pmClTot[j])
				  	{openShop[n]=true;
					  debugCase[n]="f";}
				  else
			  		{openShop[n]=false;
					  debugCase[n]="g";}
		  		}
			  else if (
					  amOpen[j]!="null" && 
					  amClose[j]!="null" && 
					  pmOpen[j]!="null" && 
					  pmClose[j]!="null")
		  		{			  
				  if (pmClTot[j]<pmOpTot[j]){	
					pmClTot[j]=pmClTot[j]+24*60;	
				  }
				  if (amOpTot[j]< totalMinutes && totalMinutes < amClTot[j])
				  	{openShop[n]=true;
					  debugCase[n]="h";}
				  else if (pmOpTot[j]< totalMinutes && totalMinutes < pmClTot[j])
				  	{openShop[n]=true;
					  debugCase[n]="i";}
				  else
			  		{openShop[n]=false;
					  debugCase[n]="j";}
		  		}
					  
				  
			  
			  
			  
			  }
			  
			  if (searchFilter){
				  if (!resultIds.contains(jsonObj.getInt("id"))){
					  amIviz[n]=false;
				  }
				  else
				  	{
					  searchBounds.add(new LatLng(
							  				jsonObj.getJSONArray("latlng").getDouble(0),
							  				jsonObj.getJSONArray("latlng").getDouble(1)));
				  	}
				  
			  }
			  
			  if (VALUES.contains(mainCat) && openShop[n]) {
			  //String markerCat = markerFullCat[0];
			  //String markerSubCat = markerFullCat[1];
			  int u = getResources().getIdentifier(icon,"drawable", getPackageName());
			  if (u!=0) {
				  
					 Marker marker = map.addMarker(new MarkerOptions()
						  .title(jsonObj.getString("name"))
						  .icon(BitmapDescriptorFactory.fromResource(u))
						  .snippet(jsonObj.getString("typetitle"))
						  .visible(amIviz[n])
						  .position(new LatLng(
							  jsonObj.getJSONArray("latlng").getDouble(0),
							  jsonObj.getJSONArray("latlng").getDouble(1)
							  )
						  )
					  );
			  }
			  else {
			  }}
			  else if (!VALUES.contains(mainCat) && mainCat.equals("temporary")){
				  int u = getResources().getIdentifier(icon,"drawable", getPackageName());

				  System.out.println("stopped at A: icon is: "+icon);
				  if (u!=0) {
					  
						 Marker marker = map.addMarker(new MarkerOptions()
							  .title(jsonObj.getString("name"))
							  .icon(BitmapDescriptorFactory.fromResource(u))
							  .snippet(jsonObj.getString("typetitle"))
							  .visible(amIviz[n])
							  .position(new LatLng(
								  jsonObj.getJSONArray("latlng").getDouble(0),
								  jsonObj.getJSONArray("latlng").getDouble(1)
								  )
							  )
						  );
				  }
			  }
			  //map.animateCamera(CameraUpdateFactory.zoomTo(16), 500, null);
		  }
		  drewMarkers = true;
		  mLoadStatus.setVisibility(View.GONE);
		  mWrapper.setVisibility(View.VISIBLE);
		  //System.out.println(Integer.toString(searchBounds.size()));
	  }
	  
	  public void onPageFinished(String url) {
	       CookieSyncManager.getInstance().sync();
	       // Get the cookie from cookie jar.
	       cookie = CookieManager.getInstance().getCookie(url);
	       if (cookie == null) {
	    	   userName="bleble";
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
	        	 userName=(parts[1]);
	           }
	         if (parts[0].equals(" PHPSESSID")) {
	        	 userName2=(parts[1]);
		           }
	         }
	       }
	     }
	  
	  
	  
	  @Override
	    public boolean onCreateOptionsMenu(Menu menu)
	    {
	        MenuInflater menuInflater = getMenuInflater();
	        menuInflater.inflate(R.layout.main_menu, menu);
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
	        case R.id.menu_filtre:
	            // Single menu item is selected do something
	            // Ex: launching new activity/screen or show alert message

	        	showFilters();
	            return true;
	 
	        case R.id.menu_ouverts:
	            //Toast.makeText(MainActivity.this, "Save is Selected", Toast.LENGTH_SHORT).show();

	        	showOpen();
	            return true;
	 
	        case R.id.menu_compte:
	        	
	        	if (!userName.equals("guest")){
	            	Intent in=new Intent(getApplicationContext(),SelectActivity.class);
	        		in.putExtra("user", userName);
	        		in.putExtra("secret", userName2);
	        		startActivity(in);}
	        	else {
	        		Intent in=new Intent(getApplicationContext(),LoginActivity.class);
	        		startActivity(in);
	        		
	        	}
	            
	        	
	        	
	            return true;
	 
	        case R.id.menu_disc:
	        	removeConnection();
/*
	        	KillConnectionTask task = new KillConnectionTask();
	        	task.execute(new String[]  {"http://www.clicstreet.com/logout"});*/
	            return true;

	        default:
	            return super.onOptionsItemSelected(item);
	        }
	    }
	    
	    public void showProfileMenu(){
	    	AlertDialog.Builder alert=new AlertDialog.Builder(this);

			LayoutInflater inflater = this.getLayoutInflater();
			View diaView = inflater.inflate(R.layout.profile_alert,null);
			alert.setView(diaView);
	    	
			alert.setPositiveButton("Annuler", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, close
					dialog.dismiss();
					//LoginActivity.this.finish();
				}});
			final Button mFavs = (Button)diaView.findViewById(R.id.favBtn);
			mFavs.setOnClickListener(new View.OnClickListener() {
			        public void onClick(View v) {
			        	Intent in=new Intent(getApplicationContext(),UserActivity.class);
			    		in.putExtra("user", userName);
			    		in.putExtra("secret", userName2);
			    		in.putExtra("mode", "fav");
			    		startActivity(in);
		        }});
			final Button mFlash = (Button)diaView.findViewById(R.id.flashBtn);
			mFlash.setOnClickListener(new View.OnClickListener() {
			        public void onClick(View v) {
			        	Intent in=new Intent(getApplicationContext(),UserActivity.class);
			    		in.putExtra("user", userName);
			    		in.putExtra("secret", userName2);
			    		in.putExtra("mode", "flash");
			    		startActivity(in);
		        }});
			final Button mQuit = (Button)diaView.findViewById(R.id.discBtn);
		    mQuit.setOnClickListener(new View.OnClickListener() {
		    	public void onClick(View v) {
		    		loseConnection();}});
			dialog=alert.show();
	    }
	    
	    
	    
	    
	    public void showFilters() {

	    	
			AlertDialog.Builder alert=new AlertDialog.Builder(this);
			alert.setTitle("Filtres");

			alert.setMultiChoiceItems(R.array.option_filters, checkedValues,
                    new DialogInterface.OnMultiChoiceClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            	 if (isChecked) {
                     VALUES.add(catNames[which]);
                 }
                 else if (VALUES.contains(catNames[which])){
                	 VALUES.remove(catNames[which]);
 
                 }
             }
         });
			alert.setNeutralButton("Tous", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
				
				}
			});
			alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					savedOptions="";
					for (int i=0;i<12;i++){
						if (VALUES.contains(catNames[i])){
							savedOptions=savedOptions+"1";							
						}
						else

							savedOptions=savedOptions+"0";	
					}
					//System.out.println(savedOptions);		
					/*if (savedOptions.length()==12)
					{savePreferences("storedCategories", savedOptions);}*/
	                 map.clear();
	                 new Thread(new Runnable() {
	                       	public void run() {
	                       		try {
	                       			retrieveAndAddCities();
	                       		} 
	                       		catch (IOException e) {
	                       			Log.e(LOG_TAG, "Cannot retrieve cities", e);
	                       			return;
	                       		}
	                       	}
	                       	}).start();

					
	}});
			alert.setPositiveButton("Annuler", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, close
					dialog.dismiss();
					//LoginActivity.this.finish();
				}});
			dialog=alert.show();
			((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener()
		      {            
		          @Override
		          public void onClick(View v)
		          {
		        	  if(togAll){
		        	  AlertDialog d = (AlertDialog) dialog;
		        	  ListView oo = d.getListView();
						for (int i=0;i<12;i++){
		        		  
		                    oo.setItemChecked(i, true);
		                    VALUES.add(catNames[i]);
		                    checkedValues[i]=true;
		                    //System.out.println(catNames[i]);
		                }
		        	  togAll=false;
		        	  ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEUTRAL).setText("Aucun");}
		        	  else {
		        		  AlertDialog d = (AlertDialog) dialog;
			        	  ListView oo = d.getListView();
							for (int i=0;i<12;i++){
			                    oo.setItemChecked(i, false);
			                    VALUES.remove(catNames[i]);
			                    checkedValues[i]=false;
			                    //System.out.println(catNames[i]);
			                }
			        	  togAll=true;
			        	  ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEUTRAL).setText("Tous");}
		        	  
		          }
		      });
			dialogShown=true;
			
		}
	    protected void removeConnection() {
	    	SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	        Editor editor = sharedPreferences.edit();
	        editor.remove("storedName");
	        editor.commit();
	    	Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		   	finish();
		   	startActivity(intent);
		   	}
	    
	    private class KillConnectionTask extends AsyncTask<String, Void, String> {
	    	protected String doInBackground(String... url) {
	    	int code = 0;
    		InputStream content = null;
    		StringBuilder sb = new StringBuilder();
    		List<NameValuePair> params = new LinkedList<NameValuePair>();

    		
            DefaultHttpClient client = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpParams params2 = new BasicHttpParams();
    		try {
    			String currentUrl = url [0];
    			params.add(new BasicNameValuePair("Accept", "*/*"));
    			params.add(new BasicNameValuePair("Cookie", "__cfduid="+userName+";PHPSESSID="+userName2));
    			params2.setParameter("http.protocol.handle-redirects",false);
    			params2.setParameter("Cookie", "__cfduid="+userName+";PHPSESSID="+userName2);
    			
    			String paramString = URLEncodedUtils.format(params, "utf-8");
    			if(!currentUrl.endsWith("?"))
    				currentUrl += "?";
    			currentUrl += paramString;
                HttpGet httpGet = new HttpGet(url [0]);
			  httpGet.setParams(params2);
			  HttpResponse execute = client.execute(httpGet);
			  code = execute.getStatusLine().getStatusCode();
			  userName="";
			  userName2="";
	          /*BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
	          String line = null;
	          while ((line = buffer.readLine()) != null) {
	              sb.append(line);
	          }*/
	          /*content.close();*/
    		}
    		catch (IOException e) {
    			  Log.e(LOG_TAG, "Error connecting to service", e);
    			  //throw new IOException("Error connecting to service", e);
    		  } 

    		
    		String response=sb.toString();
  	      return Integer.toString(code);

    
    }

	        @Override
	        protected void onPostExecute(String result) {
	          mUserName.setText(result);
	          CookieManager.getInstance().removeAllCookie();
	          CookieSyncManager.getInstance().sync();
	          finish();
	        }
    }
		private void savePreferences(String key, String value) {
	        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	        Editor editor = sharedPreferences.edit();
	        editor.putString(key, value);
	        editor.commit();
	    }
		private void showOpen(){
			filterOpen=false;
			mOpenString.setTextColor(Color.parseColor("#b3b3b3"));
			//mOpenTog.setImageResource(R.drawable.toggle_on);
        	mOpenTog.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                	showAll();
                }});
         	map.clear();
         	AnimatorSet set = new AnimatorSet();
         	set.playTogether(
         	    ObjectAnimator.ofFloat(mToggleCircle, "translationX", 0, 60),
         	    ObjectAnimator.ofFloat(mToggleCircleGreen, "translationX", 0, 60),
         	    ObjectAnimator.ofFloat(mToggleCircleGreen, "alpha", 1, 0)
         	    
     	    );
     	   set.setDuration(500).start();
         	
            new Thread(new Runnable() {
                  	public void run() {
                  		try {
                  			retrieveAndAddCities();
                  		} 
                  		catch (IOException e) {
                  			Log.e(LOG_TAG, "Cannot retrieve cities", e);
                  			return;
                  		}
                  	}
                  	}).start();
		}
		private void showAll(){

			LayoutInflater inflater = getLayoutInflater();
			View layout = inflater.inflate(R.layout.toas_layout,
            (ViewGroup) findViewById(R.id.toast_layout_root));

			TextView text = (TextView) layout.findViewById(R.id.text);

			text.setText(getResources().getString(R.string.shops_open_now));
			int duration = Toast.LENGTH_SHORT;

			Toast toast = new Toast(getApplicationContext());
			toast.setGravity(Gravity.BOTTOM, 0, 24);
			toast.setDuration(duration);
			toast.setView(layout);
			toast.show();
			filterOpen=true;
			mOpenString.setTextColor(Color.parseColor("#00e6a4"));
			//mOpenTog.setImageResource(R.drawable.toggle_off);
        	mOpenTog.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                	showOpen();
                }});
        	map.clear();
         	AnimatorSet set = new AnimatorSet();
         	set.playTogether(
         	    ObjectAnimator.ofFloat(mToggleCircle, "translationX", 60, 0),
         	    ObjectAnimator.ofFloat(mToggleCircleGreen, "translationX", 60, 0),
         	    ObjectAnimator.ofFloat(mToggleCircleGreen, "alpha", 0, 1)
         	    
     	    );
      	   set.setDuration(500).start();
            new Thread(new Runnable() {
                  	public void run() {
                  		try {
                  			retrieveAndAddCities();
                  		} 
                  		catch (IOException e) {
                  			Log.e(LOG_TAG, "Cannot retrieve cities", e);
                  			return;
                  		}
                  	}
                  	}).start();
		}
		private void searchEngine(){
			InputMethodManager imm = (InputMethodManager)getSystemService(
			Context.INPUT_METHOD_SERVICE);
			resultIds.clear();
			searchVal = mSearchBar.getText().toString();
			imm.hideSoftInputFromWindow(mSearchBar.getWindowToken(), 0);

		    new Thread(new Runnable() {
		    	public void run() {
		    		try {
		    			retrieveSearchResults(searchVal);
		    		} 
		    		catch (IOException e) {
		    			Log.e(LOG_TAG, "Cannot retrieve search results", e);
		    			return;
		    		}
		    	}
		    	}).start();
			
			
		}
		protected void retrieveSearchResults(String schTerm) throws IOException {
			  
			  HttpURLConnection conn = null;
			  final StringBuilder json = new StringBuilder();
			  try {
				  // Connect to the web service
				  URL url = new URL("http://www.clicstreet.com/search/grenoble.json?q="+URLEncoder.encode(schTerm));
				  
				  conn = (HttpURLConnection) url.openConnection();
				  InputStreamReader in = new InputStreamReader(conn.getInputStream());
				   
				  // Read the JSON data into the StringBuilder
				  int read;
				  char[] buff = new char[1024];
					  while ((read = in.read(buff)) != -1) {
					  json.append(buff, 0, read);
					  }
					  //System.out.println("Search results for: "+schTerm);
					  //System.out.println(json);
			  } catch (IOException e) {
				  Log.e(LOG_TAG, "Error connecting to service", e);
				  //throw new IOException("Error connecting to service", e);
			  } finally {
				  if (conn != null) {
					  conn.disconnect();
					  
				  }
			  }
			  runOnUiThread(new Runnable() {
				  public void run() {
					  try {
						  digestSearch(json.toString());
					  } 
					  catch (JSONException e) {
						  Log.e(LOG_TAG, "Error processing JSON: "+json.toString(), e);
					  }
		  		}
			  });
		}
		
		void digestSearch(String json) throws JSONException {
				searchBounds.clear();
			  JSONObject jsonObject = new JSONObject(json);
			  JSONArray jsonPlaces = jsonObject.getJSONArray("places");
			  Integer mResultCount = jsonObject.getInt("hits");
			  String mSearchStatus = jsonObject.getString("status");
			  
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.toas_layout,
	            (ViewGroup) findViewById(R.id.toast_layout_root));
				TextView text = (TextView) layout.findViewById(R.id.text);
				String searchMsg;
				int duration = Toast.LENGTH_SHORT;
				Toast toast = new Toast(getApplicationContext());
				toast.setGravity(Gravity.BOTTOM, 0, 24);
				toast.setDuration(duration);
				toast.setView(layout);
			if (mSearchStatus.equals("ok")){
				if (mResultCount==0){
					searchFilter=false;
		            mSearchBar.setText("");
					searchMsg=getString(R.string.no_results);
					text.setText(searchMsg+" "+searchVal);
				}
				else {
					searchFilter=true;
					showAll();
					
					for (int i=0;i<jsonPlaces.length();i++){
						resultIds.add(jsonPlaces.getJSONObject(i).getInt("id"));
						//System.out.println(resultIds.get(i).toString());
					}
						
					
					
					
					String plural="";
					if (mResultCount>1)
						{plural="s";}
					searchMsg=getString(R.string.count_results);
					text.setText(mResultCount+" "+searchMsg+plural);

					VALUES.clear();
					for (int i=0;i<catNames.length;i++){
                    VALUES.add(catNames[i]);
                    checkedValues[i]=true;
					}
		         	map.clear();
		            new Thread(new Runnable() {
		                  	public void run() {
		                  		try {
		                  			retrieveAndAddCities();
		                  		} 
		                  		catch (IOException e) {
		                  			Log.e(LOG_TAG, "Cannot retrieve cities", e);
		                  			return;
		                  		}
		                  	}
		                  	}).start();
		            mSearchTerm.setText(mResultCount+" résultats pour "+searchVal+" ");
		            mSearchCont.setVisibility(View.GONE);
		            mResultBar.setVisibility(View.VISIBLE);
					
					
					
				}
			  }
			else 
				text.setText(R.string.failed_search);
			toast.show();

}
		
		private void resetSearch(){
            mSearchCont.setVisibility(View.VISIBLE);
            mResultBar.setVisibility(View.GONE);
            mSearchBar.setText("");
			searchFilter=false;
            map.clear();
            new Thread(new Runnable() {
                  	public void run() {
                  		try {
                  			retrieveAndAddCities();
                  		} 
                  		catch (IOException e) {
                  			Log.e(LOG_TAG, "Cannot retrieve cities", e);
                  			return;
                  		}
                  	}
                  	}).start();
            
		}
		public void loseConnection(){

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
					deletePreferences(PROPERTY_REG_ID);
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
		
		
		public boolean checkPlayServices() {
	        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	        if (resultCode != ConnectionResult.SUCCESS) {
	            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
	                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
	            } else {
	                //Log.i(TAG, "This device is not supported.");
	                finish();
	            }
	            return false;
	        }
	        return true;
	    }
		

	    public void storeRegistrationId(Context context, String regId) {
	        final SharedPreferences prefs = getGcmPreferences(context);
	        int appVersion = getAppVersion(context);
	        //Log.i(TAG, "Saving regId on app version " + appVersion);
	        SharedPreferences.Editor editor = prefs.edit();
	        editor.putString(PROPERTY_REG_ID, regId);
	        editor.putInt(PROPERTY_APP_VERSION, appVersion);
	        editor.commit();
	    }
	    

	    public String getRegistrationId(Context context) {
	        final SharedPreferences prefs = getGcmPreferences(context);
	        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	        
	    	//System.out.println(registrationId);
	        if (registrationId == null || registrationId.length()==0) {
	        	System.out.println("no registrationId");
	            //Log.i(TAG, "Registration not found.");
	            return "";
	        }
	        // Check if app was updated; if so, it must clear the registration ID
	        // since the existing regID is not guaranteed to work with the new
	        // app version.
	        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	        int currentVersion = getAppVersion(context);
	        if (registeredVersion != currentVersion) {
	            //Log.i(TAG, "App version changed.");
	            return "";
	        }
	        return registrationId;
	    }
	    
	    public void registerInBackground() {
	        new AsyncTask<Void, Void, String>() {
	            @Override
	            protected String doInBackground(Void... params) {
	                String msg = "";
	                try {
	                    if (gcm == null) {
	                        gcm = GoogleCloudMessaging.getInstance(context);
	                    }
	                    regid = gcm.register(SENDER_ID);
	                    msg = "Device registered, registration ID=" + regid;

	                    // You should send the registration ID to your server over HTTP, so it
	                    // can use GCM/HTTP or CCS to send messages to your app.
	                    sendRegistrationIdToBackend(regid);

	                    // For this demo: we don't need to send it because the device will send
	                    // upstream messages to a server that echo back the message using the
	                    // 'from' address in the message.

	                    // Persist the regID - no need to register again.
	                    storeRegistrationId(context, regid);
	                } catch (IOException ex) {
	                    msg = "Error :" + ex.getMessage();
	                    // If there is an error, don't just keep trying to register.
	                    // Require the user to click a button again, or perform
	                    // exponential back-off.
	                }
	                return msg;
	            }

	            @Override
	            protected void onPostExecute(String msg) {
	                
	            }
	        }.execute(null, null, null);
	    }
		
		
		/**
	     * @return Application's version code from the {@code PackageManager}.
	     */
	    public static int getAppVersion(Context context) {
	        try {
	            PackageInfo packageInfo = context.getPackageManager()
	                    .getPackageInfo(context.getPackageName(), 0);
	            return packageInfo.versionCode;
	        } catch (NameNotFoundException e) {
	            // should never happen
	            throw new RuntimeException("Could not get package name: " + e);
	        }
	    }

	    /**
	     * @return Application's {@code SharedPreferences}.
	     */
	    public SharedPreferences getGcmPreferences(Context context) {
	        // This sample app persists the registration ID in shared preferences, but
	        // how you store the regID in your app is up to you.
	        return getSharedPreferences(LoginActivity.class.getSimpleName(),
	                Context.MODE_PRIVATE);
	    }
	    /**
	     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
	     * messages to your app. Not needed for this demo since the device sends upstream messages
	     * to a server that echoes back the message using the 'from' address in the message.
	     */
	    public void sendRegistrationIdToBackend(final String regId) {

	    	//System.out.println("precontact to server");

			mRegisterTask = new AsyncTask<Void, Void, Void>() {
				 
	            @Override
	            protected Void doInBackground(Void... params) {
	                // Register on our server
	                // On server creates a new user
	                MainActivity.register(context, getDeviceModel(), getDeviceMaker(), myVersion, userName, userName2, "", regId);
	                return null;
	            }

	            @Override
	            protected void onPostExecute(Void result) {
	                mRegisterTask = null;
	            }

	        };
	        mRegisterTask.execute(null, null, null);
	    	
	      // Your implementation here.
	    }
	    
	    static void register(final Context context, String model, String devname, String osversion, String user, String secret, String manuf, final String regId) {
	    	//System.out.println(regId+": to server");
	        //Log.i(TAG, "registering device (regId = " + regId + ")");
	        //String serverUrl = "http://katacult.com/push/register.php";
	    	
	    	
	    	
	    	
	    	//String serverUrl = "http://192.168.0.12/clicstreetv1/web/app_dev.php/api/mobile/device?_user=theo&_secret=7c0c6e18a69a431b64ae15f38acb37dd74eb712d&deviceId="+regId+"&manufacturer=SAMSUNG&type=1&model=galaxyS3&osVersion=6.8"; 
	        Map<String, String> params = new HashMap<String, String>();
	        
	        //String serverUrl = "http://192.168.0.12/clicstreetv1/web/app_dev.php/api/mobile/device";
	        String serverUrl = "http://www.clicstreet.com/api/mobile/device";
	        params.put("deviceId", regId);
	        params.put("_user", user);
	        params.put("_secret", secret);
	        params.put("manufacturer", devname);
	        params.put("type", "1");
	        params.put("model", model);
	        params.put("osVersion", osversion);
	         
	        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
	        // Once GCM returns a registration id, we need to register on our server
	        // As the server might be down, we will retry it a couple
	        // times.
	        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
	            Log.d(TAG, "Attempt #" + i + " to register");
	            try {
	                displayMessage(context, "server_registering");
	                post(serverUrl, params);
	                GCMRegistrar.setRegisteredOnServer(context, true);
	                String message = "server_registered";
	                displayMessage(context, message);
	                return;
	            } catch (IOException e) {
	                // Here we are simplifying and retrying on any error; in a real
	                // application, it should retry only on unrecoverable errors
	                // (like HTTP error code 503).
	                Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
	                if (i == MAX_ATTEMPTS) {
	                    break;
	                }
	                try {
	                    //Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
	                    Thread.sleep(backoff);
	                } catch (InterruptedException e1) {
	                    // Activity finished before we complete - exit.
	                    //Log.d(TAG, "Thread interrupted: abort remaining retries!");
	                    Thread.currentThread().interrupt();
	                    return;
	                }
	                // increase backoff exponentially
	                backoff *= 2;
	            }
	        }
	        String message = "server_register_error";
	        LoginActivity.displayMessage(context, message);
	    }

	    static void displayMessage(Context context, String message) {
	        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
	        intent.putExtra(EXTRA_MESSAGE, message);
	        System.out.println(message);
	        context.sendBroadcast(intent);
	    }
	    public static void post(String endpoint, Map<String, String> params)
	            throws IOException {URL url;
	            try {
	                url = new URL(endpoint);
	            } catch (MalformedURLException e) {
	                throw new IllegalArgumentException("invalid url: " + endpoint);
	            }
	            StringBuilder bodyBuilder = new StringBuilder();
	            Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
	            // constructs the POST body using the parameters
	            while (iterator.hasNext()) {
	                Entry<String, String> param = iterator.next();
	                bodyBuilder.append(param.getKey()).append('=')
	                        .append(param.getValue());
	                if (iterator.hasNext()) {
	                    bodyBuilder.append('&');
	                }
	            }
	            String body = bodyBuilder.toString();
	            //Log.v(TAG, "Posting '" + body + "' to " + url);
	            byte[] bytes = body.getBytes();
	            HttpURLConnection conn = null;
	            try {
	                //Log.e("URL", "> " + url);
	                conn = (HttpURLConnection) url.openConnection();
	                conn.setDoOutput(true);
	                conn.setUseCaches(false);
	                conn.setFixedLengthStreamingMode(bytes.length);
	                conn.setRequestMethod("POST");
	                conn.setRequestProperty("Content-Type",
	                        "application/x-www-form-urlencoded;charset=UTF-8");
	                // post the request
	                OutputStream out = conn.getOutputStream();
	                System.out.println(out.toString());
	                out.write(bytes);
	                out.close();
	                // handle the response
	                int status = conn.getResponseCode();
	                BufferedReader in = new BufferedReader(new InputStreamReader(
                            conn.getInputStream()));
					String inputLine;
					while ((inputLine = in.readLine()) != null) 
					    System.out.println(inputLine);
					in.close();
	                if (status != 200) {
	                  throw new IOException("Post failed with error code " + status);
	                }
	            } finally {
	                if (conn != null) {
	                    conn.disconnect();
	                }
	            }
	          }
		    public String getDeviceModel() {
		    	  String manufacturer = Build.MANUFACTURER;
		    	  String model = Build.MODEL;
		    	  if (model.startsWith(manufacturer)) {
		    	    return capitalize(model);
		    	  } else {
		    	    return capitalize(model);
		    	  }
		    	}
		    
		    public String getDeviceMaker() {
		    	  String manufacturer = Build.MANUFACTURER;
		    	    return capitalize(manufacturer);
		    	  
		    	}

			private String capitalize(String s) {
			  if (s == null || s.length() == 0) {
			    return "";
			  }
			  char first = s.charAt(0);
			  if (Character.isUpperCase(first)) {
			    return s;
			  } else {
			    return Character.toUpperCase(first) + s.substring(1);
			  }
} 
		    
		
		
	    }

