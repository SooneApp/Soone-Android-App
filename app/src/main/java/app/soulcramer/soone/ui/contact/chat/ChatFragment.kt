package app.soulcramer.soone.ui.contact.chat

import `fun`.soone.R
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.navigation.fragment.NavHostFragment
import app.soulcramer.soone.common.observeK
import app.soulcramer.soone.di.Injectable
import app.soulcramer.soone.ui.common.EqualSpacingItemDecoration
import app.soulcramer.soone.ui.common.dpToPx
import app.soulcramer.soone.ui.common.statefulview.Data
import app.soulcramer.soone.ui.user.UserViewModel
import app.soulcramer.soone.vo.Error
import app.soulcramer.soone.vo.Loading
import app.soulcramer.soone.vo.Success
import app.soulcramer.soone.vo.contacts.Chat
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.item_message.view.*
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.chrono.ChronoZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject


class ChatFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var userViewModel: UserViewModel
    private lateinit var chatViewModel: ChatViewModel

    private val toolbar: ActionBar by lazy {
        (activity as AppCompatActivity).supportActionBar!!
    }

    private val sharedPreferences: SharedPreferences by lazy {
        requireActivity().getSharedPreferences("sooneSharedPref", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    private val itemAdapter = ItemAdapter<UserMessageItem>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        userViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(UserViewModel::class.java)
        chatViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ChatViewModel::class.java)

        toolbar.title = "Chat"

        val chatId = ChatFragmentArgs.fromBundle(arguments).activeChatId

        chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            val itemsPadding = 16f.dpToPx(resources.displayMetrics)
            addItemDecoration(EqualSpacingItemDecoration(itemsPadding,
                EqualSpacingItemDecoration.VERTICAL))
        }

        val fastAdapter = FastAdapter.with<UserMessageItem, ItemAdapter<UserMessageItem>>(itemAdapter).apply {
            withSelectable(true)
            withOnClickListener { v, adapter, item, position ->
                true
            }
            withEventHook(object : ClickEventHook<UserMessageItem>() {

                override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
                    //return the views on which you want to bind this event
                    return if (viewHolder is UserMessageItem.ViewHolder) {
                        viewHolder.itemView.contactImageView
                    } else null
                }

                override fun onClick(v: View, position: Int, fastAdapter: FastAdapter<UserMessageItem>, item: UserMessageItem) {
                    //react on the click event
                }
            })
        }

        chatRecyclerView.adapter = fastAdapter

        userViewModel.user.observeK(this) {}

        chatViewModel.chat.observeK(this) { chatRessource ->
            when (chatRessource.status) {
                is Loading -> if (itemAdapter.itemList.isEmpty) statefulView.state = statefulView.loadingState
                is Error -> statefulView.state = statefulView.errorState
                is Success -> {
                    chatRessource.data?.run {
                        chatViewModel.startTimer(endDateTime)
                        if (endDateTime.isBefore(ChronoZonedDateTime.from(ZonedDateTime.now()))) {
                            chatExpired(this)
                        }
                        chatViewModel.setUser1Id(user1)
                        chatViewModel.setUser1Id(user2)
                    }
                }
            }
        }
        chatViewModel.messages.observeK(this) { messages ->
            val items = messages.sortedBy {
                ZonedDateTime.parse(it.date, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            }.map {
                UserMessageItem().apply {
                    message = it.content
                }
            }

            if (items.isEmpty()) {
                statefulView.state = statefulView.emptyState
            } else {
                statefulView.state = Data()
                itemAdapter.setNewList(items)
            }
        }

        chatViewModel.remainingTime.observeK(this) {
            if (it <= 0) {
                chatExpired(chatViewModel.chat.value?.data!!)
            }
            remainingTextView.text = "${it / 60} minutes et ${it % 60} secondes restantes"
        }

        chatViewModel.setId(chatId)


        sendMessageImageButton.setOnClickListener {
            if (newMessageEditText.text?.isNotEmpty() == true) {
                chatViewModel.sendMessage(
                    userViewModel.user.value?.data?.id ?: "",
                    newMessageEditText.text.toString()
                )
            }
        }
    }

    private fun chatExpired(chat: Chat) {
        sharedPreferences.edit {
            putBoolean("activeDecision", true)
        }
        val activeDecisionId = sharedPreferences.getString("activeDecisionId", "")
        val action = ChatFragmentDirections.Action_chat_to_match2(chat.id, activeDecisionId)
        action.setChatId(chat.id)
        action.setDecisionId(activeDecisionId)
        NavHostFragment.findNavController(this).navigate(action)
    }
}
