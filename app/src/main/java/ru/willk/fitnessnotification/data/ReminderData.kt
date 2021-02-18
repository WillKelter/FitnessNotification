package ru.willk.fitnessnotification.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "reminder_data")
@Parcelize
data class ReminderData(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String? = null,
    var type: WorkoutType = WorkoutType.Swimming,
    var hour: Int = 0,
    var minute: Int = 0,
    var days: List<String?>? = null
) : Parcelable
