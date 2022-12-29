package com.example.sunflowerprs.repository

import com.example.sunflowerprs.model.PullReqModel
import com.example.sunflowerprs.model.Resource
import com.example.sunflowerprs.network.ApiInterface
import com.example.sunflowerprs.network.Retrofit
import com.example.sunflowerprs.network.SafeApiRequest

class MainRepositoryImp : MainRepository, SafeApiRequest() {
    private val apiInterface: ApiInterface = Retrofit.getClient().create(ApiInterface::class.java)

    override suspend fun getClosedPullRequests(page: Int): Resource<ArrayList<PullReqModel>?> {
        return executeApiCall { apiInterface.getClosedPRs(page = page) }
    }
}