package app.soulcramer.soone.di

import app.soulcramer.soone.ui.contact.ContactFragment
import app.soulcramer.soone.ui.contact.chat.ChatFragment
import app.soulcramer.soone.ui.contact.chat.match.MatchFragment
import app.soulcramer.soone.ui.search.SearchFragment
import app.soulcramer.soone.ui.settings.SettingsFragment
import app.soulcramer.soone.ui.user.UserFragment
import app.soulcramer.soone.ui.user.edit.EditUserFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeUserFragment(): UserFragment

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun contributeDashboardFragment(): SearchFragment

    @ContributesAndroidInjector
    abstract fun contributeEditUserFragment(): EditUserFragment

    @ContributesAndroidInjector
    abstract fun contributeContactFragment(): ContactFragment

    @ContributesAndroidInjector
    abstract fun contributeChatFragment(): ChatFragment

    @ContributesAndroidInjector
    abstract fun contributeMatchFragment(): MatchFragment
}
