package ru.willk.fitnessnotification.data

import androidx.room.*

@Dao
interface ReminderDao {
    @Query("SELECT * from reminder_data")
    fun getReminderData(): List<ReminderData>
    @Query("SELECT * from reminder_data where id= :id")
    fun getReminderById(id: Long): ReminderData?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(reminder: ReminderData): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(reminders: List<ReminderData>)
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(reminder: ReminderData)
    @Query("DELETE FROM reminder_data")
    fun deleteAll()
    @Query("DELETE FROM reminder_data where id LIKE :id")
    fun deleteById(id: Long)
}
