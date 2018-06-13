package app.soulcramer.soone.ui.user.edit

import `fun`.soone.R
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import app.soulcramer.soone.common.afterTextChanged
import app.soulcramer.soone.common.observeK
import app.soulcramer.soone.di.Injectable
import app.soulcramer.soone.ui.user.UserViewModel
import app.soulcramer.soone.vo.user.User
import kotlinx.android.synthetic.main.fragment_user_edit.*
import javax.inject.Inject

class EditUserFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var userViewModel: UserViewModel

    private lateinit var newUser: User

    private var isValid = true

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_edit, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        userViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
                .get(UserViewModel::class.java)

        val userId = EditUserFragmentArgs.fromBundle(arguments).userId

        userViewModel.setId(userId)
        userViewModel.user.observeK(this) { userResource ->
            userResource.data?.run {
                newUser = this
                nickNameTextInputLayout.editText?.setText(nickName)
                descriptionTextInputLayout.editText?.setText(description)
            }
        }

        nickNameTextInputLayout.editText?.afterTextChanged {
            nickNameTextInputLayout.editText?.error =
                    if (it.isNotBlank()) {
                        newUser.nickName = it.trim()
                        isValid
                        actionOnDifferent()
                        null
                    } else {
                        "Votre pseudonyme est requis"
                    }
        }

        descriptionTextInputLayout.editText?.afterTextChanged {
            nickNameTextInputLayout.editText?.error =
                    if (it.trim().length < 500) {
                        newUser.description = it.trim()
                        actionOnDifferent()
                        null
                    } else {
                        "La description dois faire moins de 500 characters"
                    }
        }

    }

    private fun actionOnDifferent() {
        if (!newUser.isSameAs(userViewModel.user.value?.data)) {
            saveEditUserFab.show()
            saveEditUserFab.setOnClickListener {
                userViewModel.updateUser(newUser)
                val action = EditUserFragmentDirections.actionSaveEditUser()
                it.findNavController().navigate(action)
            }
        }
    }
}
