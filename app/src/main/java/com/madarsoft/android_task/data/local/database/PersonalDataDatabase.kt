package com.madarsoft.android_task.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.madarsoft.android_task.data.local.dao.PersonalDataDao
import com.madarsoft.android_task.data.local.entity.PersonalDataEntity

@Database(entities = [PersonalDataEntity::class], version = 1, exportSchema = false)
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
                )
                    .fallbackToDestructiveMigration()
                    .addMigrations(MIGRATION_4_5)  // Add the migration here
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Example: Add a new column to the "personal_data" table
        database.execSQL("ALTER TABLE personal_data ADD COLUMN new_column TEXT")
    }
}