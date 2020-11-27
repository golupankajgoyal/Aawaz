package com.example.aawaz;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class TrackerActivity extends AppCompatActivity{

    private static final int PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        ImageView image1 = (ImageView)findViewById(R.id.textView);
        ImageView image2 = (ImageView)findViewById(R.id.textView1);
        ImageView image3 = (ImageView)findViewById(R.id.textView2);
        ImageView image4 = (ImageView)findViewById(R.id.textView3);



        Glide
                .with(this)
                .load(R.drawable.woman)
                .centerCrop()
                .into(image1);

        Glide
                .with(this)
                .load(R.drawable.map)
                .centerCrop()
                .into(image2);
        Glide
                .with(this)
                .load(R.drawable.emergency)
                .centerCrop()
                .into(image3);
        Glide
                .with(this)
                .load(R.drawable.logout)
                .centerCrop()
                .into(image4);

        // Check GPS is enabled
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Check location permission is granted - if it is, start
        // the service, otherwise request the permission
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            startTrackerService();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }
    }

    private void startTrackerService() {
        ContextCompat.startForegroundService(this,new Intent(this, TrackerService.class));
//        finish();
    }

    private void stopTrackerService() {
        stopService(new Intent(this, TrackerService.class));
//        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Start the service when the permission is granted
            startTrackerService();
        } else {
            finish();
        }
    }


    public void Profile(View view) {
    }

    public void Maps(View view) {
    }

    public void Location(View view) {
    }

    public void logout(View view) {
    }
}