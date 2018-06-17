package app.soulcramer.soone.ui.signup

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import app.soulcramer.soone.repository.UserRepository
import app.soulcramer.soone.vo.user.User
import javax.inject.Inject

class SignupViewModel @Inject constructor(var userRepository: UserRepository) : ViewModel() {


    var user = MutableLiveData<User>()

    suspend fun createUser(phoneNumber: String): User {
        val newUser = userRepository.createUser(phoneNumber).await()!!
        return userRepository.connectUser(newUser.phoneNumber).await()
    }
}
