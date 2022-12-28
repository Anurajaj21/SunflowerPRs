package com.example.sunflowerprs.network

import com.example.sunflowerprs.model.Resource
import retrofit2.Response

abstract class SafeApiRequest {
    suspend fun <T> executeApiCall(asyncFunc: suspend () -> Response<T>): Resource<T?> {
        return try {
            val response = asyncFunc.invoke()
            if (response.isSuccessful) {
                Resource.Success(response.body())
            } else {
                if (response.code() == 403) Resource.Error(
                    null,
                    Exception("Api request limit exceed")
                )
                else Resource.Error(null, Exception(response.message() ?: "Something went wrong"))
            }
        } catch (exception: Exception) {
            Resource.Error(null, exception)
        }
    }
}