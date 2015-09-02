package com.katacult.clicstreet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class HomeActivity extends FragmentActivity {

public ArrayAdapter<String> adapter;
public AutoCompleteTextView textView;
 GoogleMap map;
final LatLng fromPosition = new LatLng(45.19302, 5.72950);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
		        .getMap();

	    map.setMyLocationEnabled(true);
	    map.getUiSettings().setRotateGesturesEnabled(false);
	    map.moveCamera(CameraUpdateFactory.newLatLngZoom(fromPosition, 16));

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.list_item);
        final AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteTextView1);
        adapter.setNotifyOnChange(true);
        textView.setAdapter(adapter);
         textView.addTextChangedListener(new TextWatcher() {

public void onTextChanged(CharSequence s, int start, int before, int count) {
if (count%3 == 1) {
adapter.clear();
                GetPlaces task = new GetPlaces();
                        //now pass the argument in the textview to the task
                                task.execute(textView.getText().toString());
        }
}

public void beforeTextChanged(CharSequence s, int start, int count,
int after) {
// TODO Auto-generated method stub

}

public void afterTextChanged(Editable s) {

}
});
	}
class GetPlaces extends AsyncTask<String, Void, ArrayList<String>>
{

@Override
               // three dots is java for an array of strings
protected ArrayList<String> doInBackground(String... args)
{

Log.d("gottaGo", "doInBackground");

ArrayList<String> predictionsArr = new ArrayList<String>();

try
{

            URL googlePlaces = new URL(
            // URLEncoder.encode(url,"UTF-8");
                    "https://maps.googleapis.com/maps/api/place/autocomplete/json?input="+ URLEncoder.encode(args[0].toString(), "UTF-8") +"&types=geocode&language=en&sensor=true&key=AIzaSyBzDHIChX3dwIM6kgdl_mK5NEn5D2Apnp0");
            URLConnection tc = googlePlaces.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    tc.getInputStream()));

            String line;
            StringBuffer sb = new StringBuffer();
                            //take Google's legible JSON and turn it into one big string.
            while ((line = in.readLine()) != null) {
            sb.append(line);
            }
                            //turn that string into a JSON object
            JSONObject predictions = new JSONObject(sb.toString());
                           //now get the JSON array that's inside that object           
            JSONArray ja = new JSONArray(predictions.getString("predictions"));

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = (JSONObject) ja.get(i);
                                    //add each entry to our array
                    predictionsArr.add(jo.getString("description"));
                }
} catch (IOException e)
{

Log.e("YourApp", "GetPlaces : doInBackground", e);

} catch (JSONException e)
{

Log.e("YourApp", "GetPlaces : doInBackground", e);

}

return predictionsArr;

}

//then our post

@Override
protected void onPostExecute(ArrayList<String> result)
{

Log.d("YourApp", "onPostExecute : " + result.size());
//update the adapter
adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.list_item);
adapter.setNotifyOnChange(true);
//attach the adapter to textview
textView.setAdapter(adapter);

for (String string : result)
{

Log.d("YourApp", "onPostExecute : result = " + string);
adapter.add(string);
adapter.notifyDataSetChanged();

}

Log.d("YourApp", "onPostExecute : autoCompleteAdapter" + adapter.getCount());

}

}

}