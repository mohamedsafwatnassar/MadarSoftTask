package com.madarsoft.android_task.domain.repository

import com.madarsoft.android_task.data.local.entity.PersonalDataEntity
import com.madarsoft.android_task.util.DataState
import kotlinx.coroutines.flow.Flow

interface LocalPersonalDataRepository {

    suspend fun addPersonalDataToDatabase(personalData: PersonalDataEntity) : Flow<DataState<PersonalDataEntity>>

    suspend fun fetchPersonalDataByIdFromLocal(id: Int):  Flow<DataState<PersonalDataEntity>>
}
