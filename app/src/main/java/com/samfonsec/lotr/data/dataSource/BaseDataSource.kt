package com.samfonsec.lotr.data.dataSource

import com.samfonsec.lotr.data.api.ResultOf

abstract class BaseDataSource {

    suspend fun <T : Any> execute(apiReturn: suspend () -> T): ResultOf<T> {
        return try {
            ResultOf.Success(apiReturn())
        } catch (e: Exception) {
            e.printStackTrace()
            ResultOf.Error(e)
        }
    }
}