package com.katacult.clicstreet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;


public class LoginActivity extends Activity {
	
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	private UserLoginTask mAuthTask = null;

	private String mEmail;
	private String mPassword;;
	private String mSecret;
	String mLogStatus="";
	Integer errorType=0;
	public Dialog dialog;
	public Boolean dialogShown =false;
	String mLogError="";
	Boolean canLogIn=false;
	private EditText mEmailView;
	private EditText mPasswordView;
	CheckBox mRememberMe;
	LinearLayout mFormToggle;
	LinearLayout mRememberedForm;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	String defName="login";
	Button mAutoConn;
	Button mForgetConn;
	 String name;

	

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

    String regid;

	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        
        name = sharedPreferences.getString("storedName", "");
		setContentView(R.layout.activity_login);

        //System.out.println("baooo");
		
		
        context = getApplicationContext();

        // Check device for Play Services APK. If check succeeds, proceed with GCM registration.
        /*
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);
            System.out.println("regid is"+regid);
            registerInBackground();
            if (regid == null || regid.length()==0) {
                registerInBackground();
            }
            
        } else {
        	System.out.println("noggogServ");
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
        
        */
        
        
		if(isGoogleMapsInstalled())
        {
        }
        else
        {
            Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.warn_no_gmaps_msg));
            builder.setCancelable(false);
            builder.setPositiveButton(getResources().getString(R.string.warn_no_gmaps_install), getGoogleMapsListener());
            AlertDialog dialog = builder.create();
            dialog.show();
        }
		checkConnection();
		   
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(LoginActivity.this);
	    System.out.println(GooglePlayServicesUtil.getErrorString(resultCode));
	    if (resultCode!=0){
	    if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	    if (resultCode == ConnectionResult.SERVICE_MISSING) {
	       Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, LoginActivity.this, resultCode);
	   		dialog.show();
	    } else if (resultCode == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED) {
	    	Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, LoginActivity.this, resultCode);
	    	dialog.show();
	    } else if (resultCode == ConnectionResult.SERVICE_DISABLED) {
	    	Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, LoginActivity.this, resultCode);
	    	dialog.show();
			
	    }}
	    else {
	    	Toast.makeText(this, "Google Play Services Required!", 
			Toast.LENGTH_LONG).show();
	    	finish();
	    	
	    }
	    }
		
	}
	
		public void checkConnection(){
			if (isOnline()){
				if (dialogShown)
				{
					dialog.dismiss();
					dialogShown=false;
				}else{}
		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.email);
		mFormToggle = (LinearLayout) findViewById(R.id.formToggle);
		mRememberedForm = (LinearLayout) findViewById(R.id.rememberedForm);
		mEmailView.setText(name);
		if (name==""){}
		else {

		    this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			mRememberedForm.setVisibility(View.VISIBLE);
			mAutoConn = (Button) findViewById(R.id.auto_conn_button);
			mAutoConn.setText(getResources().getString(R.string.action_remembered_sign_in)+": "+name);
			mForgetConn = (Button) findViewById(R.id.disconnect_button);
			mForgetConn.setText(getResources().getString(R.string.action_remembered_log_out)+": "+name);
			mFormToggle.setVisibility(View.GONE);
			
		}
		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mRememberMe = (CheckBox) findViewById(R.id.rememberBox);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
		
		findViewById(R.id.guest_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			            intent.putExtra("user", "guest");
			            intent.putExtra("secret", "947be6ef4c5c90cf9f66a461660178a379afddf5");
				       	startActivityForResult(intent, 0);
					}
				});
		
		findViewById(R.id.register_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.clicstreet.com/register/"));
						startActivity(browserIntent);
					}
				});
		
		findViewById(R.id.forgot_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.clicstreet.com/resetting/request"));
						startActivity(browserIntent);
					}
				});
		findViewById(R.id.disconnect_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				deletePreferences("storedName");
				Intent intent = getIntent();
				finish();
				startActivity(intent);
			}
		});
		findViewById(R.id.auto_conn_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
		String secret="07654cd2f5da7061dcfc009781dd927af9731129";
		  String input=name+secret;
		  MessageDigest mDigest = null;
		try {
			mDigest = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	        byte[] result = mDigest.digest(input.getBytes());
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < result.length; i++) {
	            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
	        }
	         
	        mSecret=sb.toString();
	        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("kshopid", "90");
            intent.putExtra("user", name);
            intent.putExtra("secret", mSecret);
	       	startActivityForResult(intent, 0);
			
			}
			
			
		});}
			else {
				alertConnection();
			}
		
		
	}

		@Override
		public void onResume(){
			super.onResume();
			checkConnection();

		}
		public void alertConnection(){

			AlertDialog.Builder alert=new AlertDialog.Builder(this);
			alert.setTitle(getResources().getString(R.string.warn_no_conn_title));
			alert.setMessage(getResources().getString(R.string.warn_no_conn_msg));
			alert.setIcon(R.drawable.ic_launcher);
			alert.setPositiveButton(getResources().getString(R.string.warn_no_conn_exit), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					LoginActivity.this.finish();
				}});
			alert.setNegativeButton(getResources().getString(R.string.warn_no_conn_settings), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
					LoginActivity.this.finish();
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
		
		

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		mEmailView.setError(null);
		mPasswordView.setError(null);

		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();
		  String secret="07654cd2f5da7061dcfc009781dd927af9731129";
		  String input=mEmail+secret;
		  MessageDigest mDigest = null;
		try {
			mDigest = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	        byte[] result = mDigest.digest(input.getBytes());
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < result.length; i++) {
	            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
	        }
	         
	        mSecret=sb.toString();

		boolean cancel = false;
		View focusView = null;

		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		}

		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} 

		if (cancel) {
			focusView.requestFocus();
		} else {
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	 
	public class UserLoginTask extends AsyncTask<Void, Void, Integer> {
		@Override
		protected Integer doInBackground(Void... params) {

			try {
			     DefaultHttpClient httpclient2 = new DefaultHttpClient();
			     HttpPost httppost = new HttpPost("http://www.clicstreet.com/api/mobile/login");
			     httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			     httppost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml");
			     List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			     nameValuePairs.add(new BasicNameValuePair("username", mEmail));
			     nameValuePairs.add(new BasicNameValuePair("password", mPassword));
			     nameValuePairs.add(new BasicNameValuePair("_secret", mSecret));
			     
			     try {httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));}
			     catch (UnsupportedEncodingException e) {return 1;}

			     
			     HttpResponse httpResponse = null;
			     try {httpResponse = httpclient2.execute(httppost);
			  
			     HttpEntity resEntity = httpResponse.getEntity();
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

				
			 	
			     }
			     catch (IOException e) {return 1;}
				if (canLogIn){

				if (mRememberMe.isChecked()){
				savePreferences("storedName", mEmail);}	
					
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
	            intent.putExtra("kshopid", "90");
	            intent.putExtra("user", mEmail);
	            intent.putExtra("secret", mSecret);
		       	startActivityForResult(intent, 0);
				}
				
				
				
				
				
				
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return 1;
			}


			return 1;
		}

  
		@Override
		protected void onPostExecute(final Integer success) {
			mAuthTask = null;
			showProgress(false);

			if (success == 2) {
				finish();
			} else if (errorType==2){
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			} else if (errorType==1){
				mEmailView
				.setError(getString(R.string.error_invalid_email));
				mEmailView.requestFocus();
	}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
		
		
		
		void createMarkersFromJson(String json) throws JSONException {
			  JSONArray jsonArray = new JSONArray(json);
			  JSONObject jsonObj = jsonArray.getJSONObject(0);
			  mLogStatus = jsonObj.getString("status");
			if (mLogStatus.equals("ok")){
			  canLogIn=true;}
			else {
				JSONArray jsonArray2 = jsonObj.getJSONArray("error");
				mLogError=jsonArray2.getString(0);
				  if (mLogError.contains("User")){
					  errorType=1;
					  }
				  else if (mLogError.contains("password")){
					  errorType=2;}
				  
			  }

}
	}
	private void savePreferences(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
	private void deletePreferences(String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }
	public boolean isGoogleMapsInstalled()
	{
	    try
	    {
	        @SuppressWarnings("unused")
			ApplicationInfo info = getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
	        return true;
	    }
	    catch(PackageManager.NameNotFoundException e)
	    {
	        return false;
	    }
	}
	 
	public OnClickListener getGoogleMapsListener()
	{
	    return new OnClickListener()
	    {
	        @Override
	        public void onClick(DialogInterface dialog, int which)
	        {
	            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.maps"));
	            startActivity(intent);
	 
	            finish();
	        }
	    };
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

    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    public void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);
        int appVersion = getAppVersion(context);
        //Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    public String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        
    	//System.out.println(registrationId);
        if (registrationId == null || registrationId.length()==0) {
        	//System.out.println("no registrationId");
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

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
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



    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                // LoginActivity.register(context, deviceName, "", regId);
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
    
    static void register(final Context context, String name, String email, final String regId) {
    	//System.out.println(regId+": to server");
        //Log.i(TAG, "registering device (regId = " + regId + ")");
        //String serverUrl = "http://katacult.com/push/register.php";
    	
    	
    	
    	
    	String serverUrl = "http://192.168.0.12/clicstreetv1/web/app_dev.php/api/mobile/device?_user=theo&_secret=7c0c6e18a69a431b64ae15f38acb37dd74eb712d&deviceId="+regId+"&manufacturer=SAMSUNG&type=1&model=galaxyS3&osVersion=6.8"; 
        Map<String, String> params = new HashMap<String, String>();
        /*params.put("regId", regId);
        params.put("name", name);
        params.put("email", email);*/
         
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
                if (status != 200) {
                  throw new IOException("Post failed with error code " + status);
                }
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
          }

}
