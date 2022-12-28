package com.example.sunflowerprs.model

sealed class Resource<out T>(
    val data: T? = null,
    val exception: Exception? = null
) {
    class Success<out T>(data: T) : Resource<T>(data)
    class Error<out T>(data: T?, e: Exception?) : Resource<T>(data, e)
    class Loading<out T>(data: T?) : Resource<T>(data)
}