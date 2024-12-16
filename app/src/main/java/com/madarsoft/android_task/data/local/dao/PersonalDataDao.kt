package com.madarsoft.android_task.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.madarsoft.android_task.data.local.entity.PersonalDataEntity

@Dao
interface PersonalDataDao {

    @Insert
    suspend fun insertPersonalData(personalData: PersonalDataEntity)

    @Query("SELECT * FROM personal_data WHERE id = :id")
    fun fetchPersonalDataById(id: Int): PersonalDataEntity
}
