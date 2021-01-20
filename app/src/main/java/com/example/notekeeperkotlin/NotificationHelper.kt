package com.example.notekeeperkotlin

import android.app.*
import android.content.Context
import android.content.ContextWrapper
import android.graphics.*

class NotificationHelper(
    val context: Context,
    private val notificationText: String,
    private val noteTitle: String,
    private val noteId: Int
) : ContextWrapper(context) {
    private val channel1Id = "Channel1Id"
    private val channel1Name = "Channel One"
    private lateinit var manager: NotificationManager
    private lateinit var builder: Notification.Builder
    private lateinit var notificationChannel: NotificationChannel
    private val picture: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.logo)


    init {
        createChannel()
        manager.notify(1234, builder.build())
    }

    private fun createChannel() {
        manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(
                channel1Id,
                channel1Name,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)
            manager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(context, channel1Id)
                .setSmallIcon(R.drawable.ic_baseline_assignment_24)
                .setContentTitle("Review Note")
                .setContentText(notificationText)
                .setLargeIcon(picture)
                .setStyle(
                    Notification.BigTextStyle()
                        .bigText(notificationText)
                        .setBigContentTitle(noteTitle)
                        .setSummaryText("Review Note")
                )
        } else {
            builder = Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_baseline_assignment_24)
                .setContentTitle("Review Note")
                .setContentText(notificationText)
                .setLargeIcon(picture)
                .setDefaults(Notification.DEFAULT_ALL)
        }


    }


}