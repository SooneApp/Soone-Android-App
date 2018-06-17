package app.soulcramer.soone.ui

import `fun`.soone.R
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import app.soulcramer.soone.common.observeK
import app.soulcramer.soone.di.Injectable
import app.soulcramer.soone.ui.user.UserViewModel
import app.soulcramer.soone.vo.Error
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class HomeActivity : AppCompatActivity(), Injectable, HasSupportFragmentInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var userViewModel: UserViewModel

    private val navController by lazy { findNavController(R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(UserViewModel::class.java)

        userViewModel.user.observeK(this) {
            if (it.status is Error) {
                userViewModel.connectUser("0603030303")
            }
        }
        userViewModel.connectUser("0603030303")

        bottomNavigationView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()

    override fun supportFragmentInjector() = dispatchingAndroidInjector
}
