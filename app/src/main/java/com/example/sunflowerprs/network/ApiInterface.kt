package com.example.sunflowerprs.network

import com.example.sunflowerprs.model.PullReqModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("pulls")
    suspend fun getClosedPRs(
        @Query("state") state : String= "closed",
        @Query("per_page") per_page : Int = 10,
        @Query("page") page : Int
    ) : Response<ArrayList<PullReqModel>>

}