package app.soulcramer.soone.ui.contact

import `fun`.soone.R
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.soulcramer.soone.common.observeK
import app.soulcramer.soone.di.Injectable
import app.soulcramer.soone.ui.user.UserViewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook
import kotlinx.android.synthetic.main.fragment_contacts.*
import javax.inject.Inject


class ContactFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var userViewModel: UserViewModel
    private lateinit var contactsViewModel: ContactsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        userViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(UserViewModel::class.java)
        contactsViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ContactsViewModel::class.java)

        val itemAdapter = ItemAdapter<ContactItem>()
        val fastAdapter = FastAdapter.with<ContactItem, ItemAdapter<ContactItem>>(itemAdapter).apply {
            withSelectable(true)
            withOnClickListener { v, adapter, item, position ->
                true
            }
            withEventHook(object : ClickEventHook<ContactItem>() {

                override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
                    //return the views on which you want to bind this event
                    return if (viewHolder is ContactItem.ViewHolder) {
                        (viewHolder as ContactItem.ViewHolder).itemView
                    } else null
                }

                override fun onClick(v: View, position: Int, fastAdapter: FastAdapter<ContactItem>, item: ContactItem) {
                    //react on the click event
                }
            })
        }

        contactsRecyclerView.adapter = fastAdapter

        itemAdapter.add()


        userViewModel.setId("1")
        userViewModel.user.observeK(this) { userResource ->
            userResource.data?.run {

            }
        }
    }
}
