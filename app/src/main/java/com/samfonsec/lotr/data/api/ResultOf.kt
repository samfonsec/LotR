package com.samfonsec.lotr.data.api

sealed class ResultOf<out T : Any> {
    class Success<out T : Any>(val data: T) : ResultOf<T>()
    class Error(val exception: Throwable) : ResultOf<Nothing>()
}
