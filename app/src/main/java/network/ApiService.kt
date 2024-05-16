package network

import models.LoginRequest
import models.LoginResponse
import models.RegisterRequest
import models.User
import models.availabilityResponse
import models.availabilityState
import models.availabilityStateRequest
import models.likeRequest
import models.matchResponse
import models.proximityResponse
import models.user.defaultResponse
import models.user.getImageRequest
import models.user.locationRequest
import models.user.uploadImageResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import util.Authorized

interface ApiService {
    @POST("user/login/")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>


    @POST("user/create/")
    fun registerUser(@Body request: RegisterRequest): Call<LoginResponse>

    @Authorized
    @GET("user/availability/")
    fun getAvailableUsers():Call<availabilityResponse>

    @Authorized
    @PUT("user/availability/")
    fun changeAvailability(@Body request: availabilityStateRequest): Call<availabilityState>

    @Authorized
    @PUT("user/location/")
    fun updateUserLocation(@Body request: locationRequest): Call<defaultResponse>


    @Authorized
    @Multipart
    @POST("blob/upload/")
    fun uploadImage(@Part image: MultipartBody.Part): Call<uploadImageResponse>


    @Authorized
    @POST("blob/transfer")
    fun fetchImage(@Body request: getImageRequest): Call<ResponseBody>

    @Authorized
    @GET("user/proximity")
    fun getUsersOnProximity():Call<proximityResponse>


    @Authorized
    @POST("like/")
    fun createLikeAndCheckIfMatch(@Body request: likeRequest):Call<matchResponse>


    @Authorized
    @GET("user/auth")
    fun authorizeToken():Call<User>


    @Authorized
    @GET("chat/")
    fun getChats():Call<proximityResponse>
}