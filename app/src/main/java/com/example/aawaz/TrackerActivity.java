package com.example.aawaz;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.aawaz.Database.PhoneContract;
import com.example.aawaz.Database.PhoneDbHelper;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class TrackerActivity extends Activity {

    private static final int PERMISSIONS_REQUEST = 1;
    private Button mapButton;
    private Button shareButton;
    private String r1Phone;
    private String r2Phone;
    private PhoneDbHelper mDbHelper=new PhoneDbHelper(this);
    private String mUserId;
    private static char messageKey='9';
    ImageView image1;
    ImageView image2;
    ImageView image3;
    ImageView image4;
    ImageView image5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        image1 = (ImageView)findViewById(R.id.textView);

        image2 = (ImageView)findViewById(R.id.textView1);

        image3 = (ImageView)findViewById(R.id.textView2);

        image4 = (ImageView)findViewById(R.id.textView3);

        image5 = (ImageView)findViewById(R.id.textView4);




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

        Glide
                .with(this)
                .load(R.drawable.alarm)
                .centerCrop()
                .into(image5);

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TrackerActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TrackerActivity.this,MapsActivity.class);
                startActivity(intent);
            }
        });

        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TrackerActivity.this,SmsListActivity.class);
                startActivity(intent);
            }
        });

        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(TrackerActivity.this,"LogOut Successful",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TrackerActivity.this, LogInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        image5.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View v) {
//                Code for Emergency Button
                Toast.makeText(TrackerActivity.this,"You have clicked emergency button", Toast.LENGTH_SHORT).show();
                showInfo();
                String address1;
                String address2;
                r1Phone="+91"+r1Phone;
                r2Phone="+91"+r2Phone;
                MyMessage(r1Phone);
                MyMessage(r2Phone);
            }
        });
        // Check GPS is enabled
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show();
            finish();
        }

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

    private void showInfo(){

        SQLiteDatabase db=mDbHelper.getReadableDatabase();
        Cursor cursor=db.query(PhoneContract.ItemEntry.LOCAL_TABLE_NAME,null
                ,null,null,null,null,null);

        if(cursor.moveToFirst()) {

            int phoneColumnIndex = cursor.getColumnIndex(PhoneContract.ItemEntry.COLUMN_CONTACT_NO);
            int userIdColumnIndex = cursor.getColumnIndex(PhoneContract.ItemEntry.COLUMN_USER_ID);
            int r1PhoneColumnIndex = cursor.getColumnIndex(PhoneContract.ItemEntry.COLUMN_RELATIVE_1);
            int r2PhoneColumnIndex = cursor.getColumnIndex(PhoneContract.ItemEntry.COLUMN_RELATIVE_2);
            String phonNo = cursor.getString(phoneColumnIndex);
            String userId = cursor.getString(userIdColumnIndex);
            String r1 = cursor.getString(r1PhoneColumnIndex);
            String r2 = cursor.getString(r2PhoneColumnIndex);
            r1Phone=r1;
            r2Phone=r2;
            mUserId=userId;
        }
        cursor.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public void MyMessage(String phoneNo){

        String phoneNumber =phoneNo;
        String Message = "Your Close One is in danger. Enter code "+ mUserId +" in Maps to trace the location.";
        AESenc cipher = new AESenc();

        try {
            Message=cipher.encrypt(Message);
            Message+=messageKey;
        } catch (Exception e) {
            e.printStackTrace();
        }

        final ArrayList<Integer> simCardList = new ArrayList<>();

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if(permissionCheck == PackageManager.PERMISSION_GRANTED){
            SubscriptionManager subscriptionManager;
            subscriptionManager = SubscriptionManager.from(this);
            final List<SubscriptionInfo> subscriptionInfoList = subscriptionManager
                    .getActiveSubscriptionInfoList();
            for (SubscriptionInfo subscriptionInfo : subscriptionInfoList) {
                int subscriptionId = subscriptionInfo.getSubscriptionId();
                simCardList.add(subscriptionId);
            }
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 0);
        }
            SmsManager smsManager = SmsManager.getSmsManagerForSubscriptionId(simCardList.get(0));
            smsManager.sendTextMessage(phoneNumber, null, Message, null, null);
            Toast.makeText(this, "Message Send", Toast.LENGTH_SHORT).show();

    }
}