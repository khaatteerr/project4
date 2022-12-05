package com.udacity.project4.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.udacity.project4.BuildConfig
import com.udacity.project4.R
import com.udacity.project4.locationreminders.ReminderDescriptionActivity
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem








private const val NOTIFICATION_CHANNEL_ID = BuildConfig.APPLICATION_ID + ".channel"





fun sendNotification(context: Context, reminderDataItem: ReminderDataItem) {
    val notificationManager = context
        .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

        && notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID)
         == null
    ) {

        val name = context.getString(R.string.app_name)

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            name,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    val intent = ReminderDescriptionActivity.newIntent(context.applicationContext, reminderDataItem)


    val stackBuilder = TaskStackBuilder.create(context)
        .addParentStack(ReminderDescriptionActivity::class.java)
        .addNextIntent(intent)
    val notificationPendingIntent = stackBuilder
        .getPendingIntent(getUniqueId(), PendingIntent.FLAG_UPDATE_CURRENT)


    val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)

        .setSmallIcon(R.mipmap.ic_launcher)

        .setContentTitle(reminderDataItem.title)

        .setContentText(reminderDataItem.location)

        .setContentIntent(notificationPendingIntent)

        .setAutoCancel(true)


        .build()


    notificationManager.notify(getUniqueId(), notification)

}



private fun getUniqueId() = ((System.currentTimeMillis() % 10000).toInt())