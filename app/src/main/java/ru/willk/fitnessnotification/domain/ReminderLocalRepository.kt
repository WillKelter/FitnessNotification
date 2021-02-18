package ru.willk.fitnessnotification.domain

import android.content.Context
import ru.willk.fitnessnotification.FitnessApp
import ru.willk.fitnessnotification.data.ReminderData

class ReminderLocalRepository(val context: Context?) {

    private val roomDatabase = (context?.applicationContext as FitnessApp).getDatabase()
    private val dao = roomDatabase.reminderDataDao()

    fun getReminders(): List<ReminderData> {
        return dao.getReminderData()
    }

    fun getReminderById(id: Long): ReminderData? {
        return dao.getReminderById(id)
    }

    fun saveReminder(reminderData:ReminderData):Long{
        return dao.insert(reminderData)
    }
}