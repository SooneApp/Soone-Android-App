package app.soulcramer.soone.ui.contact

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import app.soulcramer.soone.repository.UserRepository
import app.soulcramer.soone.util.AbsentLiveData
import app.soulcramer.soone.vo.Resource
import app.soulcramer.soone.vo.user.User
import javax.inject.Inject

class ContactsViewModel @Inject constructor(var userRepository: UserRepository) : ViewModel() {
    private val _id = MutableLiveData<String>()
    val id: LiveData<String>
        get() = _id
    val contacts: LiveData<Resource<User>> = Transformations
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
}
