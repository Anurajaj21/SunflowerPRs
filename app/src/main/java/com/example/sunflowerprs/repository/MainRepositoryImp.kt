package com.example.sunflowerprs.repository

import com.example.sunflowerprs.model.PullReqModel
import com.example.sunflowerprs.model.Resource
import com.example.sunflowerprs.network.ApiInterface

class MainRepositoryImp(private val apiInterface: ApiInterface): MainRepository {

    override suspend fun getClosedPullRequests(page: Int): Resource<ArrayList<PullReqModel>> {
        val result = apiInterface.getClosedPRs(page = page)
        return if(result.isSuccessful) Resource.Success(result.body() as ArrayList<PullReqModel>)
        else Resource.Error(null, Exception(result.message()))
    }
}