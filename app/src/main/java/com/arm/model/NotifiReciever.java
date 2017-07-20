package com.arm.model;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.arm.nhatki2.LaunchActivity;
import com.arm.nhatki2.R;

import static com.arm.nhatki2.KhoaActivity.saveValue;

/**
 * Created by ARM on 09-Jun-17.
 */

public class NotifiReciever extends BroadcastReceiver {
    public static int NOTIFICATION_ID = 6;
    SaveValue saveValue1;
    public static String WRITE_NHAT_KI = "WRITE_NHAT_KI";
    @Override
    public void onReceive(Context context, Intent intent) {
        saveValue1 = new SaveValue(context);
        Log.d("thong",saveValue1.getSaved_Have_Note_ToDay_Integer()+"");
        if (saveValue1.getSaved_Have_Note_ToDay_Integer() == 0)
        {
            taoThongBaoChuaCoNote(context);
        }
        else 
        {
            //chỉ hiện thông báo 1 lần
            saveValue1.setSaved_Have_Note_ToDay_Integer(0);
        }
    }


    private void taoThongBaoChuaCoNote(Context context) {
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.iconnhatky);
        
        Intent intent = new Intent(context, LaunchActivity.class);
        intent.setAction(WRITE_NHAT_KI);
        PendingIntent notificationContentIntent = PendingIntent
                .getActivity(context, 2000, intent, 0);
        NotificationCompat.Builder shareNotification =
                new NotificationCompat.Builder(context)
                .setContentTitle(context.getResources().getString(R.string.null_nhat_ki))
                .setContentText(context.getResources().getString(R.string.click))
                .setSmallIcon(R.drawable.iconnhatky)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setAutoCancel(true)
                .setContentIntent(notificationContentIntent)
                .setPriority(Notification.PRIORITY_MAX);

        updateNotification(shareNotification.build()
                , NOTIFICATION_ID,context);
    }
    private void updateNotification(Notification notification, int ID, Context context) {
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(ID, notification);
    }
}
