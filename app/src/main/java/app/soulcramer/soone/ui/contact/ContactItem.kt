package app.soulcramer.soone.ui.contact

import `fun`.soone.R
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.soulcramer.soone.vo.user.User
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_contact.view.*

class ContactItem : AbstractItem<ContactItem, ContactItem.ViewHolder>() {
    var contact: User? = null
    var lastMsg: String = ""
    var unseenMsg: Int = 0

    //The unique ID for this type of item
    override fun getType(): Int = R.id.item_contact

    //The layout to be used for this type of item
    override fun getLayoutRes(): Int = R.layout.item_contact

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    @Suppress("JoinDeclarationAndAssignment")
    class ViewHolder(view: View) : FastAdapter.ViewHolder<ContactItem>(view) {

        var contactImageView: ImageView
        var nicknameTextView: TextView
        var lastMsgTextView: TextView
        var unseenMsgCountTextView: TextView

        init {
            contactImageView = view.contactImageView
            nicknameTextView = view.nicknameTextView
            lastMsgTextView = view.lastMsgTextView
            unseenMsgCountTextView = view.unseenMsgCountTextView
        }

        override fun bindView(item: ContactItem, payloads: List<Any>) {

            Picasso.get().load("qdqz")
                .placeholder(R.drawable.ic_person_outline_black_24dp)
                .error(R.drawable.ic_person_outline_black_24dp)
                .into(contactImageView)

            nicknameTextView.text = item.contact?.nickName
            lastMsgTextView.text = item.lastMsg
            unseenMsgCountTextView.text = item.unseenMsg.toString()

        }

        override fun unbindView(item: ContactItem) {
            contactImageView.setImageDrawable(null)
            nicknameTextView.text = null
            lastMsgTextView.text = null
            unseenMsgCountTextView.text = null
        }

    }
}