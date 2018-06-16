package app.soulcramer.soone.ui.contact.chat

import `fun`.soone.R
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.soulcramer.soone.vo.user.User
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_message.view.*

class UserMessageItem : AbstractItem<UserMessageItem, UserMessageItem.ViewHolder>() {
    var contact: User? = null
    var message: String = ""

    //The unique ID for this type of item
    override fun getType(): Int = R.id.item_message

    //The layout to be used for this type of item
    override fun getLayoutRes(): Int = R.layout.item_message

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    @Suppress("JoinDeclarationAndAssignment")
    class ViewHolder(view: View) : FastAdapter.ViewHolder<UserMessageItem>(view) {

        var contactImageView: ImageView
        var messageTextView: TextView

        init {
            contactImageView = view.contactImageView
            messageTextView = view.messageTextView
        }

        override fun bindView(item: UserMessageItem, payloads: List<Any>) {

            Picasso.get().load("qdqz")
                .placeholder(R.drawable.ic_person_outline_black_24dp)
                .error(R.drawable.ic_person_outline_black_24dp)
                .into(contactImageView)

            messageTextView.text = item.message

        }

        override fun unbindView(item: UserMessageItem) {
            contactImageView.setImageDrawable(null)
            messageTextView.text = null
        }

    }
}