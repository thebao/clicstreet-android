package com.katacult.clicstreet;



import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    public String receivedMessage;
    public String receivedTitle;
    public String receivedShopId;
    public String userName;
    public String userName2;

    Intent innerIntent = new Intent(this, CommerceActivity.class);
    
    public GcmIntentService() {
        super("GcmIntentService");
    }
    public static final String TAG = "GCM Demo";

    @Override
    protected void onHandleIntent(Intent intent) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userName=sharedPreferences.getString("the_user", "guest");
        userName2=sharedPreferences.getString("the_secret", "947be6ef4c5c90cf9f66a461660178a379afddf5");
        Bundle extras = intent.getExtras();
        System.out.println("message received");
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcellingr Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " + extras.toString());
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
        	/*try {
            	JSONArray jsonArray = new JSONArray(extras.toString());
            	} 
		  catch (JSONException e) {
				  Log.e("", "Error processing JSON: "+extras.toString(), e);
		  		}*/
            	//System.out.println("type is "+extras.getClass().getName());
            	
            	receivedMessage = extras.getString("message");
            	if (extras.getString("placename")!=null)
            		receivedTitle = "Clicstreet - "+extras.getString("placename");
            	else
            		receivedTitle = "Clicstreet";
            	receivedShopId = extras.getString("placeid");
                //Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.
                sendNotification(receivedMessage);
                for (String key: extras.keySet())
                {
                  System.out.println("- "+key + "has value "+extras.get(key));
                }
                //Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent innerIntent = new Intent(this, CommerceActivity.class);
        innerIntent.putExtra("secret", userName2);
        innerIntent.putExtra("user", userName);
        innerIntent.putExtra("kshopid", receivedShopId);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                innerIntent, PendingIntent.FLAG_UPDATE_CURRENT );
        //System.out.println("making notification");
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
			        .setSmallIcon(R.drawable.clicstreet)
			        .setContentTitle(receivedTitle)
			        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
			        .setStyle(new NotificationCompat.BigTextStyle()
			        .bigText(msg))
			        .setContentText(msg);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}

