package com.example.aquariummanager.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

@androidx.room.Database(entities = [AquariumItem::class, Item::class, Task::class, Measurement::class] ,version = 24)
abstract class Database : RoomDatabase() {
    abstract fun aquariumDao(): AquariumDao
    abstract fun itemDao(): ItemDao
    abstract fun taskDao(): TaskDao
    abstract fun measurementDao(): MeasurementDao

    companion object{
        @Volatile
        private var INSTANCE: Database? = null

        /**
         * vracia inštanciu trieda Database
         *
         * @param context context pre databázu
         * @return inštancia triedy
         */
        fun getInstance(context: Context): Database {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    Database::class.java, "aquarium_database"
                )
                    .fallbackToDestructiveMigration().allowMainThreadQueries()
                    .build()
                    .also { INSTANCE = it }
            }
        }

    }
}