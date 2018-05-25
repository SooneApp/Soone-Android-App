package app.soulcramer.soone.api

import android.arch.lifecycle.LiveData
import app.soulcramer.soone.vo.user.User
import kotlinx.coroutines.experimental.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

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
     * @param userId
     */
    @GET("api/user/{userId}")
    fun getUserByIdK(@Path("userId") userId: String): Deferred<Response<User>>
}
