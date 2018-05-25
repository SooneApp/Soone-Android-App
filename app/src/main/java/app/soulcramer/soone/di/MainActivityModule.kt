package app.soulcramer.soone.di

import app.soulcramer.soone.ui.HomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun userMainActivity(): HomeActivity
}
