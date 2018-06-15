package app.soulcramer.soone.repository

import android.arch.lifecycle.LiveData
import app.soulcramer.soone.AppCoroutineDispatchers
import app.soulcramer.soone.api.SooneService
import app.soulcramer.soone.db.UserDao
import app.soulcramer.soone.vo.Resource
import app.soulcramer.soone.vo.user.User
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles User objects.
 */
@Singleton
class UserRepository @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val userDao: UserDao,
    private val service: SooneService
) {

    fun loadUserById(id: String): LiveData<Resource<User>> {
        return object : NetworkBoundResource<User, User>(dispatchers) {
            override fun saveCallResult(item: User) {
                userDao.insert(item)
            }

            override fun shouldFetch(data: User?) = data == null

            override fun loadFromDb() = userDao.findByIdAsync(id)

            override fun createCall() = service.getUserById(id)
        }.asLiveData()
    }

    fun createUser(phoneNumber: String): Deferred<User?> {
        return async(dispatchers.network) {
            service.createUser(phoneNumber).await()
            service.connect(phoneNumber, FirebaseInstanceId.getInstance().token).await()
        }
    }

    fun connectUser(phoneNumber: String): Deferred<User> {
        return async(dispatchers.network) {
            service.connect(phoneNumber, FirebaseInstanceId.getInstance().token).await()
        }
    }

    fun updateUser(user: User): Deferred<Resource<User>> {
        return async(dispatchers.network) {
            try {
                val response = service.updateUser(user.id, user).await()
                val updatedUser = withContext(dispatchers.disk) {
                    userDao.insert(response)
                    userDao.findById(response.id)
                }
                Resource.success(updatedUser)
            } catch (exception: Exception) {
                Resource.error(exception.message ?: "", null)
            }
        }
    }
}
