package app.soulcramer.soone.ui.contact.chat

import android.arch.lifecycle.*
import app.soulcramer.soone.api.SooneService
import app.soulcramer.soone.repository.ChatRepository
import app.soulcramer.soone.repository.MessageRepository
import app.soulcramer.soone.util.AbsentLiveData
import app.soulcramer.soone.vo.Resource
import app.soulcramer.soone.vo.Success
import app.soulcramer.soone.vo.contacts.Chat
import app.soulcramer.soone.vo.contacts.Message
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class ChatViewModel @Inject constructor(var chatRepository: ChatRepository,
                                        var messageRepository: MessageRepository,
                                        var service: SooneService) : ViewModel() {
    private val _id = MutableLiveData<String>()
    val id: LiveData<String>
        get() = _id

    val chat: LiveData<Resource<Chat>> = Transformations
        .switchMap(_id) { id ->
            if (id == null) {
                AbsentLiveData.create()
            } else {
                chatRepository.loadChatById(id)
            }
        }

    val userMessages: LiveData<Resource<List<Message>>> = Transformations
        .switchMap(_id) { id ->
            if (id == null || chat.value?.status !is Success) {
                AbsentLiveData.create()
            } else {
                messageRepository.loadUserMessagesByChatId(chat.value?.data?.user1!!, id)
            }
        }

    val otherUserMessages: LiveData<Resource<List<Message>>> = Transformations
        .switchMap(_id) { id ->
            if (id == null || chat.value?.status !is Success) {
                AbsentLiveData.create()
            } else {
                messageRepository.loadUserMessagesByChatId(chat.value?.data?.user2!!, id)
            }
        }

    val messages = MediatorLiveData<MutableList<Message>>().apply {
        addSource(userMessages) {
            if (it?.status is Success && it.data != null) {
                value?.addAll(it.data)
                val list = value?.distinctBy { it.id }?.toMutableList()
                value = list
            }
        }
        addSource(otherUserMessages) {
            if (it?.status is Success && it.data != null) {
                value?.addAll(it.data)
                val list = value?.distinctBy { it.id }?.toMutableList()
                value = list
            }
        }
    }


    fun setId(id: String?) {
        if (_id.value != id) {
            _id.value = id
        }
    }

    fun sendMessage(userId: String, message: String) {
        launch(CommonPool) {
            messageRepository.sendMessage(
                id.value ?: "",
                userId,
                message
            )
            retry()
        }

    }

    fun retry() {
        launch(UI) {
            _id.value?.let {
                _id.value = it
            }
        }
    }

    override fun onCleared() {
        messages.removeSource(userMessages)
        messages.removeSource(otherUserMessages)
        super.onCleared()
    }
}
