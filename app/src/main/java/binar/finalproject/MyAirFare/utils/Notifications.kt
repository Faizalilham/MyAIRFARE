package binar.finalproject.MyAirFare.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import binar.finalproject.MyAirFare.R
import binar.finalproject.MyAirFare.ui.activities.MainActivity


object Notifications {

    @SuppressLint("UnspecifiedImmutableFlag")
    fun makeStatusNotification(message: String, context: Context) {

        // Make a channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = VERBOSE_NOTIFICATION_CHANNEL_NAME
            val description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java)
        intent.action = java.lang.Long.toString(System.currentTimeMillis())
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(
            context, 0,
            intent, PendingIntent.FLAG_CANCEL_CURRENT
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0))

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
    }
}