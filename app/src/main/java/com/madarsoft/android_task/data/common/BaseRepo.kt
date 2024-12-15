package com.madarsoft.android_task.data.common

import com.madarsoft.android_task.util.DataState
import com.telda.movieApp.domain.model.CustomError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class BaseRepo {
    // Room database operation handler
    inline fun <T> performDatabaseCall(
        crossinline databaseCall: suspend () -> T
    ): Flow<DataState<T>> = flow {
        try {
            // Emit "Processing" state first
            emit(DataState.Processing)

            // Perform the database operation
            val result = databaseCall()

            // Emit successful result
            emit(DataState.Success(result))

            // Emit "Idle" state after "Success"
            emit(DataState.Idle)
        } catch (e: Exception) {
            // Log the exception
            println(e)

            // Emit "Idle"
            emit(DataState.Idle)

            // Emit database error state
            emit(DataState.Error(CustomError(e.localizedMessage ?: "Database Error", -1)))
        }
    }
}