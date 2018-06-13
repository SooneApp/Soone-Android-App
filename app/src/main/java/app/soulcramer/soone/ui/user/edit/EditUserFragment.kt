package app.soulcramer.soone.ui.user.edit

import `fun`.soone.R
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.findNavController
import app.soulcramer.soone.common.afterTextChanged
import app.soulcramer.soone.common.observeK
import app.soulcramer.soone.di.Injectable
import app.soulcramer.soone.ui.user.UserViewModel
import app.soulcramer.soone.vo.user.Sex
import app.soulcramer.soone.vo.user.User
import kotlinx.android.synthetic.main.fragment_user_edit.*
import javax.inject.Inject

class EditUserFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var userViewModel: UserViewModel

    private lateinit var newUser: User

    private var isValid = true
        set(value) {
            field = value
            if (value) {
                saveEditUserFab.show()
            } else {
                saveEditUserFab.hide()
            }
        }

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

                val sexes = enumValues<Sex>()
                    .sortedBy { it.toInt() }

                val sexStrings = sexes
                    .map { getString(it.stringRes()) }

                val dataAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, sexStrings)
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                sexSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {}

                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                        newUser.sex = position + 1
                    }
                }
                sexSpinner.adapter = dataAdapter


            }
        }

        saveEditUserFab.show()
        saveEditUserFab.setOnClickListener {
            save(it)
        }

        nickNameTextInputLayout.editText?.afterTextChanged {
            nickNameTextInputLayout.editText?.error =
                if (it.isNotBlank()) {
                    newUser.nickName = it.trim()
                    isValid = true
                    null
                } else {
                    isValid = false
                    "Votre pseudonyme est requis"
                }
        }

        descriptionTextInputLayout.editText?.afterTextChanged {
            nickNameTextInputLayout.editText?.error =
                if (it.trim().length < 500) {
                    newUser.description = it.trim()
                    isValid = true
                    null
                } else {
                    isValid = false
                    "La description dois faire moins de 500 characters"
                }
        }

    }

    private fun save(view: View) {
        if (isValid) {
            userViewModel.updateUser(newUser)
            val action = EditUserFragmentDirections.actionSaveEditUser()
            view.findNavController().navigate(action)
        }
    }
}
