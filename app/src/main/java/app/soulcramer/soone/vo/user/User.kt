package app.soulcramer.soone.vo.user

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.Required

@RealmClass
open class User(
    @field:PrimaryKey
    @field:Required
    var id: String = "",
    @field:SerializedName("name")
    @field:Index
    var nickName: String = "",
    var birthDate: String = "",
    var phoneNumber: String = "",
    var sex: Int = 0,
    //var sexInterests: SexInterest? = null,
    var description: String = "",
    var lastSeen: String = "",
    var createdAt: String = "",
    var updatedAt: String = "",
    var deletedAt: String? = null
) : RealmObject() {

    fun getSexEnum(): Sex = Sex.fromInt(sex)

    fun setSexEnum(sex: Sex) {
        this.sex = sex.toInt()
    }
}
