package com.updkbarito.apik.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.updkbarito.apik.R;
import com.updkbarito.apik.activities.IzinDetailActivity;
import com.updkbarito.apik.activities.MainActivity;
import com.updkbarito.apik.activities.MenuActivity;
import com.updkbarito.apik.activities.SplashActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class CustomFirebaseMessagingService extends FirebaseMessagingService {

    private static String TAG = CustomFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onNewToken(@NonNull String s) {
//        super.onNewToken(s);
        Log.d(TAG, "onNewToken:"+s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String jenis = "internal";
        int izinId = 0;
        Log.d(TAG, "onMessageReceived:" + remoteMessage.getFrom());

        Map<String, String> params = remoteMessage.getData();
        for(Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Log.d(TAG, "onMessageReceived:"+key+":"+value);
            if (key.equals("custom")){
                try {
                    JSONObject custom = new JSONObject(value);
                    if (custom.has("a")){
                        JSONObject izin = custom.getJSONObject("a");
                        if (izin.has("id")){
                            jenis = izin.getString("jenis");
                            izinId = Integer.parseInt(izin.getString("id"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

//        JSONObject object = new JSONObject(params);
//        Log.d(TAG, "JSON_OBJECT:"+object.toString());
//        try {
//            jenis = object.getString("jenis");
//            izinId = Helpers.parseInt(object.getString("izin_id"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        String NOTIFICATION_CHANNEL_ID = "com.updkbarito.apik";

        long pattern[] = {0, 1000, 500, 1000};

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notifikasi",
                        NotificationManager.IMPORTANCE_HIGH);

                notificationChannel.setDescription("");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.setVibrationPattern(pattern);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }

            // to diaplay notification in DND Mode
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID);
                channel.canBypassDnd();
            }

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//            Intent resultIntent = new Intent(this, SplashActivity.class);
            Intent resultIntent = new Intent(this, IzinDetailActivity.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            resultIntent.putExtra("jenis", jenis);
            resultIntent.putExtra("notify", 1);
            resultIntent.putExtra("izin_id", izinId);
            stackBuilder.addNextIntentWithParentStack(resultIntent);
//            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent resultPendingIntent = PendingIntent.getActivity( this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

            notificationBuilder.setAutoCancel(true)
                    .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                    .setContentTitle(getString(R.string.app_name));
            if (remoteMessage.getNotification() != null) notificationBuilder.setContentText(remoteMessage.getNotification().getBody());

            notificationBuilder.setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.logo_pln)
                    .setContentIntent(resultPendingIntent)
                    .setAutoCancel(true);

            notificationManager.notify(1000, notificationBuilder.build());
        }
    }
}
