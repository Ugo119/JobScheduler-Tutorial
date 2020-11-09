package com.ugo.android.notificationscheduler;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationJobService extends JobService {
    NotificationManager notificationManager;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";

    @Override
    public boolean onStartJob(JobParameters params) {
        //Create the notification channel
        createNotificationChannel();

        //Set up the notification content intent to launch the app when clicked
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (this, 0, new Intent(this, MainActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder
                (this, PRIMARY_CHANNEL_ID)
                .setContentTitle(getString(R.string.job_service_content_title))
                .setContentText(getString(R.string.job_service_content_text))
                .setSmallIcon(R.drawable.ic_job_running)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(Boolean.TRUE);
        notificationManager.notify(0, builder.build());
        return Boolean.FALSE;
    }

    public void createNotificationChannel() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //Notification channels are only available in OREO and higher.
        //Add a check on the SDK version.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID, getString(R.string.job_service_notification),
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(Boolean.TRUE);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(Boolean.TRUE);
            notificationChannel.setDescription(getString(R.string.notification_channel_description));

            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        //TRUE ensures that if the job fails, you want the job to be rescheduled instead of dropped.
        return Boolean.TRUE;
    }
}
