package app.soulcramer.soone.ui.contact

import `fun`.soone.R
import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem

class ContactItem : AbstractItem<ContactItem, ContactItem.ViewHolder>() {
    var name: String? = null
    var description: String? = null

    //The unique ID for this type of item
    override fun getType(): Int = R.id.item_contact

    //The layout to be used for this type of item
    override fun getLayoutRes(): Int = R.layout.item_contact

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(view: View) : FastAdapter.ViewHolder<ContactItem>(view) {

        override fun bindView(item: ContactItem, payloads: List<Any>) {}

        override fun unbindView(item: ContactItem) {}

    }
}