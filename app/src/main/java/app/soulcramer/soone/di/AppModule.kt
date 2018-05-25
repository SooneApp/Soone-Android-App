package app.soulcramer.soone.di

import app.soulcramer.soone.api.SooneService
import app.soulcramer.soone.db.UserDao
import app.soulcramer.soone.db.userDao
import app.soulcramer.soone.util.LiveDataCallAdapterFactory
import com.google.gson.GsonBuilder
import com.zhuinden.monarchy.Monarchy
import dagger.Module
import dagger.Provides
import io.realm.RealmConfiguration
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideGithubService(): SooneService {
        val gson = GsonBuilder().create()
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            // .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://soone.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(client)
            .build()
            .create(SooneService::class.java)
    }

    @Singleton
    @Provides
    fun provideUserDao(monarchy: Monarchy): UserDao = monarchy.userDao()

    @Provides
    @Singleton
    fun realmConfiguration(): RealmConfiguration {
        return RealmConfiguration.Builder() //
            .deleteRealmIfMigrationNeeded().build()
    }

    @Provides
    @Singleton
    fun monarchy(realmConfiguration: RealmConfiguration): Monarchy {
        return Monarchy.Builder() //
            .setRealmConfiguration(realmConfiguration) //
            .build()
    }
}
