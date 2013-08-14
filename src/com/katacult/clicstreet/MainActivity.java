package com.katacult.clicstreet;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends Activity {
  static final LatLng VICTOR = new LatLng(45.1889, 5.7244);
  static final LatLng FNAC = new LatLng(45.19037, 5.72632);
  static final LatLng ETAM = new LatLng(45.19185, 5.728369);
  static final LatLng ARTHAUD = new LatLng(45.19163, 5.72842);
  private GoogleMap map;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    setContentView(R.layout.activity_main);
    map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
        .getMap();
    Marker fnac = map.addMarker(new MarkerOptions().position(FNAC)
            .title("FNAC")
            .icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.shopping)));
    Marker arthaud = map.addMarker(new MarkerOptions().position(ARTHAUD)
            .title("Arthaud")
            .icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.culture)));
    Marker etam = map.addMarker(new MarkerOptions().position(ETAM)
            .title("Etam Lingerie")
            .icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.slip)));
    // Move the camera instantly to hamburg with a zoom of 15.
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(VICTOR, 16));
  }



} 