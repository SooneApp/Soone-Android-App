package app.soulcramer.soone.vo.user

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.Required
import java.util.*

@RealmClass
open class User(
        @field:PrimaryKey
        @field:Required
        var id: String = "",
        @field:SerializedName("nick")
        @field:Index
        var nickName: String = "",
        var birthdate: Date? = null,
        var sex: Sex? = null,
        var sexInterest: SexInterest? = null,
        var description: String = "",
        var lastSeen: Date? = null,
        var createdAt: Date? = null,
        var updatedAt: Date? = null,
        var deletedAt: Date? = null
) : RealmObject()
