package ru.willk.fitnessnotification.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import ru.willk.fitnessnotification.KEY_ID
import ru.willk.fitnessnotification.R
import ru.willk.fitnessnotification.data.ReminderData
import java.util.*

object AlarmScheduler {


    @RequiresApi(Build.VERSION_CODES.N)
    private fun scheduleAlarm(
        reminderData: ReminderData,
        dayOfWeek: Int,
        alarmIntent: PendingIntent?,
        alarmMgr: AlarmManager
    ) {
        // 1 Set up the time to schedule the alarm
        val datetimeToAlarm = Calendar.getInstance(Locale.getDefault())
        datetimeToAlarm.timeInMillis = System.currentTimeMillis()
        datetimeToAlarm.set(Calendar.HOUR_OF_DAY, reminderData.hour)
        datetimeToAlarm.set(Calendar.MINUTE, reminderData.minute)
        datetimeToAlarm.set(Calendar.SECOND, 0)
        datetimeToAlarm.set(Calendar.MILLISECOND, 0)
        datetimeToAlarm.set(Calendar.DAY_OF_WEEK, dayOfWeek)
        // 2 Compare the datetimeToAlarm to today
        val today = Calendar.getInstance(Locale.getDefault())
        if (shouldNotifyToday(dayOfWeek, today, datetimeToAlarm)) {
            // schedule for today
            alarmMgr.setRepeating(
                AlarmManager.RTC_WAKEUP,
                datetimeToAlarm.timeInMillis, (1000 * 60 * 60 * 24 * 7).toLong(), alarmIntent
            )
            return
        }
        // 3 schedule 1 week out from the day
        datetimeToAlarm.roll(Calendar.WEEK_OF_YEAR, 1)
        alarmMgr.setRepeating(
            AlarmManager.RTC_WAKEUP,
            datetimeToAlarm.timeInMillis, (1000 * 60 * 60 * 24 * 7).toLong(), alarmIntent
        )
    }

    private fun createPendingIntent(context: Context, reminderData: ReminderData, day: String?): PendingIntent? {
        // 1 create the intent using a unique type
        val intent = Intent(context.applicationContext, AlarmReceiver::class.java).apply {
            // 2
            type = "$day-${reminderData.name}-${reminderData.type.name}"
            // 3
            putExtra(KEY_ID, reminderData.id)
        }

        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    fun scheduleAlarmsForReminder(context: Context, reminderData: ReminderData) {

        // get the AlarmManager reference
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Schedule the alarms based on the days to administer the medicine
        val days = context.resources.getStringArray(R.array.days)
        if (reminderData.days != null) {
            for (index in reminderData.days!!.indices) {

                val day = reminderData.days!![index]
                if (day != null) {

                    // get the PendingIntent for the alarm
                    val alarmIntent = createPendingIntent(context, reminderData, day)

                    // schedule the alarm
                    val dayOfWeek = getDayOfWeek(days, day)
                    scheduleAlarm(reminderData, dayOfWeek, alarmIntent, alarmMgr)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun shouldNotifyToday(dayOfWeek: Int, today: Calendar, datetimeToAlarm: Calendar): Boolean {
        return dayOfWeek == today.get(Calendar.DAY_OF_WEEK) &&
                today.get(Calendar.HOUR_OF_DAY) <= datetimeToAlarm.get(Calendar.HOUR_OF_DAY) &&
                today.get(Calendar.MINUTE) <= datetimeToAlarm.get(Calendar.MINUTE)
    }

    private fun getDayOfWeek(days: Array<String>, dayOfWeek: String): Int {
        return when {
            dayOfWeek.equals(days[0], ignoreCase = true) -> Calendar.MONDAY
            dayOfWeek.equals(days[1], ignoreCase = true) -> Calendar.TUESDAY
            dayOfWeek.equals(days[2], ignoreCase = true) -> Calendar.WEDNESDAY
            dayOfWeek.equals(days[3], ignoreCase = true) -> Calendar.THURSDAY
            dayOfWeek.equals(days[4], ignoreCase = true) -> Calendar.FRIDAY
            dayOfWeek.equals(days[5], ignoreCase = true) -> Calendar.SATURDAY
            dayOfWeek.equals(days[6], ignoreCase = true) -> Calendar.SUNDAY
            else -> Calendar.MONDAY
        }
    }

}