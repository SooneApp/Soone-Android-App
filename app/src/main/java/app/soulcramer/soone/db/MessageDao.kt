package app.soulcramer.soone.db

import android.arch.lifecycle.LiveData
import app.soulcramer.soone.vo.contacts.Message
import app.soulcramer.soone.vo.contacts.MessageFields
import app.soulcramer.soone.vo.user.UserFields
import com.zhuinden.monarchy.Monarchy
import io.realm.Realm
import io.realm.kotlin.where
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageDao @Inject constructor(private val monarchy: Monarchy) {

    fun insert(message: Message) {
        monarchy.runTransactionSync { it.copyToRealmOrUpdate(message) }
    }

    fun insert(message: List<Message>) {
        monarchy.runTransactionSync { it.copyToRealmOrUpdate(message) }
    }

    fun findByChatIdAsync(id: String): LiveData<List<Message>> {
        return monarchy.findAllCopiedWithChanges { realm ->
            realm.where<Message>().equalTo(MessageFields.CHAT_ID, id)
        }
    }

    fun findById(id: String): Message {
        return monarchy.findAllSync(Realm.getInstance(monarchy.realmConfiguration)) {
            it.where<Message>().equalTo(UserFields.ID, id)
        }.first()
    }
}
