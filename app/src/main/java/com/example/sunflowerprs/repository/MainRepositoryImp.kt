package com.example.sunflowerprs.repository

import com.example.sunflowerprs.model.PullReqModel
import com.example.sunflowerprs.model.Resource
import com.example.sunflowerprs.network.ApiInterface
import com.example.sunflowerprs.network.SafeApiRequest

class MainRepositoryImp(private val apiInterface: ApiInterface) : MainRepository, SafeApiRequest() {

    override suspend fun getClosedPullRequests(page: Int): Resource<ArrayList<PullReqModel>?> {
        return executeApiCall { apiInterface.getClosedPRs(page = page) }
    }
}