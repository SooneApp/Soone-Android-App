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
    var sex: Int = 0,
    var sexInterest: SexInterest? = null,
    var description: String = "",
    var lastSeen: Date? = null,
    var createdAt: Date? = null,
    var updatedAt: Date? = null,
    var deletedAt: Date? = null
) : RealmObject() {

    fun getSexEnum(): Sex = Sex.fromInt(sex)

    fun setSexEnum(sex: Sex) {
        this.sex = sex.toInt()
    }

    fun isSameAs(user: User?): Boolean {
        if (user == null) return false

        if (id != user.id) return false
        if (nickName != user.nickName) return false
        if (birthdate != user.birthdate) return false
        if (sex != user.sex) return false
        if (sexInterest != user.sexInterest) return false
        if (description != user.description) return false
        if (lastSeen != user.lastSeen) return false
        if (createdAt != user.createdAt) return false
        if (updatedAt != user.updatedAt) return false
        if (deletedAt != user.deletedAt) return false

        return true
    }
}
