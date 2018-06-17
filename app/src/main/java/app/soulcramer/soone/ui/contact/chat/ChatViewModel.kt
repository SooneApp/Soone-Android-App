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
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.chrono.ChronoZonedDateTime
import org.threeten.bp.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class ChatViewModel @Inject constructor(var chatRepository: ChatRepository,
                                        var messageRepository: MessageRepository,
                                        var service: SooneService) : ViewModel() {

    fun startTimer(time: ZonedDateTime) {
        launch(CommonPool) {
            while (true) {
                val newValue = ChronoZonedDateTime.from(ZonedDateTime.now()).until(time, ChronoUnit.SECONDS)
                remainingTime.postValue(newValue)
                delay(1, TimeUnit.SECONDS)
            }
        }
    }


    private val _id = MutableLiveData<String>()
    val id: LiveData<String>
        get() = _id

    private val _user1Id = MutableLiveData<String>()
    val user1Id: LiveData<String>
        get() = _user1Id

    private val _user2Id = MutableLiveData<String>()
    val user2Id: LiveData<String>
        get() = _user2Id

    val remainingTime = MutableLiveData<Long>()

    val chat: LiveData<Resource<Chat>> = Transformations
        .switchMap(_id) { id ->
            if (id == null) {
                AbsentLiveData.create()
            } else {
                chatRepository.loadChatById(id)
            }
        }

    private val userMessages: LiveData<Resource<List<Message>>> = Transformations
        .switchMap(_user1Id) {
            if (it == null) {
                AbsentLiveData.create()
            } else {
                messageRepository.loadUserMessagesByChatId(user1Id.value!!, id.value!!)
            }
        }

    private val otherUserMessages: LiveData<Resource<List<Message>>> = Transformations
        .switchMap(_user2Id) {
            if (it == null) {
                AbsentLiveData.create()
            } else {
                messageRepository.loadUserMessagesByChatId(user2Id.value!!, id.value!!)
            }
        }

    val messages = MediatorLiveData<MutableList<Message>>().apply {
        addSource(userMessages) {
            if (it?.status is Success && it.data != null) {
                val list = ArrayList<Message>()
                list.addAll(it.data)
                val correctList = list.toMutableList()
                value = correctList
            }
        }
        addSource(otherUserMessages) {
            if (it?.status is Success && it.data != null) {
                val list = ArrayList<Message>()
                list.addAll(it.data)
                val correctList = list.toMutableList()
                value = correctList
            }
        }
    }


    fun setId(id: String?) {
        if (_id.value != id) {
            _id.value = id
        }
    }

    fun setUser1Id(id: String?) {
        if (_user1Id.value != id) {
            _user1Id.value = id
        }
    }

    fun setUser2Id(id: String?) {
        if (_user2Id.value != id) {
            _user2Id.value = id
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
