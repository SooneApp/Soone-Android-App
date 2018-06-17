package app.soulcramer.soone.db

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import app.soulcramer.soone.vo.user.User
import app.soulcramer.soone.vo.user.UserFields
import com.zhuinden.monarchy.Monarchy
import io.realm.Realm
import io.realm.kotlin.where
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDao @Inject constructor(private val monarchy: Monarchy) {

    fun insert(user: User) {
        monarchy.runTransactionSync { it.copyToRealmOrUpdate(user) }
    }

    fun findByIdAsync(id: String): LiveData<User> {
        val usersLiveData = monarchy.findAllCopiedWithChanges { realm ->
            realm.where<User>().equalTo(UserFields.ID, id)
        }
        return Transformations.map(usersLiveData) {
            it.firstOrNull()
        }
    }

    fun findById(id: String): User {
        return monarchy.findAllSync(Realm.getInstance(monarchy.realmConfiguration)) {
            it.where<User>().equalTo("id", id)
        }.first()
    }
}
