package app.soulcramer.soone.di

import android.app.Application
import app.soulcramer.soone.Soone
import com.zhuinden.monarchy.Monarchy
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import io.realm.RealmConfiguration
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        MainActivityModule::class]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun realmConfiguration(): RealmConfiguration

    fun monarchy(): Monarchy

    fun inject(soone: Soone)
}
