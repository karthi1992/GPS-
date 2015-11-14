package com.org.linux.camera;

import android.os.Bundle;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
       private TextView latitude;
	   private TextView longitude;
	   private TextView choice;
	   private CheckBox fineAcc;
	   private Button choose;
	   private TextView provText;
	   private LocationManager locationManager;
	   private String provider;
	   private MyLocationListener mylistener;
	   private Criteria criteria;	
 
 private static final int CAPTURE_IMAGE_CAPTURE_CODE = 0;
 Intent i;
 private ImageButton ib;

  @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main);
  ib = (ImageButton) findViewById(R.id.buttonToast);
  
  latitude = (TextView) findViewById(R.id.lat);
  longitude = (TextView) findViewById(R.id.lon);
  provText = (TextView) findViewById(R.id.prov);
  choice = (TextView) findViewById(R.id.choice);
  fineAcc = (CheckBox) findViewById(R.id.fineAccuracy);
  choose = (Button) findViewById(R.id.chooseRadio);

   ib.setOnClickListener(new OnClickListener() {
   @Override
   public void onClick(View v) {
    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    startActivityForResult(i, CAPTURE_IMAGE_CAPTURE_CODE);
   }
  });
   
   // Get the location manager
   locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
   // Define the criteria how to select the location provider
   criteria = new Criteria();
   criteria.setAccuracy(Criteria.ACCURACY_COARSE);	//default

   // user defines the criteria
   choose.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
         // TODO Auto-generated method stub
         if(fineAcc.isChecked()){
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            choice.setText("fine accuracy selected");
         }else {
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            choice.setText("coarse accuracy selected");
         }
      }
   });
   criteria.setCostAllowed(false);
   // get the best provider depending on the criteria
   provider = locationManager.getBestProvider(criteria, false);

   // the last known location of this provider
   Location location = locationManager.getLastKnownLocation(provider);

   mylistener = new MyLocationListener();

   if (location != null) {
      mylistener.onLocationChanged(location);
   } else {
      // leads to the settings because there is no last known location
      Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
      startActivity(intent);
   }
   // location updates: at least 1 meter and 200millsecs change
   locationManager.requestLocationUpdates(provider, 200, 1, mylistener);
   
 }
 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  if (requestCode == CAPTURE_IMAGE_CAPTURE_CODE) {
   if (resultCode == RESULT_OK) {
    Toast.makeText(this, "Image Captured", Toast.LENGTH_LONG).show();
   } else if (resultCode == RESULT_CANCELED) {
    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
   }
  }
 }
 @Override
 public boolean onCreateOptionsMenu(Menu menu) {
  // Inflate the menu; this adds items to the action bar if it is present.
  getMenuInflater().inflate(R.menu.main, menu);
  return true;
 }
 
 private class MyLocationListener implements LocationListener {

     @Override
     public void onLocationChanged(Location location) {
        // Initialize the location fields
        latitude.setText("Latitude: "+String.valueOf(location.getLatitude()));
        longitude.setText("Longitude: "+String.valueOf(location.getLongitude()));
        provText.setText(provider + " provider has been selected.");

        Toast.makeText(MainActivity.this,  "Location changed!",
                Toast.LENGTH_SHORT).show();
     }

     @Override
     public void onStatusChanged(String provider, int status, Bundle extras) {
        Toast.makeText(MainActivity.this, provider + "'s status changed to "+status +"!",
                Toast.LENGTH_SHORT).show();
     }

     @Override
     public void onProviderEnabled(String provider) {
        Toast.makeText(MainActivity.this, "Provider " + provider + " enabled!",
                Toast.LENGTH_SHORT).show();

     }

     @Override
     public void onProviderDisabled(String provider) {
        Toast.makeText(MainActivity.this, "Provider " + provider + " disabled!",
                Toast.LENGTH_SHORT).show();
     }
  }

}
