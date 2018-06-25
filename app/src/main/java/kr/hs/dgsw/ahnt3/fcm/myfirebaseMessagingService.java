package kr.hs.dgsw.ahnt3.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import kr.hs.dgsw.ahnt3.LeaveFragment;
import kr.hs.dgsw.ahnt3.MainActivity;
import kr.hs.dgsw.ahnt3.NotificationFragment;
import kr.hs.dgsw.ahnt3.R;
import kr.hs.dgsw.ahnt3.Util.DBHelper;

public class myfirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("asdf", remoteMessage.getData().get("idx"));

        Log.d("msg", "onMessageReceived: " + remoteMessage.getData().get("message"));
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";

        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());
        //sendPushNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"));
    }

    private void sendPushNotification(String title, String message, String type, String idx){

        Intent intent = null;
        switch (type){
            case "go_out":
            case "go_sleep":
                intent = new Intent(myfirebaseMessagingService.this, LeaveFragment.class);
                intent.putExtra("noti", type);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            case "notice":
                intent = new Intent(myfirebaseMessagingService.this, NotificationFragment.class);
                intent.putExtra("noti", type);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT );

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(myfirebaseMessagingService.this)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
/*
        PowerManager pm = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakelock = pm.newWakeLock(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG"
        );
        wakelock.acquire(5000);
*/
        notificationManager.notify(1234, notificationBuilder.build());
    }
}
