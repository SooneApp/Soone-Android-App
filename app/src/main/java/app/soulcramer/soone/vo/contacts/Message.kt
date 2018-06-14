package app.soulcramer.soone.vo.contacts

import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.Required

@RealmClass
open class Message(
    @field:PrimaryKey
    @field:Required
    var id: String = "",
    @field:Required
    @field:Index
    var chatId: String = "",
    @field:Required
    @field:Index
    var senderId: String = "",
    @field:Required
    var content: String = "",
    @field:Required
    var date: String = "",
    var createdAt: String = "",
    var updatedAt: String = "",
    var deletedAt: String? = null
) : RealmObject()
