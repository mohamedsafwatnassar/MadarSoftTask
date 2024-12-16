package com.madarsoft.android_task.data.repository

import com.madarsoft.android_task.data.common.BaseRepo
import com.madarsoft.android_task.data.local.dao.PersonalDataDao
import com.madarsoft.android_task.data.mapper.PersonalDataMapper.toDateModel
import com.madarsoft.android_task.data.mapper.PersonalDataMapper.toDomainModel
import com.madarsoft.android_task.domain.model.PersonalData
import com.madarsoft.android_task.domain.repository.LocalPersonalDataRepository
import com.madarsoft.android_task.util.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PersonalDataRepositoryImplLocal @Inject constructor(
    private val personalDataDao: PersonalDataDao
) : LocalPersonalDataRepository, BaseRepo() {

    override suspend fun addPersonalDataToDatabase(personalData: PersonalData): Flow<DataState<PersonalData>> {
        return performDatabaseCall {
            // Insert the personal data and return the inserted entity
            personalDataDao.insertPersonalData(personalData.toDateModel())
            personalData // Map to domain model before returning
        }
    }

    override suspend fun fetchPersonalDataByIdFromLocal(id: Int): Flow<DataState<PersonalData>> {
        return performDatabaseCall {
            val personalData = personalDataDao.fetchPersonalDataById(id)
            if (personalData != null) {
                personalData.toDomainModel()
            } else {
                // Handle null case (e.g., return an error state)
                throw NullPointerException("PersonalData not found for ID: $id")
            }
        }
    }
}