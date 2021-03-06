package app.soulcramer.soone.vo.user

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.Required

@RealmClass
open class SexInterest(
        @field:PrimaryKey
        @field:Required
        var id: String = "",
        var interest: String = ""
) : RealmObject()
