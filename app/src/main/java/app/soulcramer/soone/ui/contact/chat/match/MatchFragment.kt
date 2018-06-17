package app.soulcramer.soone.ui.contact.chat.match

import `fun`.soone.R
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.navigation.findNavController
import app.soulcramer.soone.common.observeK
import app.soulcramer.soone.db.UserDao
import app.soulcramer.soone.di.Injectable
import app.soulcramer.soone.ui.common.statefulview.Data
import app.soulcramer.soone.ui.contact.chat.ChatViewModel
import app.soulcramer.soone.ui.user.UserViewModel
import kotlinx.android.synthetic.main.fragment_match.*
import javax.inject.Inject


class MatchFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var userDao: UserDao

    private val toolbar: ActionBar by lazy {
        (activity as AppCompatActivity).supportActionBar!!
    }

    private lateinit var userViewModel: UserViewModel
    private lateinit var chatViewModel: ChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_match, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        userViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(UserViewModel::class.java)
        chatViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ChatViewModel::class.java)

        toolbar.title = "Match"

        val args = MatchFragmentArgs.fromBundle(arguments)
        val chatId = args.chatId

        userViewModel.user.observeK(this) { userResource ->
            userResource.data?.run {

            }
        }

        chatViewModel.chat.observeK(this) { resource ->
            resource.data?.run {
                statefulView.state = Data()
                continueTextView.text = buildSpannedString {
                    append("Voulez-vous continuer de parler avec ")
                    bold {
                        color(R.color.primaryVariant) {
                            if (user1 == userViewModel.id.value) {
                                userDao.findById(user2)?.nickName

                            } else {
                                userDao.findById(user1)?.nickName
                            }
                        }
                    }
                    append(" ?")
                }
            }
        }
        chatViewModel.setId(chatId)

        continueButton.setOnClickListener {
            val action = MatchFragmentDirections.action_match_to_search()
            it.findNavController().navigate(action)
        }

        newSearchButton.setOnClickListener {
            val action = MatchFragmentDirections.action_match_to_search()
            it.findNavController().navigate(action)
        }
    }
}
