package com.example.aawaz;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

public class SmsListner extends BroadcastReceiver {

    private String msg_from;
//    private MainActivity mainActivity=new MainActivity();
    private MainActivity main = null;

    @Override
    public void onReceive(Context context, Intent intent){

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            createNotificationChannel(context);
            Bundle bundle = intent.getExtras();
            SmsMessage[] msg = null;
            Intent localIntent = new Intent("custom-action-local-broadcast");
            intent.putExtra("name","Tutorialspoint.com");
//            LocalBroadcastManager.getInstance(mainActivity).sendBroadcast(localIntent);
            if (main!=null){
                main.dataChanged();
            }
            if(bundle != null){
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msg = new SmsMessage[pdus.length];
                    for (int i=0; i<msg.length;i++){
                        msg[i]= SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msg[i].getOriginatingAddress();
                        String msgBody = msg[i].getMessageBody();
                        Toast.makeText(context,msgBody, Toast.LENGTH_SHORT).show();
                        Log.e("Message Recieved",msgBody);
                        notification(context);
                    }

                    }catch(Exception e){
                    Log.d("Exception caught",e.getMessage());
                }
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void notification(Context context)
    {
        int notificationId=1;
        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(context,MainActivity.class);
        resultIntent.putExtra("phoneNumber",msg_from);
// Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
// Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            builder = new NotificationCompat.Builder(context, "sms")
                    .setSmallIcon(R.drawable.women)
                    .setContentTitle("Notification")
                    .setColor(main.getColor(R.color.notificationColor))
                    .setContentText("Someone have send you a message")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(resultPendingIntent)
                    .setAutoCancel(true);
        }

        NotificationManagerCompat notifyManager = NotificationManagerCompat.from(context);

// notificationId is a unique int for each notification that you must define
            notifyManager.notify(notificationId, builder.build());
    }

    public void createNotificationChannel(Context context){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        CharSequence name = "smsdo";
        String description = "chat app";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("sms", name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager =context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        }

    }

    void setMainActivityHandler(MainActivity main){
        this.main=main;
    }


}