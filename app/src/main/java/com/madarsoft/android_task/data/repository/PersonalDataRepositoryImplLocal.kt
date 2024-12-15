package com.madarsoft.android_task.data.repository

import com.madarsoft.android_task.data.local.dao.PersonalDataDao
import com.madarsoft.android_task.data.local.entity.PersonalDataEntity
import com.madarsoft.android_task.domain.repository.LocalPersonalDataRepository
import com.madarsoft.android_task.util.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PersonalDataRepositoryImplLocal @Inject constructor(
    private val personalDataDao: PersonalDataDao
) : LocalPersonalDataRepository {
    override suspend fun addPersonalDataToDatabase(personalData: PersonalDataEntity): Flow<DataState<PersonalDataEntity>> {
        personalDataDao.insertPersonalData(personalData)
    }



    override suspend fun fetchPersonalDataByIdFromLocal(id: Int): Flow<DataState<PersonalDataEntity>> {
        return personalDataDao.fetchPersonalDataById(id)
    }
}




