package network

import models.LoginRequest
import models.LoginResponse
import models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("login/")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>
}