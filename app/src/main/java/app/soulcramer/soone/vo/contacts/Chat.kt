package app.soulcramer.soone.vo.contacts

import com.google.gson.annotations.Expose
import io.realm.RealmObject
import io.realm.annotations.*
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

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
    var createdAt: String = "",
    var updatedAt: String = "",
    var deletedAt: String? = null
) : RealmObject() {

    @Ignore
    @Expose(serialize = false, deserialize = false)
    var endDateTime: ZonedDateTime = ZonedDateTime.now()
        set(value) {
            field = value
            endDate = value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)!!
        }
        get() = ZonedDateTime.parse(endDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

    @Ignore
    @Expose(serialize = false, deserialize = false)
    var startDateTime: ZonedDateTime = ZonedDateTime.now()
        set(value) {
            field = value
            startDate = value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)!!
        }
        get() = ZonedDateTime.parse(startDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
}
