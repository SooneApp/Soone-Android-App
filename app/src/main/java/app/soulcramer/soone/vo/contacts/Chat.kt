package app.soulcramer.soone.vo.contacts

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.Required

@RealmClass
open class Chat(
    @field:PrimaryKey
    @field:Required
    var id: String = "",
    @field:Required
    @field:Index
    var user1: String = "",
    @field:Required
    @field:Index
    var user2: String = "",
    var startDate: String = "",
    var endDate: String = "",
    var messages: RealmList<Message> = RealmList(),
    var createdAt: String = "",
    var updatedAt: String = "",
    var deletedAt: String? = null
) : RealmObject()
