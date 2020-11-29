package com.example.aawaz;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.aawaz.Database.PhoneContract;
import com.example.aawaz.Database.PhoneDbHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class TrackerService extends Service {

    private static final String TAG = TrackerService.class.getSimpleName();
    private String MY_KEY = "123";
    private PhoneDbHelper mDbHelper=new PhoneDbHelper(this);


    @Override
    public IBinder onBind(Intent intent) {return null;}

    @Override
    public void onCreate() {
        super.onCreate();
//        buildNotification();
//        loginToFirebase();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1001,getNotification());
        Log.d(TAG,"onStartCommand: called");
        requestLocationUpdates();
//        return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    private Notification getNotification() {
        Log.d("TrackerService","inside buildNotification()");
        String stop = "stop";
        registerReceiver(stopReceiver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,App.CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_text))
                .setContentIntent(broadcastIntent)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_tracker);
        return builder.build();
    }

    @Override
    public void onDestroy() {
//        Toast.makeText(this,"Service is stopped",Toast.LENGTH_LONG).show();
        super.onDestroy();
        Log.d(TAG,"onDestroy: called");
        stopForeground(true);
        stopSelf();
    }

    private void buildNotification() {
        Log.d("TrackerService","inside buildNotification()");
        String stop = "stop";
        registerReceiver(stopReceiver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);
        // Create the persistent notification
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setColor(getResources().getColor(R.color.notificationColor))
                .setContentText(getString(R.string.notification_text))
                .setOngoing(true)
                .setContentIntent(broadcastIntent)
                .setSmallIcon(R.drawable.women);
        notificationManager.notify(1, builder.build());
//        startForeground(1, builder.build());

    }

    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "received stop broadcast");
            // Stop the service when the notification is tapped
            unregisterReceiver(stopReceiver);
            stopSelf();
        }
    };

    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();
        request.setInterval(8000);
        request.setFastestInterval(4000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        String mKey=getKey();
        final String path = getString(R.string.firebase_path) + "/" + mKey;
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // Request location updates and when an update is
            // received, store the location in Firebase
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        Log.d(TAG, "location update " + location);
                        ref.setValue(location);
                    }
                }
            }, null);
        }
    }

    private String getKey(){
        String key="";
        SQLiteDatabase db=mDbHelper.getReadableDatabase();
        Cursor cursor=db.query(PhoneContract.ItemEntry.LOCAL_TABLE_NAME,null
                ,null,null,null,null,null);

        if(cursor.moveToFirst()) {
            int userIdColumnIndex = cursor.getColumnIndex(PhoneContract.ItemEntry.COLUMN_USER_ID);
            key=cursor.getString(userIdColumnIndex);
        }
        cursor.close();
        return key;
    }
}