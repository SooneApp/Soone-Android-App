package app.soulcramer.soone.ui.signup

import `fun`.soone.R
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
import androidx.navigation.fragment.findNavController
import app.soulcramer.soone.api.SooneService
import app.soulcramer.soone.di.Injectable
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject


class SignupFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var service: SooneService

    private lateinit var signupViewModel: SignupViewModel

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
    ): View? = inflater.inflate(R.layout.fragment_signup, container, false)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        signupViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(SignupViewModel::class.java)

        toolbar.title = "Inscription"

        startSingupButton.setOnClickListener {
            launch(CommonPool) {
                val user = signupViewModel.createUser(phoneTextInputEditText.text.toString())
                val action = SignupFragmentDirections.action_signupFragment_to_signupMoreFragment(user.id)
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onDestroyView()
    }
}
