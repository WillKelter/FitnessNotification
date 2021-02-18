package ru.willk.fitnessnotification.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import ru.willk.fitnessnotification.KEY_ID
import ru.willk.fitnessnotification.domain.ReminderLocalRepository

class AlarmReceiver : BroadcastReceiver() {
    private val TAG = AlarmReceiver::class.java.simpleName
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive() called with: context = [$context], intent = [$intent]")
        if (context != null && intent != null) {
            if (intent.extras != null) {
                val reminderData =
                    ReminderLocalRepository(context).getReminderById(intent.extras!!.getLong(KEY_ID))
                if (reminderData != null) {
                    NotificationHelper.createNotificationForWorkout(context, reminderData)
                }
            }
        }
    }
}