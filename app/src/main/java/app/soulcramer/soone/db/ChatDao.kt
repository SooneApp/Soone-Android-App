package app.soulcramer.soone.db

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import app.soulcramer.soone.vo.contacts.Chat
import app.soulcramer.soone.vo.contacts.ChatFields
import app.soulcramer.soone.vo.user.UserFields
import com.zhuinden.monarchy.Monarchy
import io.realm.Realm
import io.realm.kotlin.where
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatDao @Inject constructor(private val monarchy: Monarchy) {

    fun insert(chat: Chat) {
        monarchy.runTransactionSync { it.copyToRealmOrUpdate(chat) }
    }

    fun findByIdAsync(id: String): LiveData<Chat> {
        val chatsLiveData = monarchy.findAllCopiedWithChanges { realm ->
            realm.where<Chat>().equalTo(ChatFields.ID, id)
        }
        return Transformations.map(chatsLiveData) {
            it.firstOrNull()
        }
    }

    fun findById(id: String): Chat {
        return monarchy.findAllSync(Realm.getInstance(monarchy.realmConfiguration)) {
            it.where<Chat>().equalTo(UserFields.ID, id)
        }.first()
    }
}
