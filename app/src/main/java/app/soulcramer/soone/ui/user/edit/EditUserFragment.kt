package app.soulcramer.soone.ui.user.edit

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.soulcramer.soone.R
import app.soulcramer.soone.common.observeK
import app.soulcramer.soone.di.Injectable
import app.soulcramer.soone.ui.user.UserViewModel
import kotlinx.android.synthetic.main.fragment_user_edit.*
import javax.inject.Inject

class EditUserFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_edit, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        userViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(UserViewModel::class.java)

        val userId = EditUserFragmentArgs.fromBundle(arguments).userId
        
        userViewModel.setId(userId)
        userViewModel.user.observeK(this) { userResource ->
            userResource.data?.run {
                nickNameTextInputLayout.editText?.setText(nickName)
                descriptionTextInputLayout.editText?.setText(description)
            }
        }
    }
}
