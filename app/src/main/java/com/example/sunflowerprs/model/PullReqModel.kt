package com.example.sunflowerprs.model

import com.google.gson.annotations.SerializedName

data class PullReqModel(
    val title : String,
    @SerializedName("created_at")
    val createdAt : String,
    @SerializedName("closed_at")
    val closedAt : String,
    val user: User
)
 data class User(
     @SerializedName("avatar_url")
     val profile : String
 )