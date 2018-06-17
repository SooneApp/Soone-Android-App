package app.soulcramer.soone.ui.user

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import app.soulcramer.soone.repository.UserRepository
import app.soulcramer.soone.util.AbsentLiveData
import app.soulcramer.soone.vo.Resource
import app.soulcramer.soone.vo.user.User
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class UserViewModel @Inject constructor(var userRepository: UserRepository) : ViewModel() {
    private val _id = MutableLiveData<String>()
    val id: LiveData<String>
        get() = _id
    val user: LiveData<Resource<User>> = Transformations
        .switchMap(_id) { id ->
            if (id == null) {
                AbsentLiveData.create()
            } else {
                userRepository.loadUserById(id)
            }
        }

    fun setId(id: String?) {
        if (_id.value != id) {
            _id.value = id
        }
    }

    fun retry() {
        _id.value?.let {
            _id.value = it
        }
    }

    fun updateUser(user: User) {
        launch(UI) {
            userRepository.updateUser(user).await()
            setId(user.id)
        }
    }

    fun connectUser(phoneNumber: String = user.value?.data?.phoneNumber ?: "") {
        launch(UI) {
            val user = userRepository.connectUser(phoneNumber).await()
            setId(user.id)
        }
    }
}
