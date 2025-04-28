package com.example.myapplication

import com.example.myapplication.viewModel.SentGesture
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("post")
    suspend fun postGesture(@Body gesture: SentGesture): Response<ResponseBody>

}