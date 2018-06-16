package app.soulcramer.soone.ui.contact.chat

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import app.soulcramer.soone.api.SooneService
import app.soulcramer.soone.repository.ChatRepository
import app.soulcramer.soone.util.AbsentLiveData
import app.soulcramer.soone.vo.Resource
import app.soulcramer.soone.vo.contacts.Chat
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class ChatViewModel @Inject constructor(var chatRepository: ChatRepository, var service: SooneService) : ViewModel() {
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


    fun setId(id: String?) {
        if (_id.value != id) {
            _id.value = id
        }
    }

    fun sendMessage(userId: String, message: String) {
        launch(CommonPool) {
            service.sendMessage(
                id.value ?: "",
                userId,
                message
            ).await()
            retry()
        }
    }

    fun retry() {
        _id.value?.let {
            _id.value = it
        }
    }
}
