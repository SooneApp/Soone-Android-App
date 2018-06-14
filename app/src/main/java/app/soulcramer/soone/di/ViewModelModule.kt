package app.soulcramer.soone.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import app.soulcramer.soone.ui.contact.ContactsViewModel
import app.soulcramer.soone.ui.dashboard.DashboardViewModel
import app.soulcramer.soone.ui.user.UserViewModel
import app.soulcramer.soone.viewmodel.SooneViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    abstract fun bindUserViewModel(userViewModel: UserViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    abstract fun bindDashboardViewModel(dashboardViewModel: DashboardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ContactsViewModel::class)
    abstract fun bindContactsViewModel(contactsViewModel: ContactsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: SooneViewModelFactory): ViewModelProvider.Factory
}
