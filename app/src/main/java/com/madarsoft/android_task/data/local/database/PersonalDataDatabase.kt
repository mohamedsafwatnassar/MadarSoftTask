package com.madarsoft.android_task.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.madarsoft.android_task.data.local.dao.PersonalDataDao
import com.madarsoft.android_task.data.local.entity.PersonalDataEntity

@Database(entities = [PersonalDataEntity::class], version = 2, exportSchema = false)
abstract class PersonalDataDatabase : RoomDatabase() {

    abstract fun personalDataDao(): PersonalDataDao

    companion object {
        @Volatile
        private var INSTANCE: PersonalDataDatabase? = null

        fun getDatabase(context: Context): PersonalDataDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PersonalDataDatabase::class.java,
                    "personal_data_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
