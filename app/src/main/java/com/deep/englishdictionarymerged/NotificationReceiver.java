package com.deep.englishdictionarymerged;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import static android.support.v4.content.res.TypedArrayUtils.getString;


public class NotificationReceiver extends BroadcastReceiver {

    DatabaseHelper myDbHelper;
    Cursor c=null;
    String clicked_word = "GIRL";
    String CHANNEL_ID = "my_channel_01";

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent notificationIntent = new Intent(context,WordMeaning.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(WordMeaning.class);
        stackBuilder.addNextIntent(notificationIntent);
        Bundle b = new Bundle();


        myDbHelper = new DatabaseHelper(context);
        try {
            myDbHelper.openDatabase();
        }catch (SQLException e){
            e.printStackTrace();
        }

        c= myDbHelper.randomWord();

        if(c.moveToFirst()){
            clicked_word = c.getString(c.getColumnIndex("en_word"));

        }
        else{
            clicked_word = "GIRL";
        }

        b.putString("en_word", clicked_word);
        notificationIntent.putExtras(b);


        PendingIntent pendingIntent =stackBuilder.getPendingIntent(100,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID,"EnglishDictionary",importance);
            assert notificationManager!= null;
            notificationManager.createNotificationChannel(mChannel);
        }

        android.support.v4.app.NotificationCompat.Builder builder =
                new android.support.v4.app.NotificationCompat.Builder(context,CHANNEL_ID);

                Notification notification= builder.setContentTitle("New word ")
                        .setChannelId(CHANNEL_ID)
                .setContentText("Here is new word of the day")
                .setTicker("New Message Alert!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent).build();


               notificationManager.notify(100,notification);


    }
}
