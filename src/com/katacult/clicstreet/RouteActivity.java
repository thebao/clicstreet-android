package com.katacult.clicstreet;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tyczj.mapnavigator.Directions;
import com.tyczj.mapnavigator.Navigator;
import com.tyczj.mapnavigator.Navigator.OnPathSetListener;
import com.tyczj.mapnavigator.Route;

public class RouteActivity extends FragmentActivity implements LocationListener {

private GoogleMap map;

private TextView mTitle;

// flag for GPS status
boolean isGPSEnabled = false;

// flag for network status
boolean isNetworkEnabled = false;

boolean canGetLocation = false;

Location location; // location
double latitude; // latitude
double longitude; // longitude

// The minimum distance to change Updates in meters
private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

// The minimum time between updates in milliseconds
private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

// Declaring a Location Manager
protected LocationManager locationManager;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route);
		mTitle = (TextView) findViewById(R.id.title);
		String[] locomotion={
				getResources().getString(R.string.motion_car), 
				"", 
				getResources().getString(R.string.motion_bike),
				getResources().getString(R.string.motion_walk)};
		Intent intent = getIntent();
    	Double destLat = intent.getDoubleExtra("destLat", 0.0);
    	Double destLong= intent.getDoubleExtra("destLong", 0.0);
    	String name= intent.getStringExtra("destName");
    	String type= intent.getStringExtra("destType");
    	final String clickedId= intent.getStringExtra("destId");
    	final String userName=intent.getStringExtra("user");
    	final String userName2=intent.getStringExtra("secret");
    	String getIcon= intent.getStringExtra("destIcon");
    	Integer locoMotion = intent.getIntExtra("locoMotion", 0);
    	Double lat=0.0;
    	Double lng=0.0;
    	
    	Location whereAreyou = getLocation();
    	if (whereAreyou!=null){
			 lat=whereAreyou.getLatitude();
			 lng=whereAreyou.getLongitude();
    	}
    	
    	
    	mTitle.setText(locomotion[locoMotion]+" "+name+".");
	 	String truncIcon = getIcon.substring(0, getIcon.length()-4);
	 	String icon = "@drawable/"+truncIcon;
    	LatLng gotoPlace = new LatLng(destLat, destLong);
		//final LatLng fromPosition = new LatLng(45.19302, 5.72950);
    	final LatLng fromPosition = new LatLng(lat, lng);
    	final LatLngBounds.Builder bounds2 = new LatLngBounds.Builder(); 
    	bounds2.include(gotoPlace);
    	bounds2.include(fromPosition);
		map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
		        .getMap();
		if(locoMotion.equals(0))
			{map.setTrafficEnabled(true);}
			//System.out.println("Traffic output is: "+map.isTrafficEnabled());
		    map.setMyLocationEnabled(true);
		    //map.setPadding(0,16,0,0);
		    map.getUiSettings().setRotateGesturesEnabled(false);
		    map.moveCamera(CameraUpdateFactory.newLatLngZoom(gotoPlace, 16));
			final Navigator nav = new Navigator(map,fromPosition,gotoPlace);
			nav.setMode(locoMotion, 1234, 1);
			nav.findDirections(true, false);
			nav.setOnPathSetListener(new OnPathSetListener() {

				@Override
				public void onPathSetListener(Directions directions) {
					Directions routeDir = nav.getDirections();
					Integer routeSize = nav.getDirections().getRoutes().size();
					if (routeSize>0){
						Route lastRoute = routeDir.getRoutes().get(routeSize-1);
						LatLngBounds rteBnds = lastRoute.getMapBounds();

					    map.moveCamera(CameraUpdateFactory.newLatLngBounds(rteBnds, 50));
						Integer duration = lastRoute.getDuration();
						Integer divdMinutes = Math.round((float) duration/60);
						String pluralTime="";
						if (divdMinutes>1){
							pluralTime="s";
						}
						String durationText = Integer.toString(divdMinutes);
						String durationPrint = "\nDurée: "+durationText+" minute"+pluralTime;
						mTitle.append(durationPrint);
						Integer meters = lastRoute.getDistance();
						Integer kilometers = Math.round((float) meters/1000);
						String distance;
						String pluralKilos="";
						if (meters>1000){
							distance = Integer.toString(kilometers);
							if (kilometers>1){
								pluralKilos="s";
							}
							String distanceText = Integer.toString(kilometers);
							String distancePrint = "\nDistance: "+distanceText+" kilomètre"+pluralKilos;
							mTitle.append(distancePrint);
						}
						else{
							String distanceText = Integer.toString(meters);
							String distancePrint =  "\nDistance: "+distanceText+" mètres";
							mTitle.append(distancePrint);
							}
						
					}
					
				}
				
			});

			Marker marker=null;
			int u = getResources().getIdentifier(icon,"drawable", getPackageName());
			  if (u!=0) {
			 marker = map.addMarker(new MarkerOptions()
			  .title(name)
			  .icon(BitmapDescriptorFactory.fromResource(u))
			  .snippet(type)
			  .position(gotoPlace)
		  );}/*
			  map.setOnCameraChangeListener(new OnCameraChangeListener() {
				  
			    @Override
			    public void onCameraChange(CameraPosition arg0) {
	    map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds2.build(), 50));
	    map.setOnCameraChangeListener(null);
			    }
			});*/
	    map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {                      
			        @Override
			        public void onInfoWindowClick(Marker marker) {
			        		Intent in=new Intent(getApplicationContext(),CommerceActivity.class);
			        		in.putExtra("kshopid", clickedId);
			        		in.putExtra("user", userName);
			        		in.putExtra("secret", userName2);
			        		startActivity(in);
			        }});
	    marker.showInfoWindow();

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
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
