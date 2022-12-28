package com.example.sunflowerprs.repository

import com.example.sunflowerprs.model.PullReqModel
import com.example.sunflowerprs.model.Resource

interface MainRepository {

    suspend fun getClosedPullRequests(page : Int) : Resource<ArrayList<PullReqModel>>
}