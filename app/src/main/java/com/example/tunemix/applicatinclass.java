package com.example.tunemix;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class applicatinclass extends Application
{
    public static final String CHANNEL_ID_1="channel1";
    public static final String CHANNEL_ID_2="channel2";
    public static final String ACTION_PREVIOUS="actionprevious";
    public static final String ACTION_NEXT="actionnext";
    public static final String ACTION_PLAY="actionplay";


    @Override
    public void onCreate() {
        super.onCreate();
        createnotificationchannel();
    }

    private void createnotificationchannel() {

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)//greter than oreo
        {
            NotificationChannel channel1=new NotificationChannel(CHANNEL_ID_1,"channel(1)", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("channel1 description");

            NotificationChannel channel2=new NotificationChannel(CHANNEL_ID_2,"channel(2)", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("channel2 description");

            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel1);
            notificationManager.createNotificationChannel(channel2);


        }
    }
}
