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

    fun loadUserById(id: String): LiveData<Resource<List<User>>> {
        return object : NetworkBoundResource<List<User>, User>(dispatchers) {
            override fun saveCallResult(item: User) {
                userDao.insert(item)
            }

            override fun shouldFetch(data: List<User>?) = data == null || data.isEmpty()

            override fun loadFromDb() = userDao.findById(id)

            override fun createCall() = service.getUserById(id)
        }.asLiveData()
    }
}
