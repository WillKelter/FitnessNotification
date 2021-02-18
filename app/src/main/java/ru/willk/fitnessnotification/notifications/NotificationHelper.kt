package ru.willk.fitnessnotification.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ru.willk.fitnessnotification.KEY_ID
import ru.willk.fitnessnotification.MainActivity
import ru.willk.fitnessnotification.R
import ru.willk.fitnessnotification.data.ReminderData
import ru.willk.fitnessnotification.data.WorkoutType



object NotificationHelper {

    fun createNotificationChannel(context: Context, importance: Int, showBadge: Boolean, name: String, description: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)
            // Register the channel with the system
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

    }

//    fun createSampleDataNotification(
//        context: Context, title: String, message: String,
//        bigText: String, autoCancel: Boolean
//    ) {
//
//        val channelId = "${context.packageName}-${context.getString(R.string.app_name)}"
//
//        val notificationBuilder = NotificationCompat.Builder(context, channelId).apply {
//            setSmallIcon(R.drawable.ic_bike_24)
//            setContentTitle(title)
//            setContentText(message)
//            setAutoCancel(autoCancel)
//            setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
//            priority = NotificationCompat.PRIORITY_DEFAULT
//
//            val intent = Intent(context, MainActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
//            setContentIntent(pendingIntent)
//
//
//        }
//        val notificationManager = NotificationManagerCompat.from(context)
//        notificationManager.notify(1001, notificationBuilder.build())
//    }

    private fun buildGroupNotification(
        context: Context,
        reminderData: ReminderData
    ): NotificationCompat.Builder {
        val channelId = "${context.packageName}-${reminderData.type.name}"
        return NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_fitnes)
            setContentTitle(reminderData.type.name)
            setContentText(
                context.getString(
                    R.string.group_notification_for,
                    reminderData.type.name
                )
            )
            setStyle(
                NotificationCompat.BigTextStyle().bigText(
                    context.getString(
                        R.string.group_notification_for,
                        reminderData.type.name
                    )
                )
            )
            setAutoCancel(true)
            setGroupSummary(true)
            setGroup(reminderData.type.name)
        }
    }

    fun createNotificationForWorkout(context: Context, reminderData: ReminderData) {


        val groupBuilder = buildGroupNotification(context, reminderData)
        val notificationBuilder = buildNotification(context, reminderData)
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(reminderData.type.ordinal, groupBuilder.build())
        notificationManager.notify(reminderData.id.toInt(), notificationBuilder.build())
    }


    private fun buildNotification(
        context: Context,
        reminderData: ReminderData
    ): NotificationCompat.Builder {

        val channelId = "${context.packageName}-${reminderData.type.name}"

        return NotificationCompat.Builder(context, channelId).apply {

            setSmallIcon(R.drawable.ic_fitnes)

            setContentTitle(reminderData.name)

            setAutoCancel(true)


            val drawable = when (reminderData.type) {
                WorkoutType.Running -> R.drawable.ic_run
                WorkoutType.Cycling -> R.drawable.ic_velo
                else -> R.drawable.ic_swimming
            }
            setLargeIcon(BitmapFactory.decodeResource(context.resources, drawable))

            setContentText("${reminderData.name}")

            setGroup(reminderData.type.name)

                val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(KEY_ID, reminderData.id)
            }

            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            setContentIntent(pendingIntent)
        }
    }
}

