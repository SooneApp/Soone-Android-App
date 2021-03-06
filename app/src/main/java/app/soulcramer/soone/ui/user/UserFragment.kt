package app.soulcramer.soone.ui.user

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
import androidx.navigation.findNavController
import app.soulcramer.soone.common.observeK
import app.soulcramer.soone.di.Injectable
import app.soulcramer.soone.ui.common.statefulview.Data
import app.soulcramer.soone.vo.Error
import app.soulcramer.soone.vo.Loading
import app.soulcramer.soone.vo.Success
import app.soulcramer.soone.vo.user.Sex
import app.soulcramer.soone.vo.user.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.Period
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

class UserFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var userViewModel: UserViewModel

    private val toolbar: ActionBar by lazy {
        (activity as AppCompatActivity).supportActionBar!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        userViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(UserViewModel::class.java)

        statefulView.isSwipeRefreshEnable = true
        statefulView.swipeRefreshLayout.setOnRefreshListener {
            userViewModel.retry()
        }

        toolbar.title = "Mon Profile"

        userViewModel.user.observeK(this) { userResource ->
            when (userResource.status) {
                is Loading -> if (userResource.data == null) statefulView.state = statefulView.loadingState
                is Error -> statefulView.state = statefulView.errorState
                is Success -> {
                    userResource.data?.run {
                        handleData()
                    }
                }
            }
        }
    }

    private fun User.handleData() {
        statefulView.state = Data()
        nickNameTextView.text = nickName.capitalize()

        if (description.isEmpty()) {
            descriptionTextView.text = getString(R.string.user_description_empty)
            descriptionTextView.setTextAppearance(R.style.TextAppearance_NotifyMoe_Caption)
        } else {
            descriptionTextView.text = description
            descriptionTextView.setTextAppearance(R.style.TextAppearance_NotifyMoe_Body1)
        }

        sexTextView.text = getString(Sex.fromInt(sex).stringRes())

        if (birthDate.isNotEmpty()) {
            launch {
                val date = LocalDate.parse(birthDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                val birthday = date.atStartOfDay(ZoneId.systemDefault()).toLocalDate()
                val now = LocalDate.now()
                launch(UI) {
                    ageTextView.text = Period.between(birthday, now).years.toString()
                }
            }

        }
        Picasso.get()
            .load("https://media.notify.moe/images/covers/large/$id.jpg")
            .error(R.drawable.ic_image_off_black_24dp)
            .into(coverImageView)

        saveEditUserFab.show()
        saveEditUserFab.setOnClickListener {
            val action = UserFragmentDirections.actionEditUser(id)
            action.setUserId(id)
            it.findNavController().navigate(action)
        }
    }
}
