package app.soulcramer.soone.vo.contacts

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
    var idUser1: String = "",
    @field:Required
    @field:Index
    var idUser2: String = "",
    var createdAt: String = "",
    var updatedAt: String = "",
    var deletedAt: String? = null
) : RealmObject()
