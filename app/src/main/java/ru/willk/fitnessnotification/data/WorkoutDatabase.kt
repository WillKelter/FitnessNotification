package ru.willk.fitnessnotification.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.willk.fitnessnotification.data.converters.DaysConverter
import ru.willk.fitnessnotification.data.converters.WorkoutTypeConverter

@Database(entities = [ReminderData::class], version = 1)
@TypeConverters(WorkoutTypeConverter::class, DaysConverter::class)
abstract class WorkoutDatabase : RoomDatabase() {
    abstract fun reminderDataDao(): ReminderDao
}
