package com.example.assigmentapplication;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        getFirebaseMessage(message.getNotification().getTitle(),message.getNotification().getBody());
    }


    @SuppressLint("MissingPermission")
    private void getFirebaseMessage(String title, String massege) {
        NotificationCompat.Builder builder= new NotificationCompat.Builder(this,"myChannel")
                .setSmallIcon(R.drawable.baseline_person_24)
                .setContentTitle(massege)
                .setContentText(title)
                .setAutoCancel(true);
        NotificationManagerCompat manger=NotificationManagerCompat.from(this);
     manger.notify(101,builder.build());


    }

}
