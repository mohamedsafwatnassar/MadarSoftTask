package com.madarsoft.android_task.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personal_data")
data class PersonalDataEntity(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    val username: String,
    val age: String,
    val jobTitle: String,
    val gender: String,
)
