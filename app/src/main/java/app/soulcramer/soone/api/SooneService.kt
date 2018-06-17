package app.soulcramer.soone.api

import android.arch.lifecycle.LiveData
import app.soulcramer.soone.vo.contacts.Chat
import app.soulcramer.soone.vo.contacts.Message
import app.soulcramer.soone.vo.user.User
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.experimental.Deferred
import retrofit2.Call
import retrofit2.http.*

/**
 * REST API access points
 */
interface SooneService {
    /**
     * @param userId
     */
    @GET("api/user")
    fun getUserById(@Query("id") userId: String): LiveData<ApiResponse<User>>

    /**
     * @param userPhoneNumber
     */
    @POST("api/user")
    fun createUser(@Query("phoneNumber") userPhoneNumber: String): Deferred<User>

    /**
     * @param userId
     */
    @PUT("api/user")
    fun updateUser(@Query("id") userId: String, @Body user: User): Deferred<User>


    /**
     * @param userId
     */
    @DELETE("api/user/{userId}")
    fun deleteUser(@Path("userId") userId: String): LiveData<ApiResponse<User>>


    /**
     * @param userId
     */
    @POST("api/connect")
    fun connect(
        @Query("phoneNumber") phoneNumber: String,
        @Query("appToken") token: String?
    ): Deferred<User>

    /**
     * @param userId
     */
    @GET("api/disconnect/{userId}")
    fun disconnect(@Path("userId") userId: String): LiveData<ApiResponse<User>>

    /**
     * @param searchBody
     */
    @POST("api/instantSearch")
    fun instantSearch(@Body searchBody: SearchBody): Call<String>

    /**
     * @param chatId
     */
    @GET("api/chat")
    fun getChatById(@Query("id") chatId: String): LiveData<ApiResponse<Chat>>

    /**
     * @param chatId
     */
    @POST("api/message")
    fun sendMessage(
        @Query("chatId") chatId: String,
        @Query("senderId") userId: String,
        @Query("content") content: String
    ): Call<Chat>

    /**
     * @param chatId
     */
    @GET("api/messages")
    fun getMessages(
        @Query("chatId") chatId: String,
        @Query("userId") userId: String
    ): LiveData<ApiResponse<List<Message>>>

    /**
     * @param chatId
     */
    @GET("api/matchDecision")
    fun getMatchDecision(
        @Query("chatId") chatId: String,
        @Query("userId") userId: String
    ): LiveData<ApiResponse<List<Message>>>

    /**
     * @param chatId
     */
    @PUT("api/matchDecision")
    fun updateMatchDecision(
        @Query("chatId") chatId: String,
        @Query("userId") userId: String
    ): LiveData<ApiResponse<List<Message>>>


    data class SearchBody(
        @SerializedName("id")
        val userId: String = "",
        val ageRange: List<Int> = listOf(),
        val city: String = "Montpelier"
    )

    data class MessageBody(
        val chatId: String = "",
        val userId: String = "",
        val content: String = ""
    )
}
