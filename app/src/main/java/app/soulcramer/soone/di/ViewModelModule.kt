package app.soulcramer.soone.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import app.soulcramer.soone.ui.contact.ContactsViewModel
import app.soulcramer.soone.ui.contact.chat.ChatViewModel
import app.soulcramer.soone.ui.contact.chat.match.MatchViewModel
import app.soulcramer.soone.ui.search.SearchViewModel
import app.soulcramer.soone.ui.signup.SignupViewModel
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
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindDashboardViewModel(searchViewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ContactsViewModel::class)
    abstract fun bindContactsViewModel(contactsViewModel: ContactsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    abstract fun bindChatViewModel(chatViewModel: ChatViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(MatchViewModel::class)
    abstract fun bindMatchViewModel(matchViewModel: MatchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignupViewModel::class)
    abstract fun bindSignupViewModel(signupViewModel: SignupViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: SooneViewModelFactory): ViewModelProvider.Factory
}
