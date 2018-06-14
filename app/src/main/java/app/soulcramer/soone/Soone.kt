package app.soulcramer.soone

import `fun`.soone.BuildConfig
import android.app.Activity
import android.app.Application
import app.soulcramer.soone.di.AppInjector
import com.github.ajalt.timberkt.Timber
import com.jakewharton.threetenabp.AndroidThreeTen
import com.zhuinden.monarchy.Monarchy
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class Soone : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        Monarchy.init(this)
        AppInjector.init(this)
        AndroidThreeTen.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            /*StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
            )
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
            )*/
        }
    }

    override fun activityInjector() = dispatchingAndroidInjector
}
