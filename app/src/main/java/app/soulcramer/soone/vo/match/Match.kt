package app.soulcramer.soone.vo.match

import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.Required

@RealmClass
open class Match(
    @field:PrimaryKey
    @field:Required
    var id: String = "",
    @field:Required
    @field:Index
    var user1Id: String = "",
    @field:Required
    @field:Index
    var user2Id: String = "",
    @field:Required
    var date: String = "",
    var active: Boolean = false,
    var createdAt: String = "",
    var updatedAt: String = "",
    var deletedAt: String? = null
) : RealmObject()
