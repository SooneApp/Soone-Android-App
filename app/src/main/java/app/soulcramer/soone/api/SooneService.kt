package app.soulcramer.soone.api

import android.arch.lifecycle.LiveData
import app.soulcramer.soone.vo.user.User
import retrofit2.http.*

/**
 * REST API access points
 */
interface SooneService {
    /**
     * @param userId
     */
    @GET("api/user/{userId}")
    fun getUserById(@Path("userId") userId: String): LiveData<ApiResponse<User>>

    /**
     * @param userPhoneNumber
     */
    @POST("api/user/{userPhoneNumber}")
    fun createUser(@Path("userPhoneNumber") userPhoneNumber: String): LiveData<ApiResponse<User>>

    /**
     * @param userId
     */
    @PUT("api/user/{userId}")
    fun updateUser(@Path("userId") userId: String): LiveData<ApiResponse<User>>


    /**
     * @param userId
     */
    @DELETE("api/user/{userId}")
    fun deleteUser(@Path("userId") userId: String): LiveData<ApiResponse<User>>


    /**
     * @param userId
     */
    @POST("api/connect/{userId}")
    fun connect(@Path("userId") userId: String): LiveData<ApiResponse<User>>

    /**
     * @param userId
     */
    @GET("api/disconnect/{userId}")
    fun disconnect(@Path("userId") userId: String): LiveData<ApiResponse<User>>
}
