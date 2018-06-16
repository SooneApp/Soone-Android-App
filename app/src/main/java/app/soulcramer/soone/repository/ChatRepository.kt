package app.soulcramer.soone.repository

import android.arch.lifecycle.LiveData
import app.soulcramer.soone.AppCoroutineDispatchers
import app.soulcramer.soone.api.SooneService
import app.soulcramer.soone.db.ChatDao
import app.soulcramer.soone.db.MessageDao
import app.soulcramer.soone.vo.Resource
import app.soulcramer.soone.vo.contacts.Chat
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles User objects.
 */
@Singleton
class ChatRepository @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val chatDao: ChatDao,
    private val messageDao: MessageDao,
    private val service: SooneService
) {

    fun loadChatById(id: String): LiveData<Resource<Chat>> {
        return object : NetworkBoundResource<Chat, Chat>(dispatchers) {
            override fun saveCallResult(item: Chat) {
                chatDao.insert(item)
            }

            override fun shouldFetch(data: Chat?) = true

            override fun loadFromDb() = chatDao.findByIdAsync(id)

            override fun createCall() = service.getChatById(id)
        }.asLiveData()
    }

    suspend fun sendMessage(userId: String, chatId: String, message: String): Chat? {
        return withContext(dispatchers.network) {
            service.sendMessage(
                chatId,
                userId,
                message
            ).execute().body()
        }

    }
}
