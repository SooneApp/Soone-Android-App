package app.soulcramer.soone.repository

import android.arch.lifecycle.LiveData
import app.soulcramer.soone.AppCoroutineDispatchers
import app.soulcramer.soone.api.SooneService
import app.soulcramer.soone.db.ChatDao
import app.soulcramer.soone.db.MessageDao
import app.soulcramer.soone.vo.Resource
import app.soulcramer.soone.vo.contacts.Chat
import app.soulcramer.soone.vo.contacts.Message
import kotlinx.coroutines.experimental.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles User objects.
 */
@Singleton
class MessageRepository @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val chatDao: ChatDao,
    private val messageDao: MessageDao,
    private val service: SooneService
) {

    fun loadUserMessagesByChatId(userId: String, chatID: String): LiveData<Resource<List<Message>>> {
        return object : NetworkBoundResource<List<Message>, List<Message>>(dispatchers) {
            override fun saveCallResult(item: List<Message>) {
                messageDao.insert(item)
            }

            override fun shouldFetch(data: List<Message>?) = true

            override fun loadFromDb() = messageDao.findByChatIdAsync(userId, chatID)

            override fun createCall() = service.getMessages(chatID, userId)
        }.asLiveData()
    }

    suspend fun sendMessage(chatId: String, userId: String, message: String) {
        withContext(dispatchers.network) {
            service.sendMessage(
                chatId,
                userId,
                message
            ).enqueue(object : Callback<Chat?> {
                override fun onFailure(call: Call<Chat?>?, t: Throwable?) {
                }

                override fun onResponse(call: Call<Chat?>?, response: Response<Chat?>?) {
                }
            })
        }

    }
}
