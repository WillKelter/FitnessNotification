package ru.willk.fitnessnotification

import android.app.Application
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room
import ru.willk.fitnessnotification.data.WorkoutDatabase
import ru.willk.fitnessnotification.data.WorkoutType
import ru.willk.fitnessnotification.notifications.NotificationHelper

class FitnessApp: Application() {

    lateinit var app: FitnessApp
    private lateinit var database: WorkoutDatabase

    override fun onCreate() {
        super.onCreate()

        app = this
        database = Room.databaseBuilder(this, WorkoutDatabase::class.java, "database")
            .allowMainThreadQueries()
            .build()

        NotificationHelper.createNotificationChannel(
            this,
            NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
            getString(R.string.app_name), "Канал Фитнес - приложения."
        )

        NotificationHelper.createNotificationChannel(
            this,
            NotificationManagerCompat.IMPORTANCE_LOW, true,
            WorkoutType.Cycling.name, "Notification channel for Cycling."
        )

        NotificationHelper.createNotificationChannel(
            this,
            NotificationManagerCompat.IMPORTANCE_HIGH, true,
            WorkoutType.Swimming.name, "Notification channel for Swimming."
        )

        NotificationHelper.createNotificationChannel(
            this,
            NotificationManagerCompat.IMPORTANCE_LOW, false,
            WorkoutType.Running.name, "Notification channel for Running"
        )

    }

    fun getInstance(): FitnessApp {
        return app
    }
    fun getDatabase(): WorkoutDatabase {
        return database
    }

}