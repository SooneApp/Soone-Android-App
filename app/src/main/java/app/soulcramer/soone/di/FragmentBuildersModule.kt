package app.soulcramer.soone.di

import app.soulcramer.soone.ui.dashboard.DashboardFragment
import app.soulcramer.soone.ui.settings.SettingsFragment
import app.soulcramer.soone.ui.user.UserFragment
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
    abstract fun contributeDashboardFragment(): DashboardFragment
}
