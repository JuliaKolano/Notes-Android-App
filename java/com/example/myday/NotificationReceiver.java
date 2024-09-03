package com.example.myday;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    // DOESN'T WORK :(
    public void onReceive(Context context, Intent intent) {

        // specify the notification channel for API >= 26
        NotificationChannel channel = new NotificationChannel("0", "Task reminders", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Reminders for uncompleted tasks");
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        // receive the intent with extra info
        String taskName = intent.getStringExtra("taskName");

        // create the notification
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, "taskChannel")
                        .setContentTitle("MyDay")
                        .setContentText("'" + taskName + "' is supposed to be done in an hour")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // display the notification
        notificationManager.notify(0, builder.build());
    }
}
