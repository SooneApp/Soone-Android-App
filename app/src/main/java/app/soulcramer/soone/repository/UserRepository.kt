package app.soulcramer.soone.repository

import android.arch.lifecycle.LiveData
import app.soulcramer.soone.AppCoroutineDispatchers
import app.soulcramer.soone.api.SooneService
import app.soulcramer.soone.db.UserDao
import app.soulcramer.soone.vo.Resource
import app.soulcramer.soone.vo.user.User
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

            override fun loadFromDb() = userDao.findById(id)

            override fun createCall() = service.getUserById(id)
        }.asLiveData()
    }

    fun upadteUser(user: User) {
        userDao.insert(user)
    }
}
