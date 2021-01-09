package com.example.notekeeperkotlin

import android.app.*
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color

class NotificationHelper(val context: Context) : ContextWrapper(context) {
    private val channel1Id = "Channel1Id"
    private val channel1Name = "Channel One"
    val channel2Id = "Channel2Id"
    val channel2Name = "Channel 2"
    private lateinit var manager: NotificationManager
    lateinit var builder: Notification.Builder
    lateinit var notificationChannel: NotificationChannel

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
        } else {
            builder = Notification.Builder(context)
        }


    }


}