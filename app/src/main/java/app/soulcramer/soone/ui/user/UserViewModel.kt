package app.soulcramer.soone.ui.user

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import app.soulcramer.soone.repository.UserRepository
import app.soulcramer.soone.util.AbsentLiveData
import app.soulcramer.soone.vo.Resource
import app.soulcramer.soone.vo.user.Sex
import app.soulcramer.soone.vo.user.User
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
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

    fun createUser(phoneNumber: String) {
        launch(UI) {
            val newUser = userRepository.createUser(phoneNumber).await()!!
            val birthdate = LocalDate.of(1995, 1, 1).format(DateTimeFormatter.ISO_LOCAL_DATE)
            val now = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
            newUser.apply {
                nickName = "Loïc"
                this.birthDate = birthdate
                sex = Sex.MALE.toInt()
                description = """Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor
                 incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco
                laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit
                in voluptate velit esse cillum dolore eu fugiat nulla pariatur.
                Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."""
                lastSeen = now
            }
            userRepository.updateUser(newUser).await()
            setId(newUser.id)
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
