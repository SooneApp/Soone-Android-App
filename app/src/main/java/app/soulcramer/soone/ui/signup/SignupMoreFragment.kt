package app.soulcramer.soone.ui.signup

import `fun`.soone.R
import android.app.DatePickerDialog
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import app.soulcramer.soone.api.SooneService
import app.soulcramer.soone.common.afterTextChanged
import app.soulcramer.soone.common.observeK
import app.soulcramer.soone.di.Injectable
import app.soulcramer.soone.ui.search.SearchFragmentDirections
import app.soulcramer.soone.ui.user.UserViewModel
import app.soulcramer.soone.ui.user.edit.EditUserFragmentArgs
import app.soulcramer.soone.ui.user.edit.EditUserFragmentDirections
import app.soulcramer.soone.vo.user.Sex
import app.soulcramer.soone.vo.user.User
import kotlinx.android.synthetic.main.fragment_user_edit.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject


class SignupMoreFragment : Fragment(), Injectable, SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var service: SooneService

    private lateinit var signupViewModel: SignupViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var newUser: User
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var searchParam: SooneService.SearchBody

    private val toolbar: ActionBar by lazy {
        (activity as AppCompatActivity).supportActionBar!!
    }
    private var isValid = true
        set(value) {
            field = value
            if (value) {
                saveEditUserFab.show()
            } else {
                saveEditUserFab.hide()
            }
        }


    private val sharedPreferences: SharedPreferences by lazy {
        requireActivity().getSharedPreferences("sooneSharedPref", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_user_edit, container, false)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        signupViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(SignupViewModel::class.java)
        userViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(UserViewModel::class.java)

        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        launchActiveChat(sharedPreferences)

        toolbar.title = "Edition De Profile"

        saveEditUserFab.show()
        saveEditUserFab.setOnClickListener {
            save(it)
        }

        val userId = EditUserFragmentArgs.fromBundle(arguments).userId
        suspend fun updateUser(newUser: User) {

            launch(UI) {
                user.value = newUser
                val birthdate = LocalDate.of(1995, 1, 1).format(DateTimeFormatter.ISO_LOCAL_DATE)
                val now = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
                newUser.apply {
                    nickName = "Loïc"
                    this.birthDate = birthdate
                    sex = Sex.MALE.toInt()
                    description = """Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor
                 incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco
                laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit
                in voluptate velit esse cillum dolore eu fugiat nulla pariatur.
                Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."""
                    lastSeen = now
                }
                userRepository.updateUser(newUser).await()
                setId(newUser.id)
            }
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

                val date = LocalDate.parse(birthDate, DateTimeFormatter.ISO_DATE_TIME)
                birthdateTextInputLayout.editText?.setText(date.format(DateTimeFormatter.ISO_DATE))


                datePickerDialog = DatePickerDialog(context, { _, year, month, dayOfMonth ->
                    val newUserDate =
                        OffsetDateTime.of(year, month, dayOfMonth, 0, 0, 0, 0, ZoneOffset.UTC)
                    newUser.birthDate = newUserDate.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                    birthdateTextInputLayout.editText?.setText(newUserDate.format(DateTimeFormatter.ISO_DATE))

                }, date.year, date.month.value, date.dayOfMonth)

                birthdateTextInputLayout.editText?.setOnTouchListener { _, _ ->
                    datePickerDialog.show()
                    false
                }


            }
        }
    }

    override fun onDestroyView() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onDestroyView()
    }

    override fun onSharedPreferenceChanged(sharedPreference: SharedPreferences, key: String) {
        if (key == "activeChatId" || key == "activeDecision") {
            launchActiveChat(sharedPreference)
        }
    }

    private fun launchActiveChat(sharedPreferences: SharedPreferences) {
        val activeChatId = sharedPreferences.getString("activeChatId", "")
        val activeDecision = sharedPreferences.getBoolean("activeDecision", false)
        val activeDecisionId = sharedPreferences.getString("activeDecisionId", "")
        if (activeDecision) {
            val action = SearchFragmentDirections.action_search_to_match2(activeChatId, activeDecisionId)
            action.setChatId(activeChatId)
            action.setDecisionId(activeDecisionId)
            findNavController().navigate(action)
        }
        if (activeChatId.isNotEmpty()) {
            val action = SearchFragmentDirections.action_search_to_chat2(activeChatId)
            action.setActiveChatId(activeChatId)
            findNavController().navigate(action)
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
