package app.decathlon.inventoryreader.notifications;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyNotificationService extends FirebaseMessagingService {
    public void onMessageReceived (RemoteMessage remoteMessage){
        Log.e("FIREBASE", remoteMessage.getNotification().getBody());
    }
}
