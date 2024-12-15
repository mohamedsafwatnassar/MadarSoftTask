package com.madarsoft.android_task.data.mapper

import com.madarsoft.android_task.data.local.entity.PersonalDataEntity
import com.madarsoft.android_task.domain.model.PersonalData

object PersonalDataMapper {

    fun PersonalDataEntity.toDomainModel(): PersonalData {
        return PersonalData(
            username = this.username,
            age = this.age,
            jobTitle = this.jobTitle,
            gender = this.gender
        )
    }

    fun PersonalData.toDateModel(): PersonalDataEntity {
        return PersonalDataEntity(
            username = this.username,
            age = this.age,
            jobTitle = this.jobTitle,
            gender = this.gender
        )
    }
}