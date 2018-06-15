package app.soulcramer.soone.ui.search

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import app.soulcramer.soone.repository.UserRepository
import app.soulcramer.soone.util.AbsentLiveData
import app.soulcramer.soone.vo.Resource
import app.soulcramer.soone.vo.user.User
import javax.inject.Inject

class SearchViewModel @Inject constructor(userRepository: UserRepository) : ViewModel() {
    private val _nick = MutableLiveData<String>()
    val nick: LiveData<String>
        get() = _nick
    val user: LiveData<Resource<User>> = Transformations
        .switchMap(_nick) { id ->
            if (id == null) {
                AbsentLiveData.create()
            } else {
                userRepository.loadUserById(id)
            }
        }

    fun setNickname(nick: String?) {
        if (_nick.value != nick) {
            _nick.value = nick
        }
    }

    fun retry() {
        _nick.value?.let {
            _nick.value = it
        }
    }
}
