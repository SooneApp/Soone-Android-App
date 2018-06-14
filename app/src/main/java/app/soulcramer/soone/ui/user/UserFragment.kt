package app.soulcramer.soone.ui.user

import `fun`.soone.R
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import app.soulcramer.soone.common.observeK
import app.soulcramer.soone.di.Injectable
import app.soulcramer.soone.vo.user.Sex
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_user.*
import org.threeten.bp.LocalDate
import org.threeten.bp.Period
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

class UserFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var userViewModel: UserViewModel

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

        userViewModel.user.observeK(this) { userResource ->
            userResource.data?.run {
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
                    val date = LocalDate.parse(birthDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                    val birthday = date.atStartOfDay(ZoneId.systemDefault()).toLocalDate()
                    val now = LocalDate.now()
                    ageTextView.text = Period.between(birthday, now).years.toString()
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
//        userViewModel.retry()
    }
}
